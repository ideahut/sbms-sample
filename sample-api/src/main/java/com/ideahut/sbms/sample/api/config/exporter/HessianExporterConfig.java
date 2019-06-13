package com.ideahut.sbms.sample.api.config.exporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ideahut.sbms.client.remote.RemoteMethodService;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterInterceptor;
import com.github.ideahut.sbms.shared.remote.service.exporter.HessianServiceExporter;
import com.ideahut.sbms.sample.client.service.TestService;

@Configuration
public class HessianExporterConfig {
	
	private static final String PATH = "/hessian"; 
	
	@Autowired
	private ServiceExporterInterceptor interceptor;
	
	private<T> HessianServiceExporter setup(Class<T> serviceInterface, T service) {
		HessianServiceExporter exporter = new HessianServiceExporter();
		exporter.setServiceInterface(serviceInterface);
		exporter.setService(service);
		exporter.addInterceptor(interceptor);
		return exporter;
	}
	
	@Autowired
	private TestService testService;
	
	@Bean(name = PATH + "/TestService")
	public HessianServiceExporter TestServiceExporter() {
		return setup(TestService.class, testService);
	}
		
	
	@Autowired
	private RemoteMethodService remoteMethodService;
	
	@Bean(name = PATH + "/RemoteMethodService")
	public HessianServiceExporter RemoteMethodServiceExporter() {
		return setup(RemoteMethodService.class, remoteMethodService);
	}
}
