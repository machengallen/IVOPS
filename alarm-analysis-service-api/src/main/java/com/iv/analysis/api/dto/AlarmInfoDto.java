package com.iv.analysis.api.dto;

import java.io.Serializable;

/**
 * 告警信息体
 * @author macheng
 * 2018年4月8日
 * alarm-analysis-service-1.0.0-SNAPSHOT
 * 
 */
public class AlarmInfoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7819594934232395255L;
	private String id;
	private String content;
	private String tenantId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
