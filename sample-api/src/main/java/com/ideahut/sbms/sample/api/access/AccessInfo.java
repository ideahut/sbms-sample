package com.ideahut.sbms.sample.api.access;

import javax.servlet.http.HttpServletRequest;

import com.github.ideahut.sbms.shared.remote.service.ServiceExporterRequest;
import com.ideahut.sbms.sample.api.entity.Access;

public class AccessInfo {

	private String key;
	
	private Access access;
	
	private HttpServletRequest httpRequest;
	
	private ServiceExporterRequest exporterRequest;
	
	private boolean accessPublic;
	
	private boolean mustLogin;
	
	
	public AccessInfo(String key, Access access, HttpServletRequest httpRequest, ServiceExporterRequest exporterRequest) {
		this.key = key;
		this.access = access;
		this.httpRequest = httpRequest;
		this.exporterRequest = exporterRequest;
	}
	
	public AccessInfo(String key, Access access) {
		this(key, access, null, null);
	}
	
	public AccessInfo(String key) {
		this(key, null, null, null);
	}
	
	public AccessInfo() { 
		this(null, null, null, null);
	}		

	public String getKey() {
		return key;
	}

	public AccessInfo setKey(String key) {
		this.key = key;
		return this;
	}

	public Access getAccess() {
		return access;
	}

	public AccessInfo setAccess(Access access) {
		this.access = access;
		return this;
	}
	
	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	public AccessInfo setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
		return this;
	}

	public ServiceExporterRequest getExporterRequest() {
		return exporterRequest;
	}

	public AccessInfo setExporterRequest(ServiceExporterRequest exporterRequest) {
		this.exporterRequest = exporterRequest;
		return this;
	}
	
	public boolean isAccessPublic() {
		return accessPublic;
	}

	public AccessInfo setAccessPublic(boolean accessPublic) {
		this.accessPublic = accessPublic;
		return this;
	}

	public boolean isMustLogin() {
		return mustLogin;
	}

	public AccessInfo setMustLogin(boolean mustLogin) {
		this.mustLogin = mustLogin;
		return this;
	}	
	
}
