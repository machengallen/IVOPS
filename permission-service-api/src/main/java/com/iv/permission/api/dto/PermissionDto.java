package com.iv.permission.api.dto;

import com.iv.common.enumeration.YesOrNo;

public class PermissionDto {
	/*权限编码，供前端使用，唯一*/
	private String code;	
	/*权限描述*/
	private String description;
	/*权限url*/
	private String url;
	/*是否有效*/
	private YesOrNo isValid;	
	/*服务类别*/
	private String serviceType;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	                                                                                                                                                                                         
	public YesOrNo getIsValid() {
		return isValid;
	}
	public void setIsValid(YesOrNo isValid) {
		this.isValid = isValid;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	
}
