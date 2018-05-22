package com.iv.permission.api.dto;

import com.iv.common.enumeration.YesOrNo;

public class PermissionDto {
	/*权限编码，供前端使用，唯一*/
	private String code;	
	/*权限描述*/
	private String description;
	/*权限url*/
	private String url;
	/*是否需要订阅*/
	private YesOrNo needSubscribe;
	/*是否发布*/
	private YesOrNo releaseOrNot;
	/*是否有效*/
	private YesOrNo isValid;
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
