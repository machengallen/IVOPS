package com.iv.entity.dto;

public class PlatformSignDto {

	private String id;
	/**项目在平台下的appId*/
	private String appId;
	/**用户在该平台下的唯一标志 */
	private String openId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	
}
