package com.ideahut.sbms.sample.api.support;

public abstract class AppConstant {
	
	public static class CacheGroup {
		public static final String ACCESS	= "ACCESS";
	}
	
	public static class Request {
		public static class Header {
			public static final String ACCESS_KEY	= "Access-Key";
		}
		public static class Parameter {
			public static final String LANGUAGE		= "_lang";
			public static final String FORMAT		= "_fmt";
		}
		public static class Attribute {
			public static final String ACCESS_KEY	= "ACCESS_KEY";
			public static final String LANGUAGE		= "__LANGUAGE__";
		}
	}
	
}
