package com.jinhwan.sampletest.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class TaxVo {


	//주민등록번호
	private String regNo;
	//총급여
	private long totalPay;
	//산출세액
	private long calculatedTax;
	//퇴직연금
	private long retirementPension;
	//보험료
	private long insurance;
	//의료비
	private long medicalExpenses;
	//교육비
	private long educationalExpenses;
	//의료비
	private long donationExpenses;
	 
	 @Builder
	 public TaxVo (String regNo, long totalPay, long calculatedTax, long retirementPension, long insurance, long medicalExpenses, long educationalExpenses, long donationExpenses) {
    	this.regNo = regNo;
    	this.totalPay = totalPay;
    	this.calculatedTax = calculatedTax;
    	this.retirementPension = retirementPension;
    	this.insurance = insurance;
    	this.medicalExpenses = medicalExpenses;
    	this.educationalExpenses = educationalExpenses;
    	this.donationExpenses = donationExpenses;
    }
}
