package com.ideahut.sbms.sample.api.access;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.remoting.support.RemoteExporter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.github.ideahut.sbms.client.exception.ResponseException;
import com.github.ideahut.sbms.client.service.RemoteMethodService;
import com.github.ideahut.sbms.common.cache.CacheGroup;
import com.github.ideahut.sbms.shared.annotation.Login;
import com.github.ideahut.sbms.shared.annotation.Public;
import com.github.ideahut.sbms.shared.audit.AuditExecutor;
import com.github.ideahut.sbms.shared.audit.AuditHolder;
import com.github.ideahut.sbms.shared.helper.MessageHelper;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterBase;
import com.github.ideahut.sbms.shared.util.RequestUtil;
import com.ideahut.sbms.sample.api.entity.Access;
import com.ideahut.sbms.sample.api.entity.User;
import com.ideahut.sbms.sample.api.repository.AccessRepository;
import com.ideahut.sbms.sample.api.support.AppConstant;
import com.ideahut.sbms.sample.client.Constants;

public class AccessHandlerInterceptor extends HandlerInterceptorAdapter implements InitializingBean {
	
	@Autowired(required = false)
	private AuditExecutor auditExecutor;
	
	@Autowired
	private MessageHelper messageHelper;
	
	@Autowired
	private CacheGroup cacheGroup;
	
	@Autowired
	private AccessRepository accessRepository;
	
	private List<Class<?>> ignoredHandlerClasses;
	
	public AccessHandlerInterceptor setIgnoredHandlerClasses(List<Class<?>> ignoredHandlerClasses) {
		this.ignoredHandlerClasses = ignoredHandlerClasses;
		return this;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (ignoredHandlerClasses == null) {
			ignoredHandlerClasses = new ArrayList<Class<?>>();
		}
		if (!ignoredHandlerClasses.contains(BasicErrorController.class)) {
			ignoredHandlerClasses.add(BasicErrorController.class);
		}
		if (!ignoredHandlerClasses.contains(RemoteMethodService.class)) {
			ignoredHandlerClasses.add(RemoteMethodService.class);
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Method method = ServiceExporterBase.getInvocationMethod(handler, request);
		if (method == null || ignoredHandlerClasses.contains(method.getDeclaringClass())) {
			return true;
		}
		if (handler instanceof RemoteExporter) {			
			RemoteExporter remoteExporter = (RemoteExporter)handler;
			method = remoteExporter.getService().getClass().getMethod(method.getName(), method.getParameterTypes());
		}
		
		Public annotPublic 	= method.getAnnotation(Public.class);
		boolean isPublic 	= annotPublic != null && annotPublic.value() == true;
		Login annotLogin 	= method.getAnnotation(Login.class);
		boolean mustLogin 	= annotLogin == null || annotLogin.value() == true;
		
		String key = RequestUtil.getHeader(request, Constants.Request.Header.ACCESS_KEY);		
		if (!isPublic && key == null) {
			throw new ResponseException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.01", "LBL.ACCESS")));
		}
		
		Access access = null;
		if (key != null) {
			access = cacheGroup.get(AppConstant.CacheGroup.ACCESS, key);
			if (!isPublic) {			
				if (access == null) {
					throw new ResponseException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.02", "LBL.ACCESS")));
				}
				String validation = request.getRemoteAddr() + " " + RequestUtil.getHeader(HttpHeaders.USER_AGENT);
				if (!validation.equals(access.getValidation())) {
					throw new ResponseException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.06", "LBL.ACCESS")));
				}				
				if (access.hasExpired()) {
					cacheGroup.remove(AppConstant.CacheGroup.ACCESS, key);
					accessRepository.deleteById(key);
					throw new ResponseException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.04", "LBL.ACCESS")));
				}				
				User user = access.getUser();
				if (mustLogin && user == null) {
					throw new ResponseException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.14")));			
				}				
			}
		}
		
		User user = access != null ? access.getUser() : null;
		if (auditExecutor != null && user != null) {
			AuditHolder.setAuditor(user.getId() + "::" + user.getUsername());
		}
		
		AccessHolder.set(new AccessInfo(key, access, request, null).setAccessPublic(isPublic).setMustLogin(mustLogin));
		
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (auditExecutor != null) {
			auditExecutor.run();
		}
		AuditHolder.removeAuditor();
		AccessHolder.remove();
		super.postHandle(request, response, handler, modelAndView);
	}	
	
}
