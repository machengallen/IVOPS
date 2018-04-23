package com.iv.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iv.dao.IGroupDaoImpl;
import com.iv.dto.GroupUserInfosDto;
import com.iv.dto.UserIdsPagingDto;
import com.iv.dto.UserPagingDto;
import com.iv.enter.dto.GroupQuery;
import com.iv.enter.dto.UsersQueryDto;
import com.iv.entity.GroupEntity;
import com.iv.enumeration.LoginType;
import com.iv.external.service.UserServiceClient;
import com.iv.outer.dto.GroupEntityDto;
import com.iv.outer.dto.GroupUsersPageDto;
import com.iv.outer.dto.LocalAuthDto;

@Service
public class GroupService {
	
	@Autowired
	private IGroupDaoImpl groupDao;
	@Autowired
	private UserServiceClient userService;
	
	public GroupUserInfosDto test() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String token = request.getHeader("cook");
		GroupUserInfosDto groupUserInfosDto = new GroupUserInfosDto();		
		LocalAuthDto dto = userService.getUserInfo();
		if(null != dto) {
			groupUserInfosDto.setId(dto.getId());
			groupUserInfosDto.setUserName(dto.getUserName());
		}
		
		return groupUserInfosDto;
	}
	
	/**
	 * 根据组id、租户id/或根据组id查询组信息
	 * @param groupQuery
	 * @return
	 */
	public GroupEntityDto selectGroupInfo(GroupQuery groupQuery) {
		GroupEntity groupEntity = null;
		if(StringUtils.isEmpty(groupQuery.getTenantId())) {
			groupEntity = groupDao.selectGroupInfoById(groupQuery.getGroupId());
		}else {
			groupEntity = groupDao.selectGroupInfoByTenantAndId(groupQuery.getGroupId(),groupQuery.getTenantId());
		}
		GroupEntityDto groupEntityDto = new GroupEntityDto();
		BeanCopier copy=BeanCopier.create(GroupEntity.class, GroupEntityDto.class, false);
		copy.copy(groupEntity, groupEntityDto, null);
		return groupEntityDto;
	}
	
	/**
	 * 查询当前用户所在组信息（人员非分页）
	 * @param groupQuery
	 * @return
	 */
	public List<GroupUsersPageDto> groupUsersInfo(GroupQuery groupQuery, int userId) {
		LocalAuthDto localAuth = userService.selectLocalAuthById(userId);
		boolean f = true;
		List<GroupEntity> groups = new ArrayList<GroupEntity>();
		if(f) {
			//是管理员，查询所有团队
			groups = groupDao.selectGroupAll();
		}else {
			//非管理员，查询用户所在团队
			groups = groupDao.selectGroupsByUserId(userId);
		}
		List<GroupUsersPageDto> groupUsersDtos = new ArrayList<GroupUsersPageDto>();
		//将团队中人员转为分页格式的人员封装
		for (GroupEntity groupEntity : groups) {
			GroupUsersPageDto groupUsersPageDto = convertGroupInfo(groupEntity.getGroupId()
					, groupEntity.getGroupName(), groupEntity.getUserIds());
			groupUsersPageDto.getUserPagingDto().setTotalCount(groupEntity.getUserIds().size());
			groupUsersDtos.add(groupUsersPageDto);
		}
		return groupUsersDtos;
	}
	
	/**
	 * 将团队信息封装
	 * @param groupId
	 * @param groupName
	 * @param userIds
	 */
	public GroupUsersPageDto convertGroupInfo(short groupId, String groupName, List<Integer> userIds) {
		GroupUsersPageDto groupUsersPageDto = new GroupUsersPageDto();
		groupUsersPageDto.setGroupId(groupId);
		groupUsersPageDto.setGroupName(groupName);		
		UserPagingDto userPaging = convertUsersInfo(userIds);		
		groupUsersPageDto.setUserPagingDto(userPaging);
		return groupUsersPageDto;
	}
	
	/**
	 * 将团队内用户id转为用户基本信息
	 */
	public UserPagingDto convertUsersInfo(List<Integer> userIds) {
		UserPagingDto userPagingDto = new UserPagingDto();
		UsersQueryDto usersWechatsQuery = new UsersQueryDto();
		usersWechatsQuery.setUserIds(userIds);
		usersWechatsQuery.setLoginType(LoginType.WECHAT.toString());
		List<LocalAuthDto> UserInfosDtos = userService.selectUserInfos(usersWechatsQuery);			
		userPagingDto.setUsers(convertUserInfoDto(UserInfosDtos));
		return userPagingDto;
	}
	
	/**
	 * 将用户服务的用户信息转为团队服务的用户信息
	 * @param userInfosDtos
	 */
	public List<GroupUserInfosDto> convertUserInfoDto(List<LocalAuthDto> userInfosDtos) {
		List<GroupUserInfosDto> groupUserInfosDtos = new ArrayList<GroupUserInfosDto>();
		for (LocalAuthDto localAuthDto : userInfosDtos) {
			GroupUserInfosDto groupUserInfosDto = new GroupUserInfosDto();
			BeanCopier copy=BeanCopier.create(LocalAuthDto.class, GroupUserInfosDto.class, false);
			copy.copy(localAuthDto, groupUserInfosDto, null);
			groupUserInfosDtos.add(groupUserInfosDto);
		}
		return groupUserInfosDtos;
	}
	
	/**
	 * 查询当前用户所在组信息（人员分页）
	 * @param groupQuery
	 * @return
	 */
	public List<GroupUsersPageDto> groupUsersPageInfo(GroupQuery groupQuery, int userId) {
		LocalAuthDto localAuth = userService.selectLocalAuthById(userId);
		boolean f = true;
		List<GroupEntityDto> groupentitys = new ArrayList<GroupEntityDto>();
		List<Object[]> objects = new ArrayList<Object[]>();
		if(f) {
			//是管理员，查询所有团队
			objects = groupDao.selectGroupAllIdAndName();			
		}else {
			//非管理员，查询用户所在团队
			objects = groupDao.selectGroupIdAndNameByUserId(userId);
		}
		for (Object[] objects2 : objects) {
			GroupEntityDto groupEntity = new GroupEntityDto();
			groupEntity.setGroupId((short)objects2[0]);
			groupEntity.setGroupName((String)objects2[0]);
			groupentitys.add(groupEntity);
		}
		List<UserIdsPagingDto> userIdsPagingDto = groupDao.groupUserPageInfo((groupQuery.getCurPage() - 1) 
				* groupQuery.getItems(), groupQuery.getItems(), groupentitys);
		List<GroupUsersPageDto> groupUsers = new ArrayList<GroupUsersPageDto>();
		for (UserIdsPagingDto userIdsPaging : userIdsPagingDto) {	
			GroupUsersPageDto groupUsersPageDto = convertGroupInfo(userIdsPaging.getGroupId()
					, userIdsPaging.getGroupName(), userIdsPaging.getUserIds());
			groupUsersPageDto.getUserPagingDto().setTotalCount(userIdsPaging.getTotalCount());
			groupUsers.add(groupUsersPageDto);			
		}
		return groupUsers;
	}
	
	/**
	 * 组操作
	 * @param groupDto
	 * @return
	 */
	public Object groupOps(GroupQuery groupQuery) {
		switch (groupQuery.getOps().ordinal()) {
		// 新建组
		case 0:
			GroupEntity groupCreate = new GroupEntity();
			groupCreate.setGroupName(groupQuery.getGroupName());
			groupCreate.setUserIds(groupQuery.getUserIds());
			groupDao.saveOrUpdateGroup(groupCreate);
			return convertGroupInfo(groupCreate.getGroupId(), 
					groupCreate.getGroupName(), groupCreate.getUserIds());
		// 删除组
		case 1:
			groupDao.deleteGroupById(groupQuery.getGroupId());
			break;
		// 新增成员
		case 2:
			GroupEntity groupMemIn = groupDao.selectGroupById(groupQuery.getGroupId());
			groupMemIn.getUserIds().addAll(groupQuery.getUserIds());								
			groupDao.saveOrUpdateGroup(groupMemIn);
			return convertGroupInfo(groupMemIn.getGroupId(), 
					groupMemIn.getGroupName(), groupMemIn.getUserIds());
		// 删除成员
		case 3:
			GroupEntity groupMemOut = groupDao.selectGroupById(groupQuery.getGroupId());			
			groupMemOut.getUserIds().removeAll(groupQuery.getUserIds());			
			groupDao.saveOrUpdateGroup(groupMemOut);
			return convertGroupInfo(groupMemOut.getGroupId(), 
					groupMemOut.getGroupName(), groupMemOut.getUserIds());
		// 修改组名
		case 4:
			GroupEntity groupMod = groupDao.updateGroupName(groupQuery.getGroupId(), groupQuery.getGroupName());
			return convertGroupInfo(groupMod.getGroupId(), 
					groupMod.getGroupName(), groupMod.getUserIds());
		default:
			break;
		}
		return null;
	}
		
}
