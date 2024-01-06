package com.jinhwan.sampletest.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 회원가입에 필요한 유저 vo
 * @author jinhwan
 *
 */

@Data
public class SignupUserVo {
    @ApiModelProperty(value = "회원가입할 ID", example = "dul1test12345")
	private String userId;
    @ApiModelProperty(value = "회원가입할 password", example = "password")
	private String password;
    @ApiModelProperty(value = "회원가입한 유저명", example = "김둘리")
	private String name;
    @ApiModelProperty(value = "회원가입할 주민등록번호", example = "921108-1582816")
	private String regNo;
}
