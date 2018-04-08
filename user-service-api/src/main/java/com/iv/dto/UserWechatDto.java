package com.iv.dto;

import java.util.List;

import com.iv.entity.dto.UserWechatEntityDto;


public class UserWechatDto {
	private List<UserWechatEntityDto> userWechatEntities;

	public List<UserWechatEntityDto> getUserWechatEntities() {
		return userWechatEntities;
	}

	public void setUserWechatEntities(List<UserWechatEntityDto> userWechatEntities) {
		this.userWechatEntities = userWechatEntities;
	}
	
}
