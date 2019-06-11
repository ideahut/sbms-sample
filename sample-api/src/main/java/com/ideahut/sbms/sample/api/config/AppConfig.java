package com.ideahut.sbms.sample.api.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ideahut.sbms.client.remote.RemoteMethodService;
import com.github.ideahut.sbms.shared.audit.AuditExecutor;
import com.github.ideahut.sbms.shared.audit.interceptor.TransactionManagerAuditEntityInterceptor;
import com.github.ideahut.sbms.shared.entity.EntityInterceptor;
import com.github.ideahut.sbms.shared.optional.mapper.SysParamMapper;
import com.github.ideahut.sbms.shared.optional.sysparam.SysParamRepository;
import com.github.ideahut.sbms.shared.remote.RemoteMethodServiceImpl;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterInterceptor;
import com.ideahut.sbms.sample.api.interceptor.AccessExporterInterceptor;
import com.ideahut.sbms.sample.api.interceptor.AccessHandlerInterceptor;
import com.ideahut.sbms.sample.api.support.AppProperties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	entityManagerFactoryRef = "entityManagerFactory",
	transactionManagerRef = "transactionManager",
	basePackages = { 
		"com.ideahut.sbms.sample.api.repository",
		"com.github.ideahut.sbms.shared.optional.sysparam"
	}
)
public class AppConfig {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private AppProperties appProperties;
	
	@Autowired
	private AuditExecutor auditExecutor;
	
	@Primary
	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		String jndi = appProperties.getEnvironment().getProperty("spring.datasource.jndi-name", "").trim();
		if (!jndi.isEmpty()) {
			JndiDataSourceLookup lookup = new JndiDataSourceLookup();
			return lookup.getDataSource(jndi);
		} else {
			return DataSourceBuilder.create().build();
		}
    }
	
	@Primary
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
		EntityManagerFactoryBuilder builder, 
		@Qualifier("dataSource") DataSource dataSource
	) {
		Map<String, Object> properties = appProperties.getHibernateProperties("spring.jpa.properties");		
		return builder
			.dataSource(dataSource)
			.packages(
				"com.ideahut.sbms.sample.api.entity", 
				"com.github.ideahut.sbms.shared.optional.sysparam"
			)			
			.persistenceUnit("default")
			.properties(properties)
			.build();
	}

	@Primary
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(
		@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory
	) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
	
	@Bean
    public TaskExecutor taskExecutor() {		
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(appProperties.getTask().getExecutor().getCorePoolSize());
        executor.setMaxPoolSize(appProperties.getTask().getExecutor().getMaxPoolSize());
        executor.setThreadNamePrefix(appProperties.getTask().getExecutor().getThreadNamePrefix());
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
	
	@Bean
	public SysParamMapper sysParamMapper(SysParamRepository sysParamRepository) {
		SysParamMapper mapper = new SysParamMapper();
		mapper.setSysParamRepository(sysParamRepository);
		return mapper;
	}
	
	@Bean
	public List<EntityInterceptor> listEntityInterceptor() {
		List<EntityInterceptor> list = new ArrayList<EntityInterceptor>();
		list.add(new TransactionManagerAuditEntityInterceptor());
		//list.add(new DefaultAuditEntityInterceptor());
		return list;
	}
	
	@Bean
	public ServiceExporterInterceptor serviceExporterInterceptor() {
		AccessExporterInterceptor interceptor = new AccessExporterInterceptor();
		interceptor.setAuditExecutor(auditExecutor);
		interceptor.setEntityInterceptors(listEntityInterceptor());		
		return interceptor;
	}
	
	@Bean
	public AccessHandlerInterceptor accessHandlerInterceptor() {
		AccessHandlerInterceptor interceptor = new AccessHandlerInterceptor();
		interceptor.setAuditExecutor(auditExecutor);
		interceptor.setEntityInterceptors(listEntityInterceptor());
		interceptor.setIgnoredHandlerClasses(appProperties.getIgnoredHandlerClasses());
		return interceptor;
	}
	
	@Bean
	public RemoteMethodService RemoteMethodService() {
		RemoteMethodServiceImpl service = new RemoteMethodServiceImpl();
		service.setApplicationContext(applicationContext);
		service.setServiceInterfaces(appProperties.getRemoteMethodServices());
		return service;
	}
	
	
}
