package com.ideahut.sbms.sample.view.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.github.ideahut.sbms.shared.helper.MessageHelper;
import com.github.ideahut.sbms.shared.helper.impl.MessageHelperImpl;
import com.ideahut.sbms.sample.view.app.AppProperties;

@Configuration
public class MessageConfig {

	@Autowired
	private AppProperties appProperties;
	
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
	    localeResolver.setDefaultLocale(appProperties.getLanguage().getPrimary());
	    return localeResolver;
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        return messageSource;
	}
	
	@Bean
    public MessageHelper messageHelper() {
		MessageHelperImpl messageHelper = new MessageHelperImpl();
		messageHelper.setLocaleResolver(localeResolver());
		messageHelper.setMessageSource(messageSource());
		return messageHelper;
	}
	
}
