package com.iv.permission.api.dto;
import java.util.List;

import com.iv.common.enumeration.YesOrNo;
public class FunctionInfoDto {
	private int id;	
	private String name;
	/*权限code集合*/
	private List<String> permissionIds;
	/*服务类别*/
	private ServiceType serviceType;
	/*是否需要订阅*/
	private YesOrNo needSubscribe;
	/*是否发布*/
	private YesOrNo releaseOrNot;
	/*是否有效*/
	private YesOrNo isValid;
	/*是否已订阅:项目组权限展示时使用*/
	private YesOrNo isSubscribed;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getPermissionIds() {
		return permissionIds;
	}
	public void setPermissionIds(List<String> permissionIds) {
		this.permissionIds = permissionIds;
	}
	public ServiceType getServiceType() {
		return serviceType;
	}
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
	
	public YesOrNo getIsSubscribed() {
		return isSubscribed;
	}
	public void setIsSubscribed(YesOrNo isSubscribed) {
		this.isSubscribed = isSubscribed;
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
	
}
