package com.jinhwan.sampletest.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.jinhwan.sampletest.constant.CommonConstant;
import com.jinhwan.sampletest.constant.MessageConstant;
import com.jinhwan.sampletest.entity.Member;
import com.jinhwan.sampletest.repository.MemberRepository;
import com.jinhwan.sampletest.util.AES256Util;
import com.jinhwan.sampletest.util.HashUtil;
import com.jinhwan.sampletest.util.StringUtil;
import com.jinhwan.sampletest.util.TokenProvider;
import com.jinhwan.sampletest.vo.ResponseVo;
import com.jinhwan.sampletest.vo.SignupUserVo;
import com.jinhwan.sampletest.vo.TaxVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiService {

	private final MemberRepository memberRepository;
	
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final TokenProvider jwtTokenProvider;
	
	private Logger logger = LoggerFactory.getLogger(ApiService.class);
	/**
	 * 회원가입을 진행한다.
	 * @param signupUserVo
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws UnsupportedEncodingException 
	 * @throws InvalidKeyException 
	 */
	public ResponseVo signUp (SignupUserVo signupUserVo) throws NoSuchAlgorithmException {
		ResponseVo responseVo;
		
		//주민등록번호와 이름 빈값인 경우 오류 
		if (StringUtil.isEmpty(signupUserVo.getRegNo())|| StringUtil.isEmpty(signupUserVo.getName())) {
			responseVo = new ResponseVo(MessageConstant.CHECK_USER_INFO);
			return responseVo;
		}
		
		// '-' 존재하는 경우 제거
		String regNo = signupUserVo.getRegNo().replace("-", "");
		signupUserVo.setRegNo(regNo);

		///이미 회원가입이 되어 있는 사람인지 확인
		int	count = (memberRepository.findByRegNo(AES256Util.AES_Encode(regNo))).size();
		if (count > 0) {
			responseVo = new ResponseVo(MessageConstant.DUPLICATION_USER_ID);
			return responseVo;
		}
		
		//패스워드 SHA해싱
		signupUserVo.setPassword(HashUtil.sha256(signupUserVo.getPassword()));
		
		//주민등록번호 값 암호화
		signupUserVo.setRegNo(AES256Util.AES_Encode(signupUserVo.getRegNo()));
		
		Member member = new Member(signupUserVo);
		//회원가입 실행
		memberRepository.save(member);
		
		responseVo = new ResponseVo(CommonConstant.SUCC);
		
		return responseVo;
	}
	
	/**
	 * 로그인을 진행한다. DB회원 테이블에 해당 고객이 있는지 확인하고 존재하는 경우 토큰 발행한다.
	 * @param userId
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public ResponseVo login(String userId , String password) throws NoSuchAlgorithmException {
		ResponseVo responseVo; 
		
		//ID/PW로 토큰 발행
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, HashUtil.sha256(password));
		Authentication authentication =  authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		responseVo = new ResponseVo("Bearer " + jwtTokenProvider.createToken(authentication));
		return responseVo;
	}
	
	/**
	 * jwt토큰값에서 획득한 userId로 유저정보 조회
	 * @param token
	 * @return
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 * @throws InvalidKeyException 
	 */
	public ResponseVo userInfo(String userId){
		ResponseVo responseVo;

		Member member = memberRepository.findByUserId(userId);
		
		member.setRegNo(StringUtil.regNoFormmat(AES256Util.AES_Decode(member.getRegNo())));
		member.setPassword("");	//비밀번호는 보통안준다.
		
		responseVo = new ResponseVo(member);
		return responseVo;
	}
			
	/**
	 * 스크랩 데이터 기반 결정세액 계산
	 * 소수점 한자리는 모두 반올림처리
	 * @return
	 */
	public long calculaeTaxForScrapData (TaxVo taxVo) {
		logger.info("TaxVo: " + taxVo.toString());

		long calTax = taxVo.getCalculatedTax();	//산출세액
			
		//근로소득공제세액		
		long workIncomeTax = calWorkTaxDeduction(calTax);										

		//퇴직연금세액공제
		long retirementPensionDeduction = calRetirementPensionDeduction(taxVo.getRetirementPension()); 	
		
		//의료비 공제금액
		long medicalDeduction =	calMedicalDeducation(taxVo.getTotalPay(), taxVo.getMedicalExpenses());

		//특별세액공제금액 = 보험료공제금액  + 교육비공제금액 + 기부금공제금액 + 의료비공제금액
		long specialTax =  calSpecialTaxDeducation(taxVo.getInsurance(), taxVo.getEducationalExpenses() , taxVo.getDonationExpenses() , medicalDeduction);
		
		//표준세액공제금액
		long nomalTaxDeduction = calNomalTaxDedution(specialTax);
		if (nomalTaxDeduction == 130000) {
			specialTax = 0;
		}
		
		logger.info("산출세액:" + calTax);
		logger.info("근로소득공제세액:" + workIncomeTax);
		logger.info("특별세액공제금액:" + specialTax);
		logger.info("표준세액공제금액:" + nomalTaxDeduction);
		logger.info("퇴직연금세액공제금액:" + retirementPensionDeduction);
		
		// 결정세액 = 산출세액 - 근로소득공제세액 - 특별세액공제금액 - 표준세액공제금액 - 퇴직연금세액공제금액
		long confirmTax = calTax - workIncomeTax - specialTax - nomalTaxDeduction - retirementPensionDeduction;
		logger.info("결정세액수정전:" + confirmTax);

		if (confirmTax < 0) {
			confirmTax = 0;
		}

		logger.info("결정세액수정후:" + confirmTax);

		return confirmTax;
	}
	
	/**
	 * 의료비 공제금액 계산
	 * @param totalPay
	 * @param medicalExpense
	 * @return
	 */										
	public long calMedicalDeducation (long totalPay , long medicalExpense) {
		long medicalDeduction = Math.round((medicalExpense - (totalPay * 0.03)) * 0.15);		
		if (medicalDeduction < 0) {
			medicalDeduction = 0;
		} 
		return medicalDeduction;
	}
	
	/**
	 * 특별세액공제금액 계산
	 * @param insurance
	 * @param educationExpense
	 * @param donationExpense
	 * @param medicalDeduction
	 * @return
	 */
	public long calSpecialTaxDeducation (long insurance , long educationExpense , long donationExpense , long medicalDeduction) {
		long insuranceDeduction = Math.round(insurance * 0.12);					//보험료 공제금액
		long educationDeduction = Math.round(educationExpense * 0.15);			//교육비납입금액
		long donationDeduction = Math.round(donationExpense * 0.15); 			//기부금납입금액

		return insuranceDeduction + educationDeduction + donationDeduction + medicalDeduction;
	}
	/**
	 * 표준세액공제금액
	 * @param specialTax
	 * @return
	 */
	public long calNomalTaxDedution (long specialTax) {
		//표준세액공제금액은 특별세액 공제금액이 13만원보다 적을경우 13만원 , 그 이상일 경우 0원
		return specialTax < 130000 ? 130000 : 0;
	}
	
	/**
	 * 근로세액공제
	 */
	public long calWorkTaxDeduction (long calTax) {
		return Math.round(calTax * 0.55);
	}
	/**
	 * 퇴직연금세액공제금액
	 * @param retirementPension
	 * @return
	 */
	public long calRetirementPensionDeduction (long retirementPension) {
		return Math.round(retirementPension * 0.15);
	}
}
