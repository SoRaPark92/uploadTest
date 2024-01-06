package com.jinhwan.sampletest.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginVo {
	
    @ApiModelProperty(value = "회원가입한 userID", example = "dul1test12345")
	private String userId;
    @ApiModelProperty(value = "회원가입한 password", example = "password")
    private String password;
}
