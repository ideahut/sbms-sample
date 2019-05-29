package com.ideahut.sbms.sample.view.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.github.ideahut.sbms.shared.filter.MDCFilter;

@Configuration
public class FilterConfig {
	
	@Autowired
	private Environment environment;
	
	@Bean
	public FilterRegistrationBean<MDCFilter> mdcFilter() {
		FilterRegistrationBean<MDCFilter> bean = new FilterRegistrationBean<MDCFilter>();
		MDCFilter filter = new MDCFilter();
		filter.setEnvironment(environment);
		bean.setFilter(filter);
		bean.addUrlPatterns("/*");
		bean.setOrder(1);
		return bean;
	}
}
