package com.ideahut.sbms.sample.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.github.ideahut.sbms.shared.annotation.Login;
import com.github.ideahut.sbms.shared.annotation.Public;
import com.ideahut.sbms.sample.client.dto.AccessDto;
import com.ideahut.sbms.sample.client.service.AccessService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/access")
public class AccessController {
	
	@Autowired
	private AccessService accessService;
	
	@Public
	@RequestMapping(value = "/key", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("key")
	public ResponseDto key() {
		AccessDto result = accessService.key();
		return ResponseDto.SUCCESS(result);
	}
	
	@RequestMapping(value = "/info", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("info")
	public ResponseDto info() {
		AccessDto result = accessService.info();
		return ResponseDto.SUCCESS(result);
	}
	
	@Login(false)
	@RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ApiOperation("login")
	public ResponseDto login(@RequestParam("username") String username, @RequestParam("authcode") String authcode) {
		AccessDto result = accessService.login(username, authcode);
		return ResponseDto.SUCCESS(result);
	}
	
	@RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("logout")
	public ResponseDto logout() {
		AccessDto result = accessService.logout();
		return ResponseDto.SUCCESS(result);
	}
	
}
