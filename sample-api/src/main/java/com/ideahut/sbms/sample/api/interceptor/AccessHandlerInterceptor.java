package com.ideahut.sbms.sample.api.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.ModelAndView;

import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.github.ideahut.sbms.client.exception.ResponseException;
import com.github.ideahut.sbms.common.cache.CacheGroup;
import com.github.ideahut.sbms.shared.annotation.Login;
import com.github.ideahut.sbms.shared.annotation.Public;
import com.github.ideahut.sbms.shared.audit.Auditor;
import com.github.ideahut.sbms.shared.helper.MessageHelper;
import com.github.ideahut.sbms.shared.interceptor.BaseHandlerInterceptor;
import com.github.ideahut.sbms.shared.moment.MomentHolder;
import com.github.ideahut.sbms.shared.util.RequestUtil;
import com.ideahut.sbms.sample.api.access.AccessHolder;
import com.ideahut.sbms.sample.api.access.AccessInfo;
import com.ideahut.sbms.sample.api.entity.Access;
import com.ideahut.sbms.sample.api.entity.User;
import com.ideahut.sbms.sample.api.repository.AccessRepository;
import com.ideahut.sbms.sample.api.support.AppConstant;
import com.ideahut.sbms.sample.client.Constants;

public class AccessHandlerInterceptor extends BaseHandlerInterceptor {
	
	@Autowired
	private MessageHelper messageHelper;
	
	@Autowired
	private CacheGroup cacheGroup;
	
	@Autowired
	private AccessRepository accessRepository;
	
	
	@Override
	public boolean beforeHandle(HttpServletRequest request, Method method) throws Exception {
		
		Public annotPublic 	= method.getAnnotation(Public.class);
		boolean isPublic 	= annotPublic != null && annotPublic.value() == true;
		Login annotLogin 	= method.getAnnotation(Login.class);
		boolean mustLogin 	= annotLogin == null || annotLogin.value() == true;
		
		String key = RequestUtil.getHeader(request, Constants.Request.Header.ACCESS_KEY);		
		if (!isPublic && key == null) {
			throw new ResponseException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.01", true, "LBL.ACCESS")));
		}
		
		Access access = null;
		if (key != null) {
			access = cacheGroup.get(AppConstant.CacheGroup.ACCESS, key);
			if (!isPublic) {			
				if (access == null) {
					throw new ResponseException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.02", true, "LBL.ACCESS")));
				}
				String validation = request.getRemoteAddr() + " " + RequestUtil.getHeader(HttpHeaders.USER_AGENT);
				if (!validation.equals(access.getValidation())) {
					throw new ResponseException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.06", true, "LBL.ACCESS")));
				}				
				if (access.hasExpired()) {
					cacheGroup.remove(AppConstant.CacheGroup.ACCESS, key);
					accessRepository.deleteById(key);
					throw new ResponseException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.04", true, "LBL.ACCESS")));
				}				
				User user = access.getUser();
				if (mustLogin && user == null) {
					throw new ResponseException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.14")));			
				}				
			}
		}
		
		User user = access != null ? access.getUser() : null;
		if (user != null) {
			MomentHolder.getMomentAttributes().setAuditor(new Auditor(String.valueOf(user.getId()), user.getUsername()));
		}
		AccessHolder.set(new AccessInfo(key, access, request, null).setAccessPublic(isPublic).setMustLogin(mustLogin));
		
		return true;
	}

	@Override
	public void afterHandle(HttpServletRequest request, ModelAndView modelAndView) throws Exception {
		AccessHolder.remove();
	}	
	
}
