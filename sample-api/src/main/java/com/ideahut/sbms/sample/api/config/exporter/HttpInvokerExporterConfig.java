package com.ideahut.sbms.sample.api.config.exporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ideahut.sbms.client.remote.RemoteMethodService;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterInterceptor;
import com.github.ideahut.sbms.shared.remote.service.exporter.HttpInvokerServiceExporter;
import com.ideahut.sbms.sample.client.service.TestService;

@Configuration
public class HttpInvokerExporterConfig {
	
	private static final String PATH = "/httpinvoker";
	
	@Autowired
	private ServiceExporterInterceptor interceptor;
	
	private<T> HttpInvokerServiceExporter setup(Class<T> serviceInterface, T service) {
		HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
		exporter.setServiceInterface(serviceInterface);
		exporter.setService(service);
		exporter.addInterceptor(interceptor);
		return exporter;
	}
	
	
	@Autowired
	private TestService testService;
	
	@Bean(name = PATH + "/TestService")
	public HttpInvokerServiceExporter TestServiceExporter() {
		return setup(TestService.class, testService);
	}
	
	
	@Autowired
	private RemoteMethodService remoteMethodService;
	
	@Bean(name = PATH + "/RemoteMethodService")
	public HttpInvokerServiceExporter RemoteMethodServiceExporter() {
		return setup(RemoteMethodService.class, remoteMethodService);
	}
}
