package com.ideahut.sbms.sample.view.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
	
	private Task task = new Task();
	
	private Language language = new Language();
	
	private Initial initial = new Initial();
	
	private List<Class<?>> ignoredHandlerClasses = new ArrayList<Class<?>>();
	
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

	public Initial getInitial() {
		return initial;
	}

	public void setInitial(Initial initial) {
		this.initial = initial;
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
	
	public static class Initial {
		private String page;
		private String theme;
		public String getPage() {
			return page;
		}
		public void setPage(String page) {
			this.page = page;
		}
		public String getTheme() {
			return theme;
		}
		public void setTheme(String theme) {
			this.theme = theme;
		}		
	}
	
}
