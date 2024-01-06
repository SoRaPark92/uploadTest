package com.jinhwan.sampletest.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jinhwan.sampletest.constant.MessageConstant;
import com.jinhwan.sampletest.entity.Member;
import com.jinhwan.sampletest.service.ApiService;
import com.jinhwan.sampletest.util.SecureTokenUtil;
import com.jinhwan.sampletest.vo.LoginVo;
import com.jinhwan.sampletest.vo.ResponseVo;
import com.jinhwan.sampletest.vo.SignupUserVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApiController {
	private final ApiService szsService;

	/**
	 * 회원가입
	 * @param signUserVo
	 * @return
	 */
	
	@ApiOperation(value = "회원가입", notes = "회원가입")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "API결과" , response = ResponseVo.class)
    })
	@RequestMapping(method = RequestMethod.POST, path = "/api/signup")
	public HashMap<String, Object> signup(@RequestBody SignupUserVo signUserVo) {
		ResponseVo responseVo;
		try {
			responseVo = szsService.signUp(signUserVo);
		} catch (Exception e) {
			responseVo = new ResponseVo(MessageConstant.SERVER_ERROR);
			e.printStackTrace();
		}
		return responseVo.getResponse();
	}

	/**
	 * 로그인
	 * @param LoginVo
	 * @return
	 */
	
	@ApiOperation(value = "로그인", notes = "회원가입이 된 경우에만 로그인 가능")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "API결과" , response = ResponseVo.class)
    })
	@RequestMapping(method = RequestMethod.POST, path = "/api/login")
	public HashMap<String, Object> login(@RequestBody LoginVo loginVo) {
		ResponseVo responseVo;
		try {
			responseVo = szsService.login(loginVo.getUserId(), loginVo.getPassword());
		} catch (Exception e) {
			responseVo = new ResponseVo(MessageConstant.SERVER_ERROR);
			e.printStackTrace();
		}
		return responseVo.getResponse();

	}

	/**
	 * 유저정보 조회
	 * @return
	 */

	@ApiOperation(value = "유저정보 조회(회원가입시에 사용한 정보들을 조회)", notes = "로그인이 되어있을 때만 가능 , JWT토큰 삽입 필요")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "API결과" , response = Member.class)
    })
	@RequestMapping(method = RequestMethod.GET, path = "/api/me")
	public HashMap<String, Object> userInfo() {
		//JWT토큰값을 이용하여 비로그인시 오류 발생
		ResponseVo responseVo;
		try {
			responseVo = szsService.userInfo(SecureTokenUtil.getCurrentMemberId());
		} catch (Exception e) {
			responseVo = new ResponseVo(MessageConstant.SERVER_ERROR);
			e.printStackTrace();
		}
		return responseVo.getResponse();
	}
}
