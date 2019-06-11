package com.ideahut.sbms.sample.api.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ideahut.sbms.sample.api.interceptor.AccessHandlerInterceptor;
import com.ideahut.sbms.sample.client.Constants;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private AccessHandlerInterceptor accessHandlerInterceptor;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	private static final List<MediaType> DEFAULT_MEDIA_TYPE = Collections.singletonList(MediaType.APPLICATION_JSON);

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		
		configurer
		
		// parameter
		.parameterName(Constants.Request.Parameter.FORMAT)
		.favorParameter(false) // set true untuk baca format dari paramaeter (nilainya sesua dengan mediaType yg didaftarkan)
		
		// extension
		.favorPathExtension(false) // set true untuk baca format berdasarkan extension (nilainya sesua dengan mediaType yg didaftarkan)
		.useRegisteredExtensionsOnly(true)
		.ignoreUnknownPathExtensions(false)
		
		// header
		.ignoreAcceptHeader(false) // set false jika ingin membaca header Accept, contoh: application/json, application/xml		
		
		// default content type jika semua kondisi tidak terpenuhi
		.defaultContentType(DEFAULT_MEDIA_TYPE.get(0))
		
		
		// daftar media type yang disupport
		.mediaType("xml", MediaType.APPLICATION_XML)
		.mediaType("json", MediaType.APPLICATION_JSON)		
		
		// Antisipasi jika nama header Accept yang terbaca huruf kecil semua (Accept => accept)
		.defaultContentTypeStrategy(new ContentNegotiationStrategy() {
			@Override
			public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException {
				String[] headerValueArray = webRequest.getHeaderValues(HttpHeaders.ACCEPT);
				if (headerValueArray == null) {
					headerValueArray = webRequest.getHeaderValues(HttpHeaders.ACCEPT.toLowerCase()); // header yang kebaca huruf kecil semua??
					if (headerValueArray == null) {
						return DEFAULT_MEDIA_TYPE;
					}
					if (headerValueArray.length == 1 && "*/*".equals(headerValueArray[0])) {
						return DEFAULT_MEDIA_TYPE;
					}
				}
				List<String> headerValues = Arrays.asList(headerValueArray);
				try {
					List<MediaType> mediaTypes = MediaType.parseMediaTypes(headerValues);
					MediaType.sortBySpecificityAndQuality(mediaTypes);
					return !CollectionUtils.isEmpty(mediaTypes) ? mediaTypes : DEFAULT_MEDIA_TYPE;
				}
				catch (InvalidMediaTypeException ex) {
					throw new HttpMediaTypeNotAcceptableException("Could not parse 'Accept' header " + headerValues + ": " + ex.getMessage());
				}
			}
		});
		WebMvcConfigurer.super.configureContentNegotiation(configurer);
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
		
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);		
		converters.add(new MappingJackson2XmlHttpMessageConverter(xmlMapper));
		
		// Catatan: jika menggunakan @EnableWebMvc, maka jackson.default-property-inclusion=NON_NULL tidak berfungsi, sehingga perlu diconfigure manual converter yang dibutuhkan
		// Tambah converter yang lain??
		
		WebMvcConfigurer.super.configureMessageConverters(converters);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// add resource handler for swagger
		registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");	    
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(accessHandlerInterceptor);
		WebMvcConfigurer.super.addInterceptors(registry);
	}		
	
}
