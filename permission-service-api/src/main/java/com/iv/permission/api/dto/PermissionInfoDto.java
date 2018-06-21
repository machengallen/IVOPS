package com.iv.permission.api.dto;

import com.iv.common.enumeration.ServiceType;
import com.iv.common.enumeration.YesOrNo;

public class PermissionInfoDto {
	private String code;
	/*权限名称*/
	private String name;
	/*权限描述*/
	private String description;
	/*权限url*/
	private String url;
	/*权限类型(告警、团队、微信等)*/
	private ServiceType serviceType;	
	/*是否需要订阅*/
	private YesOrNo needSubscribe;
	/*是否发布*/
	private YesOrNo releaseOrNot;
	/*是否有效*/
	private YesOrNo isValid;
	/*是否已经订阅*/
	private YesOrNo isSubscribed;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public ServiceType getServiceType() {
		return serviceType;
	}
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
	public YesOrNo getNeedSubscribe() {
		return needSubscribe;
	}
	public void setNeedSubscribe(YesOrNo needSubscribe) {
		this.needSubscribe = needSubscribe;
	}
	public YesOrNo getReleaseOrNot() {
		return releaseOrNot;
	}
	public void setReleaseOrNot(YesOrNo releaseOrNot) {
		this.releaseOrNot = releaseOrNot;
	}
	public YesOrNo getIsValid() {
		return isValid;
	}
	public void setIsValid(YesOrNo isValid) {
		this.isValid = isValid;
	}
	public YesOrNo getIsSubscribed() {
		return isSubscribed;
	}
	public void setIsSubscribed(YesOrNo isSubscribed) {
		this.isSubscribed = isSubscribed;
	}
	
}
