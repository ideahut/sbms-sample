package com.ideahut.sbms.sample.view.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.github.ideahut.sbms.client.remote.RemoteMethodService;
import com.github.ideahut.sbms.shared.audit.AuditExecutor;
import com.github.ideahut.sbms.shared.moment.MomentHolder;
import com.github.ideahut.sbms.shared.remote.service.ServiceExporterBase;
import com.ideahut.sbms.sample.view.app.AppProperties;
import com.ideahut.sbms.sample.view.util.ViewUtil;

public class RequestHandlerInterceptor extends HandlerInterceptorAdapter implements InitializingBean {
	
	@Autowired(required = false)
	private AuditExecutor auditExecutor;
	
	//@Autowired
	//private MessageHelper messageHelper;
	
	@Autowired
	private AppProperties appProperties;
	
	private List<Class<?>> ignoredHandlerClasses;
	
	public RequestHandlerInterceptor setIgnoredHandlerClasses(List<Class<?>> ignoredHandlerClasses) {
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
		boolean isRest = ViewUtil.isRest(method);
		if (!isRest) {
			ViewUtil.setCurrentPageAttribute(request, appProperties);
		}
		//MomentAttributes momentAttributes = MomentHolder.findMomentAttributes(true);
		
		/*
		User user = access != null ? access.getUser() : null;
		if (auditExecutor != null && user != null) {
			momentAttributes.setAuditor(new Auditor(String.valueOf(user.getId()), user.getUsername()));
		}
		*/
		
		
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (auditExecutor != null) {
			auditExecutor.run();
		}
		MomentHolder.removeMomentAttributes();
		super.postHandle(request, response, handler, modelAndView);
	}	
	
}
