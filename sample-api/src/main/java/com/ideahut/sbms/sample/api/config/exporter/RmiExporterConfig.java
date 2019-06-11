package com.ideahut.sbms.sample.api.config.exporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ideahut.sbms.client.remote.RemoteMethodService;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterBase;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterInterceptor;
import com.github.ideahut.sbms.shared.remote.service.exporter.RmiServiceExporter;
import com.ideahut.sbms.sample.client.service.TestService;

@Configuration
public class RmiExporterConfig extends ServiceExporterBase {
	
	private static final int PORT = 1699;
	
	@Autowired
	private ServiceExporterInterceptor interceptor;
	
	
	@Autowired
	private TestService testService;
	
	@Bean
	public RmiServiceExporter TestServiceExporter() {
		RmiServiceExporter exporter = export(
			RmiServiceExporter.class, 
			TestService.class, 
			testService, 
			PORT, 
			"TestService"
		);
		exporter.addInterceptor(interceptor);
		return exporter;
	}
	
	
	@Autowired
	private RemoteMethodService remoteMethodService;
	
	@Bean
	public RmiServiceExporter RemoteMethodServiceExporter() {
		RmiServiceExporter exporter = export(
			RmiServiceExporter.class, 
			RemoteMethodService.class, 
			remoteMethodService, 
			PORT, 
			"RemoteMethodService"
		);
		exporter.addInterceptor(interceptor);
		return exporter;
	}
}
