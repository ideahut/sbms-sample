package com.ideahut.sbms.sample.api.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ideahut.sbms.client.service.RemoteMethodService;
import com.github.ideahut.sbms.shared.audit.AuditExecutor;
import com.github.ideahut.sbms.shared.audit.AuditExecutorImpl;
import com.github.ideahut.sbms.shared.audit.handler.AuditHandler;
import com.github.ideahut.sbms.shared.audit.handler.TransactionManagerAuditHandler;
import com.github.ideahut.sbms.shared.audit.interceptor.TransactionManagerAuditEntityInterceptor;
import com.github.ideahut.sbms.shared.entity.EntityInterceptor;
import com.github.ideahut.sbms.shared.remote.RemoteMethodServiceImpl;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterInterceptor;
import com.github.ideahut.sbms.shared.repo.optional.AuditRepository;
import com.github.ideahut.sbms.shared.repo.optional.SysParamRepository;
import com.ideahut.sbms.sample.api.access.AccessExporterInterceptor;
import com.ideahut.sbms.sample.api.access.AccessHandlerInterceptor;

@Configuration
@ComponentScan({
	"com.ideahut.sbms.sample.api",
	"com.github.ideahut.sbms.shared.mapper.optional",
})
@EntityScan(
	basePackages = {
		"com.ideahut.sbms.sample.api.entity",
		"com.github.ideahut.sbms.shared.entity.optional",
	}
)
@EnableJpaRepositories(
	basePackages = {
		"com.ideahut.sbms.sample.api.repository",
		//"com.github.ideahut.sbms.shared.repo.optional",
	},
	basePackageClasses = {
		SysParamRepository.class
	}
)
public class AppConfig {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private AppProperties appProperties;
	
	
	@Bean
	public DataSource dataSource() {
		String jndi = environment.getProperty("spring.datasource.jndi-name", "").trim();
		if (!jndi.isEmpty()) {
			JndiDataSourceLookup lookup = new JndiDataSourceLookup();
			return lookup.getDataSource(jndi);
		} else {
			return DataSourceBuilder
			.create(AppConfig.class.getClassLoader())
			.driverClassName(environment.getProperty("spring.datasource.driver-class-name").trim())
			.url(environment.getProperty("spring.datasource.url").trim())
			.username(environment.getProperty("spring.datasource.username"))
			.password(environment.getProperty("spring.datasource.password"))
			.build();
		}
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
	public AuditHandler auditHandler() {
		TransactionManagerAuditHandler auditHandler = new TransactionManagerAuditHandler();
		auditHandler.setApplicationContext(applicationContext);
		auditHandler.setCreateEntityAuditTable(true);
		auditHandler.setEntityAuditTableSuffix("audit");
		
		//RepositoryAuditHandler auditHandler = new RepositoryAuditHandler();
		//auditHandler.setAuditRepository(auditRepository);
		
		return auditHandler;
	}
	
	@Bean
	public AuditExecutor auditExecutor(AuditRepository auditRepository) {
		AuditExecutorImpl executor = new AuditExecutorImpl();
		executor.setTaskExecutor(taskExecutor());
		executor.setAuditHandler(auditHandler());
		return executor;
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
		return new AccessExporterInterceptor()
			.setEntityInterceptors(listEntityInterceptor());
	}
	
	@Bean
	public AccessHandlerInterceptor accessHandlerInterceptor() {
		return new AccessHandlerInterceptor()
			.setIgnoredHandlerClasses(appProperties.getIgnoredHandlerClasses())
			.setEntityInterceptors(listEntityInterceptor());
	}
	
	@Bean
	public RemoteMethodService RemoteMethodService() {
		RemoteMethodServiceImpl service = new RemoteMethodServiceImpl();
		service.setApplicationContext(applicationContext);
		service.setInterceptor(serviceExporterInterceptor());
		service.setServiceInterfaces(appProperties.getRemoteMethodServices());
		return service;
	}
	
	
	
}
