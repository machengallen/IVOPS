package com.iv.outer.dto;
import com.iv.dto.UserPagingDto;

public class GroupUsersPageDto {
	private short groupId;
	private String groupName;	
	private UserPagingDto userPagingDto;
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
	public UserPagingDto getUserPagingDto() {
		return userPagingDto;
	}
	public void setUserPagingDto(UserPagingDto userPagingDto) {
		this.userPagingDto = userPagingDto;
	}
	
}
