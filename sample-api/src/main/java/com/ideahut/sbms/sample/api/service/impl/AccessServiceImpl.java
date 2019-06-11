package com.ideahut.sbms.sample.api.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.github.ideahut.sbms.client.dto.ResponseDto;
import com.github.ideahut.sbms.client.exception.ResponseRuntimeException;
import com.github.ideahut.sbms.common.cache.CacheGroup;
import com.github.ideahut.sbms.common.util.DigestUtil;
import com.github.ideahut.sbms.common.util.TimeUtil;
import com.github.ideahut.sbms.shared.annotation.Login;
import com.github.ideahut.sbms.shared.annotation.Public;
import com.github.ideahut.sbms.shared.helper.MessageHelper;
import com.github.ideahut.sbms.shared.util.RequestUtil;
import com.ideahut.sbms.sample.api.access.AccessHolder;
import com.ideahut.sbms.sample.api.access.AccessInfo;
import com.ideahut.sbms.sample.api.entity.Access;
import com.ideahut.sbms.sample.api.entity.User;
import com.ideahut.sbms.sample.api.repository.AccessRepository;
import com.ideahut.sbms.sample.api.repository.UserRepository;
import com.ideahut.sbms.sample.api.support.AppConstant;
import com.ideahut.sbms.sample.client.dto.AccessDto;
import com.ideahut.sbms.sample.client.service.AccessService;

@Service
public class AccessServiceImpl implements AccessService {
	
	@Autowired
	private MessageHelper messageHelper;
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CacheGroup cacheGroup;
	

	@Public
	@Override
	public AccessDto key() {
		AccessInfo info = AccessHolder.get();
		String key = info.getKey();
		Access access = null;
		if (key == null) {
			access = new Access();
			if (info.getHttpRequest() != null) {
				HttpServletRequest request = info.getHttpRequest();
				String validation = request.getRemoteAddr() + " " + RequestUtil.getHeader(HttpHeaders.USER_AGENT);
				access.setValidation(validation);
			}
			Long expiration = TimeUtil.getGMTCurrentTimeMillis() + (3600 * 24 * 1000); // 24 jam			
			access.setExpiration(expiration);
			access.setUser(null);
			access = accessRepository.save(access);
		} else {
			access = info.getAccess();
			if (access == null) {
				throw new ResponseRuntimeException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.02", true, "LBL.ACCESS")));
			}
			if (info.getHttpRequest() != null) {
				HttpServletRequest request = info.getHttpRequest();
				String validation = request.getRemoteAddr() + " " + RequestUtil.getHeader(request, HttpHeaders.USER_AGENT);
				if (!validation.equals(access.getValidation())) {
					throw new ResponseRuntimeException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.06", true, "LBL.ACCESS")));
				}
			}			
			if (access.hasExpired()) {
				cacheGroup.remove(AppConstant.CacheGroup.ACCESS, key);
				accessRepository.deleteById(key);
				throw new ResponseRuntimeException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.04", true, "LBL.ACCESS")));
			}
		}
		if (access != null) {
			AccessDto result = modelMapper.map(access, AccessDto.class);
			result.setExpiration(null);
			result.setValidation(null);
			result.setUser(null);
			return result;
		}
		return null;
	}

	@Override
	public AccessDto info() {
		AccessInfo info = AccessHolder.get();
		Access access = info.getAccess();
		if (access != null) {
			AccessDto result = modelMapper.map(access, AccessDto.class);
			result.setExpiration(null);
			result.setValidation(null);			
			return result;
		}
		return null;
	}

	@Login(false)
	@Override
	public AccessDto login(String username, String authcode) {
		if (username == null) {
			throw new ResponseRuntimeException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.01", "username").setField("username")));
		}
		if (authcode == null) {
			throw new ResponseRuntimeException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.01", "authcode").setField("authcode")));
		}
		AccessInfo info = AccessHolder.get();
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new ResponseRuntimeException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.02", "username").setField("username")));
		}
		String gencode = DigestUtil.digest("SHA-256", info.getKey() + user.getUsername() + user.getPassword());
		if (!gencode.equals(authcode)) {
			throw new ResponseRuntimeException(ResponseDto.ERROR(messageHelper.getCodeMessage("E.06", "password").setField("password")));
		}
		cacheGroup.remove(AppConstant.CacheGroup.ACCESS, info.getKey());
		Access access = accessRepository.findById(info.getKey()).get();
		access.setUser(user);
		access = accessRepository.save(access);
		AccessDto result = modelMapper.map(access, AccessDto.class);
		result.setExpiration(null);
		result.setValidation(null);
		return result;
	}

	@Override
	public AccessDto logout() {
		AccessInfo info = AccessHolder.get();
		cacheGroup.remove(AppConstant.CacheGroup.ACCESS, info.getKey());
		Access access = accessRepository.findById(info.getKey()).get();
		access.setUser(null);
		access = accessRepository.save(access);
		AccessDto result = modelMapper.map(access, AccessDto.class);
		result.setExpiration(null);
		result.setValidation(null);
		return result;
	}

}
