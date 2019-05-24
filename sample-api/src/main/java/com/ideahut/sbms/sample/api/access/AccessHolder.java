package com.ideahut.sbms.sample.api.access;

import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

public abstract class AccessHolder {

	private static final ThreadLocal<AccessInfo> holder = 
		new NamedThreadLocal<AccessInfo>("Access Info ThreadLocal");

	private static final ThreadLocal<AccessInfo> inheritableHolder = 
		new NamedInheritableThreadLocal<AccessInfo>("Access Info InheritableThreadLocal");
	
	public static void remove() {
		holder.remove();
		inheritableHolder.remove();
	}
	
	public static void set(AccessInfo info, boolean inheritable) {
		if (info == null) {
			remove();
		} else {
			if (inheritable) {
				inheritableHolder.set(info);
				holder.remove();
			} else {
				holder.set(info);
				inheritableHolder.remove();
			}
		}
	}
	
	public static void set(AccessInfo info) {
		set(info, false);
	}
	
	public static AccessInfo get() {
		AccessInfo info = holder.get();
		if (info == null) {
			info = inheritableHolder.get();
		}
		return info;
	}	
	
}
