package com.ideahut.sbms.sample.api.config;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.github.ideahut.sbms.shared.helper.MessageHelper;
import com.github.ideahut.sbms.shared.helper.impl.MessageHelperImpl;
import com.github.ideahut.sbms.shared.util.RequestUtil;

@Configuration
public class MessageConfig {

	@Autowired
	private AppProperties appProperties;
	
	@Bean
	public LocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver() {
			@Override
			public Locale resolveLocale(HttpServletRequest request) {
				Locale defaultLocale = this.getDefaultLocale();
				if (defaultLocale == null) {
					defaultLocale = Locale.getDefault();
				}
				String headerLang = RequestUtil.getHeader(request, HttpHeaders.ACCEPT_LANGUAGE);
				if (headerLang == null || headerLang.isEmpty()) {
					return defaultLocale;
				}
				Locale locale = Locale.lookup(Locale.LanguageRange.parse(headerLang), this.getSupportedLocales());
				return locale != null ? locale : defaultLocale;
			}			
		};
	    localeResolver.setDefaultLocale(appProperties.getLanguage().getPrimary());
	    localeResolver.setSupportedLocales(appProperties.getLanguage().getSupported());
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
		messageHelper.setCheckArguments(true);
		return messageHelper;
	}
	
}
