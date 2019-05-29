package com.ideahut.sbms.sample.api.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.ideahut.sbms.client.dto.CodeMessageDto;
import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.github.ideahut.sbms.common.cache.CacheGroup;
import com.github.ideahut.sbms.shared.annotation.Public;
import com.github.ideahut.sbms.shared.entity.optional.SysParam;
import com.github.ideahut.sbms.shared.helper.MessageHelper;
import com.github.ideahut.sbms.shared.mapper.optional.SysParamMapper;
import com.github.ideahut.sbms.shared.repo.optional.SysParamRepository;
import com.ideahut.sbms.sample.api.config.AppProperties;
import com.ideahut.sbms.sample.api.entity.Test;
import com.ideahut.sbms.sample.api.repository.TestRepository;
import com.ideahut.sbms.sample.api.service.PhpTestService;
import com.ideahut.sbms.sample.client.dto.TestDto;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/test")
public class TestController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private MessageHelper messageHelper;
	
	@Autowired
	private TestRepository testRepository;
	
	@Autowired
	private SysParamRepository sysParamRepository;
	
	@Autowired
	private SysParamMapper sysParamMapper;
	
	@Autowired
	private PhpTestService phpTestService;
	
	@Autowired
	private ModelMapper modelMapper;

	@Public
	@RequestMapping(value = "/helo", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("Hello")
    public ResponseDto helo() {
		//throw new RuntimeException("ERRR");
        return ResponseDto.SUCCESS("OLEH");
	}
	
	@Public
	@RequestMapping(value = "/message", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("Message")
    public ResponseDto message() {
		String msg = messageHelper.getMessage("E.01", "Text");
        return ResponseDto.SUCCESS(msg);
	}
	
	@Public
	@RequestMapping(value = "/hessian/php/halo", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("Hessian PHP - halo")
    public ResponseDto hessian_php_halo() {
		String res = phpTestService.halo();
		return ResponseDto.SUCCESS(res);
	}
	
	@Public
	@RequestMapping(value = "/hessian/php/add", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("Hessian PHP - add")
    public ResponseDto hessian_php_add() {
		int res = phpTestService.add(1,2);
		return ResponseDto.SUCCESS(res);
	}
	
	@Public
	@RequestMapping(value = "/hessian/php/code", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("Hessian PHP - code")
    public ResponseDto hessian_php_code() {
		CodeMessageDto res = phpTestService.code();
		return ResponseDto.SUCCESS(res);
	}
	
	@Public
	@RequestMapping(value = "/hessian/php/text", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("Hessian PHP - text")
    public ResponseDto hessian_php_text(@RequestParam("text") String text) {
		Object res = phpTestService.text(text);
		return ResponseDto.SUCCESS(res);
	}
	
	@Public
	@RequestMapping(value = "/sysparam/{sys}/{param}", method = {RequestMethod.POST})
    @ApiOperation("SysParam")
    public ResponseDto sysParam_sys_param(@PathVariable("sys") Integer sys, @PathVariable("param") Integer param) {
		SysParam sysParam = sysParamRepository.findBySysAndParam(sys, param);
		return ResponseDto.SUCCESS(sysParamMapper.toDto(sysParam));
	}
	
	@Public
	@RequestMapping(value = "/sysparam/{sys}", method = {RequestMethod.POST})
    @ApiOperation("SysParam")
    public ResponseDto sysParam_sys(@PathVariable("sys") Integer sys) {
		List<SysParam> sysParam = sysParamRepository.findBySys(sys);
		return ResponseDto.SUCCESS(sysParamMapper.toDto(sysParam));
	}
	
	@Public
	@RequestMapping(value = "/audit", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("Audit")
    public ResponseDto audit() {
		
		LOGGER.info("AUDIT");
		Test test = new Test();
		test.setName("AUDIT");
		test = testRepository.save(test);
		return ResponseDto.SUCCESS(modelMapper.map(test, TestDto.class));
		
		/*
		Test test = testRepository.findById(1L).get();
		testRepository.delete(test);
		return ResponseDto.SUCCESS(modelMapper.map(test, TestDto.class));
		*/
	}
	
	
	@Autowired
	private AppProperties props;
	
	@Public
	@RequestMapping(value = "/props", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDto props() {
		return ResponseDto.SUCCESS(props);
	}
	
	
	@Autowired
	private CacheGroup cacheGroup;
	
	@Public
	@RequestMapping(value = "/cache", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDto cache() {
		return ResponseDto.SUCCESS(cacheGroup.groups());
	}
	
}
