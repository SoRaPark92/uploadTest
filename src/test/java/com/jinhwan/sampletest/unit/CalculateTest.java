package com.jinhwan.sampletest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jinhwan.sampletest.service.ApiService;
import com.jinhwan.sampletest.vo.TaxVo;

@RunWith( SpringJUnit4ClassRunner.class)
@SpringBootTest

public class CalculateTest {
	@Autowired
	private ApiService szsService;
	
	
	//퇴직연금세액공제 계산
	@Test
	public void calRetirementPensionDeductionTest() {
		
		assertEquals(szsService.calRetirementPensionDeduction((long)1333333.333), 200000);
		assertEquals(szsService.calRetirementPensionDeduction(1000000), 150000);
		assertEquals(szsService.calRetirementPensionDeduction(500100), 75015);

	}
	
	//근로세액공제 계산
	@Test
	public void calWorkTaxDeductionTest() {
		
		assertEquals(szsService.calWorkTaxDeduction((long) 333333.333), 183333);
		assertEquals(szsService.calWorkTaxDeduction(600000), 330000);
		assertEquals(szsService.calWorkTaxDeduction(20000000), 11000000);

	}
	
	//의료비 공제금액 계산
	@Test
	public void calMedicalDeducationTest() {
		
		assertEquals(szsService.calMedicalDeducation(30000000 , 700000), 0);
		assertEquals(szsService.calMedicalDeducation(5000000 , 400000), 37500);

	}
	
	//특별세액공제금액 계산
	@Test
	public void calSpecialTaxDeducationTest() {
		assertEquals(szsService.calSpecialTaxDeducation(100000 , 200000,150000 ,0) , 64500);
		assertEquals(szsService.calSpecialTaxDeducation(120000 , 600000,420000 ,0) ,167400);

	}

	//평균세액공제금액 계산
	@Test
	public void calNomalTaxDedutionTest() {
		assertEquals(szsService.calNomalTaxDedution(54000) , 130000);
		assertEquals(szsService.calNomalTaxDedution(130000) , 0);
		assertEquals(szsService.calNomalTaxDedution(170000) , 0);

	}
	//결정세액 테스트
	@Test
	public void calculaeTaxForScrapData() {
		TaxVo taxVo = new TaxVo("" , (long)30000000 , (long)600000,(long)1333333.333 , (long)100000,(long)700000 , (long)200000,(long)150000);
		assertEquals(szsService.calculaeTaxForScrapData(taxVo) , 0);
		
		taxVo = new TaxVo("" , (long)30000000 , (long)1200000,(long)1333333.333 , (long)100000,(long)700000 , (long)200000,(long)150000);
		assertEquals(szsService.calculaeTaxForScrapData(taxVo) , 210000);

		taxVo = new TaxVo("" , (long)5000000 , (long)20000000,(long)500100 , (long)120000,(long)400000 , (long)600000,(long)420000);
		assertEquals(szsService.calculaeTaxForScrapData(taxVo) , 8720085);

		
	}
}
