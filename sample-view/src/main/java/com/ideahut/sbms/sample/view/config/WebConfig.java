package com.ideahut.sbms.sample.view.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.ideahut.sbms.sample.view.app.AppConstant;
import com.ideahut.sbms.sample.view.app.AppProperties;
import com.ideahut.sbms.sample.view.interceptor.RequestHandlerInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private AppProperties appProperties;	

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestHandlerInterceptor());
		registry.addInterceptor(localeChangeInterceptor());
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	

	@Bean
	public RequestHandlerInterceptor requestHandlerInterceptor() {
		return new RequestHandlerInterceptor().setIgnoredHandlerClasses(appProperties.getIgnoredHandlerClasses());
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	    localeChangeInterceptor.setParamName(AppConstant.Request.Parameter.LANGUAGE);
	    return localeChangeInterceptor;
	}
	
}
