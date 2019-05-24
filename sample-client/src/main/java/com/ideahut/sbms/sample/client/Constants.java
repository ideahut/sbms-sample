package com.ideahut.sbms.sample.client;

public abstract class Constants {
	
	public enum Currency {
		IDR, USD
	}

	public enum Gender {
		M, F
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
