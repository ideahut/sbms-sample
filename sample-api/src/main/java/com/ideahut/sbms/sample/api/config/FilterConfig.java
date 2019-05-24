package com.ideahut.sbms.sample.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.github.ideahut.sbms.shared.filter.CORSFilter;
import com.github.ideahut.sbms.shared.filter.MDCFilter;
import com.github.ideahut.sbms.shared.filter.RequestWrapperFilter;

@Configuration
public class FilterConfig {
	
	@Autowired
	private Environment environment;
	
	@Bean
	public FilterRegistrationBean<CORSFilter> corsFilter() {
		FilterRegistrationBean<CORSFilter> bean = new FilterRegistrationBean<CORSFilter>();
		CORSFilter filter = new CORSFilter();
		filter.setEnvironment(environment);
		bean.setFilter(filter);
		bean.addUrlPatterns("/*");
		bean.setOrder(1);
		return bean;
	}
	
	@Bean
	public FilterRegistrationBean<RequestWrapperFilter> requestWrapperFilter() {
		FilterRegistrationBean<RequestWrapperFilter> bean = new FilterRegistrationBean<RequestWrapperFilter>();
		RequestWrapperFilter filter = new RequestWrapperFilter();
		filter.setEnvironment(environment);
		bean.setFilter(filter);
		bean.addUrlPatterns("/*");
		bean.setOrder(2);		
		return bean;
	}
	
	@Bean
	public FilterRegistrationBean<MDCFilter> mdcFilter() {
		FilterRegistrationBean<MDCFilter> bean = new FilterRegistrationBean<MDCFilter>();
		MDCFilter filter = new MDCFilter();
		filter.setEnvironment(environment);
		bean.setFilter(filter);
		bean.addUrlPatterns("/*");
		bean.setOrder(3);
		return bean;
	}
}
