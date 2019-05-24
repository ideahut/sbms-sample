package com.ideahut.sbms.sample.api.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.ideahut.sbms.shared.repo.optional.AuditRepository;

// TODO: Masih explore gimana caranya hanya spesifik entity yang discan tanpa harus menggunakan persitance.xml
//@Configuration
@EnableJpaRepositories(
	basePackages = {
		"com.idh.ms.core.repo",
	},
	basePackageClasses = {
		AuditRepository.class	
	},
	entityManagerFactoryRef = "entityManagerFactory", 
	transactionManagerRef = "transactionManager"
)
/*
@EntityScan(
	basePackages = {
		"com.idh.ms.core.entity",
	},
	basePackageClasses = {
		SysParam.class
	}
)
*/
public class DatabaseConfig {

	@Autowired
	private Environment environment;
	
	@Primary
	@Bean(name = "dataSource")
	public DataSource dataSource() {
		String jndi = environment.getProperty("spring.datasource.jndi-name", "").trim();
		if (jndi.length() != 0) {
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
	
	@Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(
        	"com.idh.ms.core.entity", 
        	"com.idh.ms.shared.entity.optional"
        );
                
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", environment.getProperty("spring.jpa.database-platform"));
        properties.put("hibernate.jdbc.batch_size", environment.getProperty("spring.jpa.properties.hibernate.jdbc.batch_size"));
        properties.put("hibernate.order_inserts", environment.getProperty("spring.jpa.properties.hibernate.order_inserts"));
        properties.put("hibernate.order_updates", environment.getProperty("spring.jpa.properties.hibernate.order_updates"));
        properties.put("hibernate.jdbc.batch_versioned_data", environment.getProperty("spring.jpa.properties.hibernate.jdbc.batch_versioned_data"));
        properties.put("hibernate.generate_statistics", environment.getProperty("spring.jpa.properties.hibernate.generate_statistics"));
        properties.put("hibernate.id.new_generator_mappings", environment.getProperty("spring.jpa.properties.hibernate.id.new_generator_mappings"));
        properties.put("hhibernate.cache.use_second_level_cache", environment.getProperty("spring.jpa.properties.hibernate.cache.use_second_level_cache"));
        properties.put("hibernate.globally_quoted_identifiers", environment.getProperty("spring.jpa.properties.hibernate.globally_quoted_identifiers"));
        properties.put("hibernate.format_sql", environment.getProperty("spring.jpa.properties.hibernate.format_sql"));
        properties.put("hibernate.show_sql", environment.getProperty("spring.jpa.properties.hibernate.show_sql"));
        properties.put("hibernate.use_sql_comments", environment.getProperty("spring.jpa.properties.hibernate.use_sql_comments"));
        properties.put("hibernate.type", environment.getProperty("spring.jpa.properties.hibernate.type"));
        properties.put("hibernate.naming.physical-strategy", environment.getProperty("spring.jpa.hibernate.naming.physical-strategy"));
        em.setJpaPropertyMap(properties);

        return em;
	}
	
	@Primary
    @Bean
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
	}	
	
}
