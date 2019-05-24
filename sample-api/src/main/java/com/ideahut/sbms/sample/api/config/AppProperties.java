package com.ideahut.sbms.sample.api.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
	
	private Cache cache = new Cache();
	
	private Task task = new Task();
	
	private Language language = new Language();
	
	private List<Class<?>> remoteMethodServices = new ArrayList<Class<?>>();
	
	private List<Class<?>> ignoredHandlerClasses = new ArrayList<Class<?>>();
	
	
	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public List<Class<?>> getRemoteMethodServices() {
		return remoteMethodServices;
	}

	public void setRemoteMethodServices(List<Class<?>> remoteMethodServices) {
		this.remoteMethodServices = remoteMethodServices;
	}

	public List<Class<?>> getIgnoredHandlerClasses() {
		return ignoredHandlerClasses;
	}

	public void setIgnoredHandlerClasses(List<Class<?>> ignoredHandlerClasses) {
		this.ignoredHandlerClasses = ignoredHandlerClasses;
	}









	public static class Task {
		private Executor executor = new Executor();		
		public Executor getExecutor() {
			return executor;
		}
		public void setExecutor(Executor executor) {
			this.executor = executor;
		}

		public static class Executor {
			private int corePoolSize;
		    private int maxPoolSize;
		    private String threadNamePrefix;
			public int getCorePoolSize() {
				return corePoolSize;
			}
			public void setCorePoolSize(int corePoolSize) {
				this.corePoolSize = corePoolSize;
			}
			public int getMaxPoolSize() {
				return maxPoolSize;
			}
			public void setMaxPoolSize(int maxPoolSize) {
				this.maxPoolSize = maxPoolSize;
			}
			public String getThreadNamePrefix() {
				return threadNamePrefix;
			}
			public void setThreadNamePrefix(String threadNamePrefix) {
				this.threadNamePrefix = threadNamePrefix;
			}
		}
	}
	
	public static class Language {
		private List<Locale> supported;
		private Locale primary;
		public List<Locale> getSupported() {
			return supported;
		}
		public void setSupported(List<Locale> supported) {
			this.supported = supported;
		}
		public Locale getPrimary() {
			return primary;
		}
		public void setPrimary(Locale primary) {
			this.primary = primary;
		}
	}
	
	public static class Cache {
		private Map<String, String> configuration = new HashMap<String, String>();
		private List<Group> groups = new ArrayList<Group>();
		public Map<String, String> getConfiguration() {
			return configuration;
		}
		public void setConfiguration(Map<String, String> configuration) {
			this.configuration = configuration;
		}
		public List<Group> getGroups() {
			return groups;
		}
		public void setGroups(List<Group> groups) {
			this.groups = groups;
		}

		public static class Group {
			private String name;
			private int limit;
			private long age;
			private boolean nullable;
			private String collector;
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public int getLimit() {
				return limit;
			}
			public void setLimit(int limit) {
				this.limit = limit;
			}
			public long getAge() {
				return age;
			}
			public void setAge(long age) {
				this.age = age;
			}
			public boolean isNullable() {
				return nullable;
			}
			public void setNullable(boolean nullable) {
				this.nullable = nullable;
			}
			public String getCollector() {
				return collector;
			}
			public void setCollector(String collector) {
				this.collector = collector;
			}
		}
	}
}
