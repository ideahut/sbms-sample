package com.ideahut.sbms.sample.api.access;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.github.ideahut.sbms.client.service.RemoteMethodService;
import com.github.ideahut.sbms.common.cache.CacheGroup;
import com.github.ideahut.sbms.shared.annotation.Login;
import com.github.ideahut.sbms.shared.annotation.Public;
import com.github.ideahut.sbms.shared.audit.AuditExecutor;
import com.github.ideahut.sbms.shared.audit.AuditHolder;
import com.github.ideahut.sbms.shared.helper.MessageHelper;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterInterceptor;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterRequest;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterResult;
import com.ideahut.sbms.sample.api.entity.Access;
import com.ideahut.sbms.sample.api.entity.User;
import com.ideahut.sbms.sample.api.repository.AccessRepository;
import com.ideahut.sbms.sample.api.support.AppConstant;
import com.ideahut.sbms.sample.client.Constants;

public class AccessExporterInterceptor implements ServiceExporterInterceptor {
	
	@Autowired(required = false)
	private AuditExecutor auditExecutor;
	
	@Autowired
	private MessageHelper messageHelper;
	
	@Autowired
	private CacheGroup cacheGroup;
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Override
	public ResponseDto beforeInvoke(ServiceExporterRequest request) {
		Method method = request.getMethod();
		if (RemoteMethodService.class.equals(method.getDeclaringClass())) {
			return null;
		}
		try {
			method = request.getExporter().getService().getClass().getMethod(method.getName(), method.getParameterTypes());
		} catch (Exception e) {
			return ResponseDto.ERROR("99", e.getMessage());
		}
		
		Public annotPublic 	= method.getAnnotation(Public.class);
		boolean isPublic 	= annotPublic != null && annotPublic.value() == true;
		Login annotLogin 	= method.getAnnotation(Login.class);
		boolean mustLogin 	= annotLogin == null || annotLogin.value() == true;
		
		String key = (String)request.getAttribute(Constants.Request.Attribute.ACCESS_KEY);		
		if (!isPublic && key == null) {
			return ResponseDto.ERROR(messageHelper.getCodeMessage("E.01", "LBL.ACCESS"));
		}
		
		Access access = null;
		if (key != null) {
			access = cacheGroup.get(AppConstant.CacheGroup.ACCESS, key);
			if (!isPublic) {			
				if (access == null) {
					return ResponseDto.ERROR(messageHelper.getCodeMessage("E.02", "LBL.ACCESS"));
				}
				// TODO: perlu dicari mekanisme validasi untuk ServiceExporter
				//String validation = request.getRemoteAddr() + " " + RequestUtil.getHeader(HttpHeaders.USER_AGENT);
				//if (!validation.equals(access.getValidation())) {
				//	throw new ResponseException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.06", "LBL.ACCESS")));
				//}				
				if (access.hasExpired()) {
					cacheGroup.remove(AppConstant.CacheGroup.ACCESS, key);
					accessRepository.deleteById(key);
					return ResponseDto.ERROR(messageHelper.getCodeMessage("E.04", "LBL.ACCESS"));
				}				
				User user = access.getUser();
				if (mustLogin && user == null) {
					return ResponseDto.ERROR(messageHelper.getCodeMessage("E.14"));			
				}				
			}
		}
		
		User user = access != null ? access.getUser() : null;
		if (auditExecutor != null && user != null) {
			AuditHolder.setAuditor(user.getId() + "::" + user.getUsername());
		}
		
		String language = (String)request.getAttribute(Constants.Request.Attribute.LANGUAGE);
		MessageHelper.LanguageHolder.set(language);
		AccessHolder.set(new AccessInfo(key, access, null, request).setAccessPublic(isPublic).setMustLogin(mustLogin));
				
		return null;
	}

	@Override
	public void afterInvoke(ServiceExporterRequest request, ServiceExporterResult result) {
		if (auditExecutor != null) {
			auditExecutor.run();
		}
		AuditHolder.removeAuditor();
		MessageHelper.LanguageHolder.remove();
		AccessHolder.remove();
	}
	
}
