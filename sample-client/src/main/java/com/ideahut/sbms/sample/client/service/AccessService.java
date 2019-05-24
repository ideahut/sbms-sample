package com.ideahut.sbms.sample.client.service;

import com.ideahut.sbms.sample.client.dto.AccessDto;

public interface AccessService {

	public AccessDto key();
	
	public AccessDto info();
	
	public AccessDto login(String username, String authcode);
	
	public AccessDto logout();
	
}
