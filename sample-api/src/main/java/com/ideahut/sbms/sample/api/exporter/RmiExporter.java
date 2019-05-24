package com.ideahut.sbms.sample.api.exporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ideahut.sbms.client.service.RemoteMethodService;
import com.github.ideahut.sbms.shared.remote.cutom.RmiServiceExporter;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterBase;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterInterceptor;
import com.ideahut.sbms.sample.client.service.TestService;

@Configuration
public class RmiExporter extends ServiceExporterBase {
	
	private static final int PORT = 1699;
	
	@Autowired
	private ServiceExporterInterceptor interceptor;
	
	
	@Autowired
	private TestService testService;
	
	@Bean
	public RmiServiceExporter TestServiceExporter() {
		return export(
			RmiServiceExporter.class, 
			TestService.class, 
			testService, 
			PORT, 
			"TestService"
		).setInterceptor(interceptor);
	}
	
	
	@Autowired
	private RemoteMethodService remoteMethodService;
	
	@Bean
	public RmiServiceExporter RemoteMethodServiceExporter() {
		return export(
			RmiServiceExporter.class, 
			RemoteMethodService.class, 
			remoteMethodService, 
			PORT, 
			"RemoteMethodService"
		).setInterceptor(interceptor);
	}
}
