package com.jinhwan.sampletest.vo;

import java.util.HashMap;

import com.jinhwan.sampletest.constant.CommonConstant;
import com.jinhwan.sampletest.constant.MessageConstant;

import io.swagger.annotations.ApiModelProperty;

/**
 * 응답 표준 VO
 *
 */
public class ResponseVo {
	
    @ApiModelProperty(value = "오류코드", example = "2000")
	private String errCode;
    @ApiModelProperty(value = "오류메세지", example = "가입할 수 없는 회원정보입니다.")
	private String errMsg;
    @ApiModelProperty(value = "결과데이터(해당 타입은 API에 따라서 결과가 다를 수 있음)", example = "{}")
	private Object resultData;
	
	public ResponseVo(MessageConstant error) {
		this.errCode = error.getCode();
		this.errMsg = error.getMessage();
	}

	public ResponseVo(String resultData) {
		MessageConstant error = MessageConstant.SUCCESS;

		this.errCode = error.getCode();
		this.errMsg = error.getMessage();
		this.resultData = resultData;
	}

	public ResponseVo(Object resultData) {
		MessageConstant error = MessageConstant.SUCCESS;

		this.errCode = error.getCode();
		this.errMsg = error.getMessage();
		this.resultData = resultData;
	}

	
	/**
	 * 응답 오브젝트 반환
	 * @return the response
	 */
	public HashMap<String, Object> getResponse() {
		HashMap<String, Object> response = new HashMap<String, Object>();
		
		response.put(CommonConstant.ERR_CODE, errCode);
		response.put(CommonConstant.ERR_MSG, errMsg);
		response.put(CommonConstant.RESULT_DATA, resultData);
		
		return response;
	}
}
