package com.ideahut.sbms.sample.api.service.impl;

import org.springframework.stereotype.Service;

import com.github.ideahut.sbms.client.dto.CodeMessageDto;
import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.github.ideahut.sbms.shared.annotation.Public;
import com.ideahut.sbms.sample.client.service.TestService;

@Service
public class TestServiceImpl implements TestService {

	@Public
	@Override
	public String halo() {
		return "OLAH";
	}

	@Public
	@Override
	public int add(int a, int b) {
		return a + b;
	}

	@Public
	@Override
	public CodeMessageDto code() {
		return new CodeMessageDto("XX", "OK");
	}

	@Public
	@Override
	public ResponseDto text(String text) {
		return ResponseDto.SUCCESS(text);
	}

	@Public
	@Override
	public void poke(boolean yes) {
				
	}
	
}
