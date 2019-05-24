package com.ideahut.sbms.sample.view.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AnnotationInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//System.out.println(handler);
		//System.out.println(handler.getClass());
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
            //String controllerName = handlerMethod.getBean().getClass().getSimpleName().replace("Controller", "");
            //actionName = handlerMethod.getMethod().getName();
			System.out.println(handlerMethod.getBean().getClass());
			System.out.println(handlerMethod.getMethod());
			//handlerMethod.getMethod().getA
		}
		//return super.preHandle(request, response, handler);
		return false;
	}
	
}
