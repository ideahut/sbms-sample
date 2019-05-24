package com.ideahut.sbms.sample.client.service;

import com.github.ideahut.sbms.client.dto.CodeMessageDto;
import com.github.ideahut.sbms.client.dto.ResponseDto;

public interface TestService {

	public String halo();
	
	public int add(int a, int b);
	
	public CodeMessageDto code();
	
	public ResponseDto text(String text);
	
	public void poke(boolean yes);
	
}
