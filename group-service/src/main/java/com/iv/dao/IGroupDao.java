package com.iv.dao;

import java.util.List;

import com.iv.dto.UserIdsPagingDto;
import com.iv.entity.GroupEntity;
import com.iv.outer.dto.GroupEntityDto;

/**
 * 组相关接口
 * @author zhangying
 * 2018年4月13日
 * aggregation-1.4.0-SNAPSHOT
 */
public interface IGroupDao {
	
	GroupEntity selectGroupInfoById(short id) throws RuntimeException;
	
	GroupEntity selectGroupInfoByTenantAndId(short id, String tenantId) throws RuntimeException;
	
	List<GroupEntity> selectGroupAll() throws RuntimeException;
	
	List<GroupEntity> selectGroupsByUserId(int userId)  throws RuntimeException;
	
	List<Object[]> selectGroupAllIdAndName() throws RuntimeException;
	
	List<Object[]> selectGroupIdAndNameByUserId(int userId) throws RuntimeException;
	
	List<UserIdsPagingDto> groupUserPageInfo(int curItems,int item,List<GroupEntityDto> GroupEntitys) throws RuntimeException;
	
	void saveOrUpdateGroup(GroupEntity groupEntity) throws RuntimeException;
	
	GroupEntity selectGroupById(short id) throws RuntimeException;
	
	void deleteGroupById(short id) throws RuntimeException;
	
	GroupEntity updateGroupName(final short id, String groupName) throws RuntimeException;
}
