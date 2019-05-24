package com.ideahut.sbms.sample.client.dto;

import com.github.ideahut.sbms.client.dto.base.DtoTime;

@SuppressWarnings("serial")
public class AccessDto extends DtoTime {
	
	private String key;
	
	private UserDto user;
	
	private String validation;
	
	private Long expiration;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}
	
}
