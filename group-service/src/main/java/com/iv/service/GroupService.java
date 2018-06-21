package com.iv.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.dao.IGroupDaoImpl;
import com.iv.dto.GroupUserInfosDto;
import com.iv.dto.GroupUsersPageDto;
import com.iv.dto.UserIdsPagingDto;
import com.iv.dto.UserPagingDto;
import com.iv.enter.dto.GroupQuery;
import com.iv.enter.dto.OpsGroupDto;
import com.iv.enter.dto.UsersQueryDto;
import com.iv.entity.GroupEntity;
import com.iv.enumeration.LoginType;
import com.iv.external.service.TenantFacadeServiceClient;
import com.iv.external.service.UserServiceClient;
import com.iv.outer.dto.GroupEntityDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.tenant.api.dto.SubEnterpriseInfoDto;
import com.iv.tenant.api.dto.UserListDto;

@Service
public class GroupService {
	
	@Autowired
	private IGroupDaoImpl groupDao;
	@Autowired
	private UserServiceClient userService;
	@Autowired
	private TenantFacadeServiceClient tenantFacadeService;
	
	/*public GroupUserInfosDto test() {
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
	}*/
	
	/**
	 * 根据组id、租户id/或根据组id查询组信息
	 * @param groupQuery
	 * @return
	 */
	public GroupEntityDto selectGroupInfo(String subTenantId, short groupId) {
		GroupEntity groupEntity = null;
		groupEntity = groupDao.selectGroupInfoByTenantAndId(groupId,subTenantId);
		GroupEntityDto groupEntityDto = new GroupEntityDto();
		groupEntityDto.setGroupId(groupEntity.getGroupId());
		groupEntityDto.setGroupName(groupEntity.getGroupName());
		groupEntityDto.setUserId(groupEntity.getUserIds());
		return groupEntityDto;
	}
	
	/**
	 * 查询当前用户所在组信息（人员非分页）
	 * @param groupQuery
	 * @return
	 */
	public List<GroupUsersPageDto> groupUsersInfo(int userId) {
		LocalAuthDto localAuth = userService.selectLocalAuthById(userId);
		boolean f = false;
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
					, groupEntity.getGroupName(), groupEntity.getUserIds(), null);
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
	public GroupUsersPageDto convertGroupInfo(short groupId, String groupName, List<Integer> userIds, String tenantId) {
		GroupUsersPageDto groupUsersPageDto = new GroupUsersPageDto();
		groupUsersPageDto.setGroupId(groupId);
		groupUsersPageDto.setGroupName(groupName);
		if(!StringUtils.isEmpty(groupId) && !StringUtils.isEmpty(groupName) && userIds.size() > 0) {					
			UserPagingDto userPaging = convertUsersInfo(userIds,tenantId);		
			groupUsersPageDto.setUserPagingDto(userPaging);
		}		
		return groupUsersPageDto;
	}
	
	/**
	 * 将团队内用户id转为用户基本信息
	 */
	public UserPagingDto convertUsersInfo(List<Integer> userIds,String tenantId) {
		UserPagingDto userPagingDto = new UserPagingDto();
		UsersQueryDto usersWechatsQuery = new UsersQueryDto();
		usersWechatsQuery.setUserIds(userIds);
		usersWechatsQuery.setLoginType(LoginType.WECHAT);
		List<LocalAuthDto> UserInfosDtos = userService.selectUserInfos(usersWechatsQuery, tenantId);	
		if(null != UserInfosDtos && UserInfosDtos.size() > 0) {
			userPagingDto.setUsers(convertUserInfoDto(UserInfosDtos));
		}		
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
		boolean f = false;
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
			groupEntity.setGroupId((Short)objects2[0]);
			groupEntity.setGroupName((String)objects2[1]);
			groupentitys.add(groupEntity);
		}
		List<UserIdsPagingDto> userIdsPagingDto = groupDao.groupUserPageInfo((groupQuery.getCurPage() - 1) 
				* groupQuery.getItems(), groupQuery.getItems(), groupentitys);
		List<GroupUsersPageDto> groupUsers = new ArrayList<GroupUsersPageDto>();
		for (UserIdsPagingDto userIdsPaging : userIdsPagingDto) {	
			GroupUsersPageDto groupUsersPageDto = convertGroupInfo(userIdsPaging.getGroupId()
					, userIdsPaging.getGroupName(), userIdsPaging.getUserIds(), localAuth.getCurTenantId());
			if(userIdsPaging.getTotalCount() != 0) {
				groupUsersPageDto.getUserPagingDto().setTotalCount(userIdsPaging.getTotalCount());
			}			
			groupUsers.add(groupUsersPageDto);			
		}
		return groupUsers;
	}
	
	/**
	 * 组操作
	 * @param groupDto
	 * @return
	 *//*
	public Object groupOps(GroupQuery groupQuery) {
		GroupUsersPageDto groupUsersPageDto = null;
		switch (groupQuery.getOps().ordinal()) {		
		// 新建组
		case 0:
			GroupEntity groupCreate = new GroupEntity();
			groupCreate.setGroupName(groupQuery.getGroupName());
			groupCreate.setUserIds(groupQuery.getUserIds());
			groupDao.saveOrUpdateGroup(groupCreate);
			groupUsersPageDto = convertGroupInfo(groupCreate.getGroupId(), 
					groupCreate.getGroupName(), groupCreate.getUserIds());
			groupUsersPageDto.getUserPagingDto().setTotalCount(groupQuery.getUserIds().size());
			return groupUsersPageDto;
		// 删除组
		case 1:
			groupDao.deleteGroupById(groupQuery.getGroupId());
			break;
		// 新增成员
		case 2:
			GroupEntity groupMemIn = groupDao.selectGroupById(groupQuery.getGroupId());
			groupMemIn.getUserIds().addAll(groupQuery.getUserIds());								
			groupDao.saveOrUpdateGroup(groupMemIn);
			groupUsersPageDto = convertGroupInfo(groupMemIn.getGroupId(), 
					groupMemIn.getGroupName(), groupMemIn.getUserIds());
			groupUsersPageDto.getUserPagingDto().setTotalCount(groupMemIn.getUserIds().size());
			return groupUsersPageDto;
		// 删除成员
		case 3:
			GroupEntity groupMemOut = groupDao.selectGroupById(groupQuery.getGroupId());			
			groupMemOut.getUserIds().removeAll(groupQuery.getUserIds());			
			groupDao.saveOrUpdateGroup(groupMemOut);
			groupUsersPageDto = convertGroupInfo(groupMemOut.getGroupId(), 
					groupMemOut.getGroupName(), groupMemOut.getUserIds());
			groupUsersPageDto.getUserPagingDto().setTotalCount(groupMemOut.getUserIds().size());
			return groupUsersPageDto;
		// 修改组名
		case 4:
			GroupEntity groupMod = groupDao.updateGroupName(groupQuery.getGroupId(), groupQuery.getGroupName());
			groupUsersPageDto = convertGroupInfo(groupMod.getGroupId(), 
					groupMod.getGroupName(), groupMod.getUserIds());
			groupUsersPageDto.getUserPagingDto().setTotalCount(groupMod.getUserIds().size());
			return groupUsersPageDto;
		default:
			break;
		}
		return null;
	}
	*/
	/**
	 * 获取当前项目组下所有人员id列表
	 * @param request
	 * @return
	 */
	public List<LocalAuthDto> selectUsersInfoByTenantId(HttpServletRequest request){
		SubEnterpriseInfoDto subEnterpriseInfo = tenantFacadeService.getCurrentSubEnterpriseBack(request);	
		if(null != subEnterpriseInfo && subEnterpriseInfo.getUserIds().size() > 0) {			
			List<Integer> userIds = new ArrayList<Integer>(subEnterpriseInfo.getUserIds());
			UsersQueryDto usersQueryDto = new UsersQueryDto(userIds);
			return userService.selectUserInfos(usersQueryDto, subEnterpriseInfo.getTenantId());
		}
		return null;
	}
	
	/**
	 * 查询所有组信息（人员非分页）
	 * @param groupQuery
	 * @return
	 */
	public List<GroupUsersPageDto> groupsUsersInfo() {		
		List<GroupEntity> groups = new ArrayList<GroupEntity>();		
		groups = groupDao.selectGroupAll();		
		List<GroupUsersPageDto> groupUsersDtos = new ArrayList<GroupUsersPageDto>();
		//将团队中人员转为分页格式的人员封装
		for (GroupEntity groupEntity : groups) {
			GroupUsersPageDto groupUsersPageDto = convertGroupInfo(groupEntity.getGroupId()
					, groupEntity.getGroupName(), groupEntity.getUserIds(), null);
			groupUsersPageDto.getUserPagingDto().setTotalCount(groupEntity.getUserIds().size());
			groupUsersDtos.add(groupUsersPageDto);
		}
		return groupUsersDtos;
	}
	
	/**
	 * 根据组Ids获取组信息（组名称、组id）
	 * @return
	 */
	public List<GroupEntityDto> groupsInfo(List<Short> groupIds) {
		List<GroupEntity> groups = groupDao.groupsInfo(groupIds);
		List<GroupEntityDto> groupDtos= new ArrayList<GroupEntityDto>();
		if(null != groups && groups.size() > 0) {
			for (GroupEntity groupEntity : groups) {
				groupDtos.add(convertGroupEntity(groupEntity));
			}
		}
		return groupDtos;
	}
	
	/**
	 * 将GroupEntity信息转换为GroupEntityDto
	 * @return
	 */
	public GroupEntityDto convertGroupEntity(GroupEntity groupEntity) {
		GroupEntityDto groupEntityDto = new GroupEntityDto();
		BeanCopier copy=BeanCopier.create(GroupEntity.class, GroupEntityDto.class, false);
		copy.copy(groupEntity, groupEntityDto, null);
		return groupEntityDto;
	}	
	
	/**
	 * 创建组
	 * @param opsGroupDto
	 * @return
	 */
	public GroupUsersPageDto createGroup(OpsGroupDto opsGroupDto, HttpServletRequest request) {
		int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));		
		GroupUsersPageDto groupUsersPageDto = null;
		GroupEntity groupCreate = new GroupEntity();
		groupCreate.setGroupName(opsGroupDto.getGroupName());
		List<Integer> userIds = opsGroupDto.getUserIds();
		userIds.add(userId);
		groupCreate.setUserIds(listDuplicateRemoval(userIds));
		groupDao.saveOrUpdateGroup(groupCreate);
		groupUsersPageDto = convertGroupInfo(groupCreate.getGroupId(), 
				groupCreate.getGroupName(), groupCreate.getUserIds(), null);
		groupUsersPageDto.getUserPagingDto().setTotalCount(opsGroupDto.getUserIds().size());
		return groupUsersPageDto;
		
	}
	
	/**
	 * 对用户ids进行去重
	 * @param userIds
	 * @return
	 */
	public List<Integer> listDuplicateRemoval(List<Integer> userIds){
		Set<Integer> set = new HashSet<Integer>();
		List<Integer> userIdList = new ArrayList<Integer>();
		for (Integer userId : userIds) {
			if(set.add(userId)) {
				userIdList.add(userId);
			}
		}
		return userIdList;
	}
	
	
	/**
	 * 删除组
	 * @param groupId
	 * @return
	 */
	public void deleteGroup(short groupId) {
		groupDao.deleteGroupById(groupId);		
	}
	
	/**
	 * 增加组成员
	 * @param opsGroupDto
	 * @return
	 */
	public GroupUsersPageDto memberInGroup(OpsGroupDto opsGroupDto) {
		GroupUsersPageDto groupUsersPageDto = null;
		GroupEntity groupMemIn = groupDao.selectGroupById(opsGroupDto.getGroupId());
		if(opsGroupDto.getUserIds().size() > 0) {
			//不添加已存在的用户id
			for (Integer userId : opsGroupDto.getUserIds()) {
				if(!groupMemIn.getUserIds().contains(userId)) {
					groupMemIn.getUserIds().add(userId);
				}
			}									
			groupDao.saveOrUpdateGroup(groupMemIn);
		}		
		groupUsersPageDto = convertGroupInfo(groupMemIn.getGroupId(), 
				groupMemIn.getGroupName(), groupMemIn.getUserIds(), null);
		groupUsersPageDto.getUserPagingDto().setTotalCount(groupMemIn.getUserIds().size());
		return groupUsersPageDto;
		
	}
	
	/**
	 * 删除组成员
	 * @param opsGroupDto
	 * @return
	 */
	public GroupUsersPageDto memberOutGroup(OpsGroupDto opsGroupDto) {
		GroupUsersPageDto groupUsersPageDto = null;
		GroupEntity groupMemOut = groupDao.selectGroupById(opsGroupDto.getGroupId());			
		groupMemOut.getUserIds().removeAll(opsGroupDto.getUserIds());			
		groupDao.saveOrUpdateGroup(groupMemOut);
		groupUsersPageDto = convertGroupInfo(groupMemOut.getGroupId(), 
				groupMemOut.getGroupName(), groupMemOut.getUserIds(),null);
		if(groupMemOut.getUserIds().size() > 0) {
			groupUsersPageDto.getUserPagingDto().setTotalCount(groupMemOut.getUserIds().size());
		}		
		return groupUsersPageDto;
	}
	
	/**
	 * 修改组名称
	 * @param groupId
	 * @param groupName
	 * @return
	 */
	public GroupUsersPageDto groupNameMod(short groupId, String groupName) {
		GroupUsersPageDto groupUsersPageDto = null;
		GroupEntity groupMod = groupDao.updateGroupName(groupId, groupName);
		groupUsersPageDto = convertGroupInfo(groupMod.getGroupId(), 
				groupMod.getGroupName(), groupMod.getUserIds(), null);
		groupUsersPageDto.getUserPagingDto().setTotalCount(groupMod.getUserIds().size());
		return groupUsersPageDto;
	}
	
	/**
	 * 租户下人员分页查询
	 * @param request
	 * @param groupQuery
	 * @return
	 */
	public UserPagingDto selectTenantUserPageInfo(HttpServletRequest request, GroupQuery groupQuery){
		int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
		LocalAuthDto localAuth = userService.selectLocalAuthById(userId);
		UserPagingDto userPagingDto = new UserPagingDto();
		UserListDto userListDto = tenantFacadeService.getCurrentSubEnterpriseUserList(request,groupQuery.getCurPage(),groupQuery.getItems());	
		if(null != userListDto && userListDto.getUserIds().size() > 0) {			
			List<Integer> userIdsList = new ArrayList<Integer>(userListDto.getUserIds());
			UsersQueryDto usersQueryDto = new UsersQueryDto(userIdsList);
			List<LocalAuthDto> UserInfosDtos = userService.selectUserInfos(usersQueryDto, localAuth.getCurTenantId());
			if(null != UserInfosDtos && UserInfosDtos.size() > 0) {
				userPagingDto.setUsers(convertUserInfoDto(UserInfosDtos));
				userPagingDto.setTotalCount(userListDto.getTotal());
			}
			return userPagingDto;
		}
			
			
		return null;
	}
	
	/**
	 * 查询组内人员id集合
	 * @param subTenantId
	 * @param groupId
	 * @return
	 */
	public List<Integer> selectGroupUserIds(String subTenantId, short groupId){
		return groupDao.selectGroupUserIds(subTenantId, groupId);
	}
	
}
