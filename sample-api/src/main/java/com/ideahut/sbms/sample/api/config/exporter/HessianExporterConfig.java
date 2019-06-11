package com.ideahut.sbms.sample.api.config.exporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ideahut.sbms.client.remote.RemoteMethodService;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterBase;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterInterceptor;
import com.github.ideahut.sbms.shared.remote.service.exporter.HessianServiceExporter;
import com.ideahut.sbms.sample.client.service.TestService;

@Configuration
public class HessianExporterConfig extends ServiceExporterBase {
	
	private static final String PATH = "/hessian"; 
	
	@Autowired
	private ServiceExporterInterceptor interceptor;
	
	
	@Autowired
	private TestService testService;
	
	@Bean(name = PATH + "/TestService")
	public HessianServiceExporter TestServiceExporter() {
		HessianServiceExporter exporter = export(
			HessianServiceExporter.class, 
			TestService.class, 
			testService
		);
		exporter.addInterceptor(interceptor);
		return exporter;
	}
		
	
	@Autowired
	private RemoteMethodService remoteMethodService;
	
	@Bean(name = PATH + "/RemoteMethodService")
	public HessianServiceExporter RemoteMethodServiceExporter() {
		HessianServiceExporter exporter = export(
			HessianServiceExporter.class, 
			RemoteMethodService.class, 
			remoteMethodService
		);
		exporter.addInterceptor(interceptor);
		return exporter;
	}
}
