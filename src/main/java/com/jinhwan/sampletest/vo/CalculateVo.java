package com.jinhwan.sampletest.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CalculateVo {
    @ApiModelProperty(value = "이름", example = "password")
	private String name;
    @ApiModelProperty(value = "결정세액", example = "400,000")
	private String confirmTax;
    @ApiModelProperty(value = "퇴직연금공제액", example = "220,000")
	private String retirementPensionDeduction;		

	
	@Builder
    public CalculateVo (String name , String confirmTax , String retirementPensionDeduction) {
    	this.name = name; 
    	this.confirmTax = confirmTax; 
    	this.retirementPensionDeduction = retirementPensionDeduction; 
    }
}
