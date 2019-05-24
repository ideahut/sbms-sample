package com.ideahut.sbms.sample.view.config;

import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ideahut.sbms.shared.helper.MessageHelper;
import com.github.ideahut.sbms.shared.helper.impl.MessageHelperImpl;

@Configuration
/*
@EntityScan(
	basePackages = {
		"com.idh.ms.core.entity",
		"com.idh.ms.shared.entity.optional",
	}
)
@EnableJpaRepositories(
	basePackages = {
		"com.idh.ms.core.repo",
		"com.idh.ms.shared.repo.optional",
	}
)
*/
public class ContextConfig implements WebMvcConfigurer {
	
	@Autowired
	private Environment env;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// Locale
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	    lci.setParamName("_lang");
	    registry.addInterceptor(lci);
	    
	    //AnnotationInterceptor ai = new AnnotationInterceptor();
	    //registry.addInterceptor(ai).order(Ordered.HIGHEST_PRECEDENCE);
	    
	}

	/*
	@Bean
	public DataSource dataSource() {
		String jndi = env.getProperty("spring.datasource.jndi-name", "").trim();
		if (jndi.length() != 0) {
			JndiDataSourceLookup lookup = new JndiDataSourceLookup();
			return lookup.getDataSource(jndi);
		} else {
			return DataSourceBuilder
			.create(ContextConfig.class.getClassLoader())
			.driverClassName(env.getProperty("spring.datasource.driver-class-name").trim())
			.url(env.getProperty("spring.datasource.url").trim())
			.username(env.getProperty("spring.datasource.username"))
			.password(env.getProperty("spring.datasource.password"))
			.build();
		}
    }
    */
	
	@Bean
    public TaskExecutor taskExecutor() {		
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(env.getProperty("task.executor.corePoolSize", int.class));
        executor.setMaxPoolSize(env.getProperty("task.executor.maxPoolSize", int.class));
        executor.setThreadNamePrefix(env.getProperty("task.executor.threadNamePrefix"));
        executor.initialize();
        return executor;
    }
	
	@Bean
    public MessageHelper messageHelper() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale(env.getProperty("default.locale", "en").trim()));	    
        
		MessageHelperImpl messageHelper = new MessageHelperImpl();
		messageHelper.setLocaleResolver(localeResolver);
		messageHelper.setMessageSource(messageSource);
		return messageHelper;
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
	
}
