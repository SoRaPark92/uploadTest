package com.jinhwan.sampletest.constant;


public enum MessageConstant {

	SUCCESS("0", "성공"),
	
	CHECK_LOGIN_INFO("1000", "아이디/패스워드를 확인해주세요."),
	CHECK_USER_INFO("1001", "주민등록번호와 이름을 확인해주세요."),

	NON_SIGNUP_INFO("2000", "가입할 수 없는 회원정보입니다."),
	DUPLICATION_USER_ID("2001", "이미 가입된 회원입니다."),

	SCRAP_ERROR("3001", "유저정보 스크랩중 오류가 발생하였습니다."),	
	SCRAP_NOT_FOUND("3002", "유저의 스크랩내용이 없어 계산에 실패하였습니다."),

	SERVER_ERROR("9999", "서버 처리중 에러가 발생 했습니다.");

	
	private String code;
	private String message;
	
	private MessageConstant(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
