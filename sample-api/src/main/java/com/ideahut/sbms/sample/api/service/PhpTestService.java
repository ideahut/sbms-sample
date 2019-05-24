package com.ideahut.sbms.sample.api.service;

import com.github.ideahut.sbms.client.dto.CodeMessageDto;

public interface PhpTestService {

	public String halo();
	
	public int add(int a, int b);
	
	public CodeMessageDto code();
	
	public Object text(String text);
	
}
