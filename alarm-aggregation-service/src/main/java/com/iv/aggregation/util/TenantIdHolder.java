package com.iv.aggregation.util;

import org.springframework.core.NamedThreadLocal;

/**
 * 租户标识符本地线程变量
 * @author macheng
 * 2018年6月26日
 * 
 */
public class TenantIdHolder {

	private static final ThreadLocal<String> tenantIdHolder =
			new NamedThreadLocal<String>("user tenant id");
	
	public static void set(String tenantId) {
		tenantIdHolder.set(tenantId);
	}
	
	public static String get() {
		return tenantIdHolder.get();
	}
}
