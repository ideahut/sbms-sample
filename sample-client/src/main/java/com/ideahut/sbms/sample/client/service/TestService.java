package com.ideahut.sbms.sample.client.service;

import java.math.BigDecimal;

import com.github.ideahut.sbms.client.dto.CodeMessageDto;
import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.ideahut.sbms.sample.client.dto.TestDto;

public interface TestService {

	public String halo();
	
	public int add(int a, int b);
	
	public CodeMessageDto code();
	
	public ResponseDto text(String text);
	
	public void poke(boolean yes);
	
	public TestDto test(BigDecimal value);
	
}
