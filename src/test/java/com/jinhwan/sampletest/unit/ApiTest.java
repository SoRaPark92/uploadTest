package com.jinhwan.sampletest.unit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jinhwan.sampletest.constant.CommonConstant;
import com.jinhwan.sampletest.constant.MessageConstant;
import com.jinhwan.sampletest.entity.Member;
import com.jinhwan.sampletest.service.ApiService;
import com.jinhwan.sampletest.vo.SignupUserVo;

@WebAppConfiguration
@SpringBootTest
@RunWith( SpringJUnit4ClassRunner.class)
public class ApiTest {

	@Autowired
	private ApiService apiService;
	
	
	@Before
	public void setup() throws Exception {
		SignupUserVo signupUserVo = new SignupUserVo();
		signupUserVo.setPassword("password");
		signupUserVo.setUserId("dul1test12345");
		signupUserVo.setName("tester");
		signupUserVo.setRegNo("921101-1123517");
		
		apiService.signUp(signupUserVo);
	}
	
	/**
	 * 회원가입 테스트
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void signUpTest() throws NoSuchAlgorithmException {
		SignupUserVo signupUserVo = new SignupUserVo();
		signupUserVo.setPassword("1004wlsghks");
		signupUserVo.setUserId("hong1");
		signupUserVo.setName("홍길동");
		signupUserVo.setRegNo("860101-1545437");
		
		HashMap<String,Object> result = apiService.signUp(signupUserVo).getResponse();
		System.out.println("signUpTest:" + result.toString());

		assertEquals(result.get(CommonConstant.ERR_CODE), MessageConstant.SUCCESS.getCode());
	}
	
	/**
	 * 회원가입 실패 검증
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void signUpTestFail2() throws NoSuchAlgorithmException {
		SignupUserVo signupUserVo = new SignupUserVo();
		signupUserVo.setPassword("password");
		signupUserVo.setUserId("dul2");
		signupUserVo.setName("tester");
		signupUserVo.setRegNo("921101-1123517");
		
		HashMap<String,Object> result = apiService.signUp(signupUserVo).getResponse();
		
		System.out.println("signUpTestFail2:" + result.toString());
		
		assertEquals(result.get(CommonConstant.ERR_CODE), MessageConstant.DUPLICATION_USER_ID.getCode());
	}

	
	/**
	 * 로그인 검증
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void login() throws NoSuchAlgorithmException {
		HashMap<String,Object> result = apiService.login("dul1test12345", "password").getResponse();
		System.out.println("login:" + result.toString());
	}

	/**
	 * 로그인실패 오류검증
	 */
	@Test
	public void loginFail() throws NoSuchAlgorithmException {
        Exception exception = assertThrows(BadCredentialsException.class, () -> {
        	apiService.login("password", "password").getResponse();
        });
    
        /* THEN -> EXPECTED EXCEPTION MESSAGE */  
        assertThat(exception.getMessage(), containsString("자격 증명에 실패하였습니다."));
	}
	
	/**
	 * 로그인 기준 데이터 검증
	 */
	@Test
	public void getUserInfo () {
		HashMap<String,Object> result = apiService.userInfo("dul1test12345").getResponse();
		Member member = (Member)result.get(CommonConstant.RESULT_DATA);
		System.out.println("getUserInfo:" + result.toString());

		assertEquals(result.get(CommonConstant.ERR_CODE), MessageConstant.SUCCESS.getCode());
		assertEquals(member.getUserId(), "dul1test12345");
	}
}
