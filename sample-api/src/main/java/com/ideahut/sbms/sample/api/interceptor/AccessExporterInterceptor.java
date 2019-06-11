package com.ideahut.sbms.sample.api.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.github.ideahut.sbms.common.cache.CacheGroup;
import com.github.ideahut.sbms.shared.annotation.Login;
import com.github.ideahut.sbms.shared.annotation.Public;
import com.github.ideahut.sbms.shared.audit.Auditor;
import com.github.ideahut.sbms.shared.helper.MessageHelper;
import com.github.ideahut.sbms.shared.interceptor.BaseServiceExporterInterceptor;
import com.github.ideahut.sbms.shared.moment.MomentHolder;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterRequest;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterResult;
import com.github.ideahut.sbms.shared.util.RequestUtil;
import com.ideahut.sbms.sample.api.access.AccessHolder;
import com.ideahut.sbms.sample.api.access.AccessInfo;
import com.ideahut.sbms.sample.api.entity.Access;
import com.ideahut.sbms.sample.api.entity.User;
import com.ideahut.sbms.sample.api.repository.AccessRepository;
import com.ideahut.sbms.sample.api.support.AppConstant;
import com.ideahut.sbms.sample.client.Constants;

public class AccessExporterInterceptor extends BaseServiceExporterInterceptor {
	
	@Autowired
	private MessageHelper messageHelper;
	
	@Autowired
	private CacheGroup cacheGroup;
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Override
	public ResponseDto beforeInvoke(ServiceExporterRequest request) {
		
		Method method = request.getMethod();
		
		Public annotPublic 	= method.getAnnotation(Public.class);
		boolean isPublic 	= annotPublic != null && annotPublic.value() == true;
		Login annotLogin 	= method.getAnnotation(Login.class);
		boolean mustLogin 	= annotLogin == null || annotLogin.value() == true;
		
		String key = (String)request.getAttribute(Constants.Request.Attribute.ACCESS_KEY);		
		if (!isPublic && key == null) {
			return ResponseDto.ERROR(messageHelper.getCodeMessage("E.01", true, "LBL.ACCESS"));
		}
		
		Access access = null;
		if (key != null) {
			access = cacheGroup.get(AppConstant.CacheGroup.ACCESS, key);
			if (!isPublic) {			
				if (access == null) {
					return ResponseDto.ERROR(messageHelper.getCodeMessage("E.02", true, "LBL.ACCESS"));
				}
				HttpServletRequest httpRequest = request.getRequest();
				if (httpRequest != null) {
					// TODO: perlu dicari mekanisme validasi untuk RmiServiceExporter
					String validation = httpRequest.getRemoteAddr() + " " + RequestUtil.getHeader(httpRequest, HttpHeaders.USER_AGENT);
					if (!validation.equals(access.getValidation())) {
						return ResponseDto.ERROR(messageHelper.getCodeMessage("E.06", true, "LBL.ACCESS"));
					}
				}				
				if (access.hasExpired()) {
					cacheGroup.remove(AppConstant.CacheGroup.ACCESS, key);
					accessRepository.deleteById(key);
					return ResponseDto.ERROR(messageHelper.getCodeMessage("E.04", true, "LBL.ACCESS"));
				}				
				User user = access.getUser();
				if (mustLogin && user == null) {
					return ResponseDto.ERROR(messageHelper.getCodeMessage("E.14"));			
				}				
			}
		}
		User user = access != null ? access.getUser() : null;
		if (user != null) {
			MomentHolder.getMomentAttributes().setAuditor(new Auditor(String.valueOf(user.getId()), user.getUsername()));
		}
		String language = (String)request.getAttribute(Constants.Request.Attribute.LANGUAGE);
		MomentHolder.getMomentAttributes().setLanguage(language);
		AccessHolder.set(new AccessInfo(key, access, null, request).setAccessPublic(isPublic).setMustLogin(mustLogin));
				
		return null;
	}

	@Override
	public void afterInvoke(ServiceExporterRequest request, ServiceExporterResult result) {
		AccessHolder.remove();
	}
	
}
