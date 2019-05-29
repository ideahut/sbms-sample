package com.ideahut.sbms.sample.view.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideahut.sbms.sample.view.app.AppProperties;

@Configuration
public class AppConfig {
	
	@Autowired
	private AppProperties appProperties;
	
	
	@Bean
    public TaskExecutor taskExecutor() {		
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(appProperties.getTask().getExecutor().getCorePoolSize());
        executor.setMaxPoolSize(appProperties.getTask().getExecutor().getMaxPoolSize());
        executor.setThreadNamePrefix(appProperties.getTask().getExecutor().getThreadNamePrefix());
        executor.initialize();
        return executor;
    }
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper;
	}
	
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	
	/*
	@Bean
	public AuditExecutor auditExecutor(AuditRepository auditRepository) {
		AuditExecutorImpl executor = new AuditExecutorImpl();
		executor.setAuditRepository(auditRepository);
		return executor;
	}		
	*/
}
