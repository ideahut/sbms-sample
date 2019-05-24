package com.ideahut.sbms.sample.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ideahut.sbms.common.cache.CacheGroup;
import com.github.ideahut.sbms.common.cache.CacheValueColletor;
import com.github.ideahut.sbms.common.cache.LocalCacheGroup;
import com.ideahut.sbms.sample.api.entity.Access;
import com.ideahut.sbms.sample.api.repository.AccessRepository;

@Configuration
public class CacheConfig {
	
	@Autowired
	private AppProperties appProperties;
	
	@Autowired
	private AccessRepository accessRepository;
	
	
	@Bean
	public CacheGroup cacheGroup() {
		/* 
		 * Field configuration bisa digunakan untuk cache group yg lain seperti redis, utk mensetting host dan port.
		 * value collector didefinisikan di class ini, dan di properties diidentifikasi dengan nama method 
		 */
		LocalCacheGroup cacheGroup = new LocalCacheGroup();
		for (AppProperties.Cache.Group group : appProperties.getCache().getGroups()) {
			cacheGroup.register(
				group.getName(), 
				group.getLimit(), 
				group.getAge(), 
				group.isNullable(),
				build(group.getCollector())
			);
		}
		return cacheGroup;
	}
	
	@SuppressWarnings("unchecked")
	private<KEY, VALUE> CacheValueColletor<KEY, VALUE> build(String methodName) {
		methodName = methodName != null ? methodName.trim() : "";
		if (methodName.isEmpty()) {
			return null;
		}
		try {
			return (CacheValueColletor<KEY, VALUE>) getClass().getMethod(methodName).invoke(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	/*
	 * DAFTAR COLLECTOR
	 */
	
	public CacheValueColletor<String, Access> accessCollector() {
		return new CacheValueColletor<String, Access>() {
			@Override
			public Access collect(String key, Object... args) throws Exception {
				return accessRepository.findById(key).orElse(null);
			}
		};
	}
	
}
