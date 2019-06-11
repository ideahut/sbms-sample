package com.ideahut.sbms.sample.api.support;

import java.lang.reflect.UndeclaredThrowableException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.github.ideahut.sbms.client.exception.ResponseException;
import com.github.ideahut.sbms.client.exception.ResponseRuntimeException;

@ControllerAdvice
public class ErrorHandler {	
	
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public ResponseDto handleAllException(Throwable throwable) {
    	Throwable ex = throwable;
    	if (ex instanceof UndeclaredThrowableException) {
    		ex = ex.getCause();
    	}
    	ResponseDto result = null;
    	if (ex instanceof ResponseException) {
    		result = ((ResponseException)ex).getResponse();
    	} 
    	else if (ex instanceof ResponseRuntimeException) {
    		result = ((ResponseRuntimeException)ex).getResponse();
    	}
    	else {
    		result = ResponseDto.ERROR("99", ex.getMessage());
    	}
    	result.setInfo("exception", ex.getClass().getName());
    	return result;
    }
    
}