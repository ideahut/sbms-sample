package com.ideahut.sbms.sample.api.config.exporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ideahut.sbms.client.remote.RemoteMethodService;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterInterceptor;
import com.github.ideahut.sbms.shared.remote.service.exporter.RmiServiceExporter;
import com.ideahut.sbms.sample.client.service.TestService;

@Configuration
public class RmiExporterConfig {
	
	@Autowired
	private ServiceExporterInterceptor interceptor;
	
	private<T> RmiServiceExporter setup(Class<T> serviceInterface, T service, String serviceName) {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(serviceInterface);
		exporter.setService(service);
		exporter.setRegistryPort(1699);
		exporter.setServiceName(serviceName);
		exporter.addInterceptor(interceptor);
		return exporter;
	}
	
	@Autowired
	private TestService testService;
	
	@Bean
	public RmiServiceExporter TestServiceExporter() {
		return setup(TestService.class, testService, "TestService");
	}
	
	
	@Autowired
	private RemoteMethodService remoteMethodService;
	
	@Bean
	public RmiServiceExporter RemoteMethodServiceExporter() {
		return setup(RemoteMethodService.class, remoteMethodService, "RemoteMethodService");
	}
	
}
