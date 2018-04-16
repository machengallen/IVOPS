package com.iv.outer.dto;
import java.util.List;

import com.iv.dto.UserInfosDto;

/**
 * 团队信息（包括用户基本信息与微信头像）
 * @author zhangying
 * 2018年4月16日
 * aggregation-1.4.0-SNAPSHOT
 */
public class GroupUsersEntityDto {

	private short groupId;
	private String groupName;	
	private List<UserInfosDto> userInfos;
	public short getGroupId() {
		return groupId;
	}
	public void setGroupId(short groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<UserInfosDto> getUserInfos() {
		return userInfos;
	}
	public void setUserInfos(List<UserInfosDto> userInfos) {
		this.userInfos = userInfos;
	}
	

	
}
