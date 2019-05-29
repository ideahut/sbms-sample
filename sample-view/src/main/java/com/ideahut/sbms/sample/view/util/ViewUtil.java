package com.ideahut.sbms.sample.view.util;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ideahut.sbms.sample.view.app.AppConstant;
import com.ideahut.sbms.sample.view.app.AppProperties;
import com.ideahut.sbms.sample.view.object.Page;

public abstract class ViewUtil {
	
	public static void setCurrentPageAttribute(HttpServletRequest request, AppProperties appProperties) {
		Page currentPage = new Page();
		currentPage.setTheme(appProperties.getInitial().getTheme());
		request.setAttribute(AppConstant.Request.Attribute.CURRENT_PAGE, currentPage);
	}
	
	public static boolean isRest(Method handlerMethod) {
		ResponseBody responseBody = handlerMethod.getAnnotation(ResponseBody.class);
		if (responseBody != null) {
			return true;
		}
		RestController restController = handlerMethod.getDeclaringClass().getAnnotation(RestController.class);
		if (restController != null) {
			return true;
		}
		return false;
	}
	
}
