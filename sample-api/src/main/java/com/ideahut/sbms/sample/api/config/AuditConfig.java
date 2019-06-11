package com.ideahut.sbms.sample.api.config;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.ideahut.sbms.shared.audit.AuditExecutor;
import com.github.ideahut.sbms.shared.audit.AuditExecutorImpl;
import com.github.ideahut.sbms.shared.audit.AuditHandler;
import com.github.ideahut.sbms.shared.audit.handler.TransactionManagerAuditHandler;
import com.github.ideahut.sbms.shared.optional.audit.AuditRepository;
import com.github.ideahut.sbms.shared.optional.mapper.AuditMapper;
import com.ideahut.sbms.sample.api.support.AppProperties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	entityManagerFactoryRef = "auditEntityManagerFactory",
	transactionManagerRef = "auditTransactionManager",
	basePackages = { 
		"com.github.ideahut.sbms.shared.optional.audit"
	}
)
public class AuditConfig implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private AppProperties appProperties;
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	@Autowired
	private AuditRepository auditRepository;
	
	
	@Bean(name = "auditDataSource")
	@ConfigurationProperties(prefix = "audit.datasource")
	public DataSource auditDatasource() {
		String jndi = appProperties.getEnvironment().getProperty("audit.datasource.jndi-name", "").trim();
		if (jndi.length() != 0) {
			JndiDataSourceLookup lookup = new JndiDataSourceLookup();
			return lookup.getDataSource(jndi);
		} else {
			return DataSourceBuilder.create().build();
		}
    }
	
	@Bean(name = "auditEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
		EntityManagerFactoryBuilder builder, 
		@Qualifier("auditDataSource") DataSource dataSource
	) {
		Map<String, Object> properties = appProperties.getHibernateProperties("audit.jpa.properties");		
		return builder
			.dataSource(dataSource)
			.packages(
				"com.github.ideahut.sbms.shared.optional.audit"
			)			
			.persistenceUnit("audit")
			.properties(properties)
			.build();
	}

	@Bean(name = "auditTransactionManager")
	public PlatformTransactionManager transactionManager(
		@Qualifier("auditEntityManagerFactory") EntityManagerFactory entityManagerFactory
	) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
	@Bean
	public AuditMapper auditMapper() {
		AuditMapper mapper = new AuditMapper();
		mapper.setAuditRepository(auditRepository);
		return mapper;
	}
	
	@Bean
	public AuditHandler auditHandler() {
		TransactionManagerAuditHandler auditHandler = new TransactionManagerAuditHandler();
		auditHandler.setApplicationContext(applicationContext);
		auditHandler.setCreateEntityAuditTable(true);
		auditHandler.setUseDefaultTransactionManager(true);
		auditHandler.setEntityAuditTableSuffix("audit");
		
		//RepositoryAuditHandler auditHandler = new RepositoryAuditHandler();
		//auditHandler.setAuditRepository(auditRepository);
		
		return auditHandler;
	}
	
	@Bean
	public AuditExecutor auditExecutor() {
		AuditExecutorImpl executor = new AuditExecutorImpl();
		executor.setTaskExecutor(taskExecutor);
		executor.setAuditHandler(auditHandler());
		return executor;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		AuditHandler handler = event.getApplicationContext().getBean(AuditHandler.class);
		try {
			handler.initialize();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
