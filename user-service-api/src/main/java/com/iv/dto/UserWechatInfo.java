package com.iv.dto;

import com.iv.entity.dto.UserWechatEntityDto;

public class UserWechatInfo {
	private int eventId;
	private UserWechatEntityDto wechatEntity;
	
	
	public UserWechatInfo(int eventId, UserWechatEntityDto wechatEntity) {
		super();
		this.eventId = eventId;
		this.wechatEntity = wechatEntity;
	}
	
	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public UserWechatEntityDto getWechatEntity() {
		return wechatEntity;
	}
	public void setWechatEntity(UserWechatEntityDto wechatEntity) {
		this.wechatEntity = wechatEntity;
	}

	
}
