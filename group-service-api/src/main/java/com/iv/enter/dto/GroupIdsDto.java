package com.iv.enter.dto;

import java.util.List;

/**
 * 团队id列表入参
 * @author zhangying
 *
 */
public class GroupIdsDto {
	List<Short> groupIds;

	public List<Short> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Short> groupIds) {
		this.groupIds = groupIds;
	}

	public GroupIdsDto(List<Short> groupIds) {
		super();
		this.groupIds = groupIds;
	}

	public GroupIdsDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
