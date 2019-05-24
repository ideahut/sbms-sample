package com.ideahut.sbms.sample.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
 * http://localhost:8086/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Autowired
	private Environment env;
	
    @Bean
    public Docket swagger() {
    	return new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(info())
           		//.pathMapping("/swagger")
				.select()
       			//.apis(RequestHandlerSelectors.any())
				.apis(RequestHandlerSelectors.basePackage(env.getProperty("swagger.package", "").trim()))
       			.paths(PathSelectors.any())
       			.build();
                
    }
    
    private ApiInfo info() {
        return new ApiInfoBuilder()
                .title(env.getProperty("swagger.info.title", "").trim())
                .license(env.getProperty("swagger.info.license", "").trim())
                .licenseUrl(env.getProperty("swagger.info.licenseUrl", "").trim())
                .termsOfServiceUrl(env.getProperty("swagger.info.termsOfServiceUrl", "").trim())
                .description(env.getProperty("swagger.info.description", "").trim())
                .version(env.getProperty("swagger.info.version", "").trim())
                .build();
    }
    
}
