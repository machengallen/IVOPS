package com.iv.permission.service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iv.common.enumeration.ServiceType;
import com.iv.common.enumeration.YesOrNo;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.enter.dto.UsersQueryDto;
import com.iv.enumeration.LoginType;
import com.iv.outer.dto.GroupEntityDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.FunctionInfoDto;
import com.iv.permission.api.dto.IdsDto;
import com.iv.permission.api.dto.PermissionDto;
import com.iv.permission.api.dto.SubTenantRoleDto;
import com.iv.permission.api.dto.SubTenantUserRoleDto;
import com.iv.permission.dao.impl.FunctionInfoDaoImpl;
import com.iv.permission.dao.impl.PermissionDaoImpl;
import com.iv.permission.dao.impl.SubTenantPermissionDaoImpl;
import com.iv.permission.dao.impl.SubTenantRoleDaoImpl;
import com.iv.permission.dao.impl.SubTenantUserRoleDaoImpl;
import com.iv.permission.dto.SubTenantRoleFunctionDto;
import com.iv.permission.entity.FunctionInfo;
import com.iv.permission.entity.PermissionInfo;
import com.iv.permission.entity.SubTenantPermission;
import com.iv.permission.entity.SubTenantRole;
import com.iv.permission.entity.SubTenantUserRole;
import com.iv.permission.feign.client.GroupServiceClient;
import com.iv.permission.feign.client.UserServiceClient;
import com.iv.permission.response.ErrorMsg;

@Service
public class SubTenantPermissionService {
	
	@Autowired 
	private SubTenantPermissionDaoImpl subTenantPermissionDao;
	@Autowired 
	private SubTenantRoleDaoImpl subTenantRoleDao;
	@Autowired
	private SubTenantUserRoleDaoImpl subTenantUserRoleDao;
	@Autowired
	private UserServiceClient userServiceClient;
	@Autowired
	private GroupServiceClient groupService;
	@Autowired
	private FunctionInfoDaoImpl functionInfoDao;
	@Autowired
	private PermissionDaoImpl permissionDao;	

	/**
	 * 预设项目组创建者
	 * @param subTenantId
	 * @param userId
	 */
	public void createSubEnterpriseAdmin(String subTenantId, int userId) {
		//赋予项目组所有权限
		List<Integer> functionIds = functionInfoDao.selectAllSubFunction();
		List<SubTenantPermission> subPermissions = new ArrayList<SubTenantPermission>();
		for (Integer integer : functionIds) {
			SubTenantPermission permission = new SubTenantPermission();
			permission.setGroupPermissionId(integer);
			subPermissions.add(permission);
		}
		subTenantPermissionDao.saveOrUpdate(subPermissions, subTenantId);
		SubTenantRole role = new SubTenantRole();
		role.setDescription("项目组创建者");
		role.setName("subTenant_admin");
		role.setSubTenantPermissions(subPermissions);
		subTenantRoleDao.saveOrUpdateSubTenantRoleBytenantId(role,subTenantId);
		List<SubTenantRole> roles = new ArrayList<SubTenantRole>();
		roles.add(role);
		SubTenantUserRole subTenantUserRole = new SubTenantUserRole();
		subTenantUserRole.setUserId(userId);
		subTenantUserRole.setSubTenantRoles(roles);
		subTenantUserRoleDao.saveByTenantId(subTenantUserRole, subTenantId);		
	}
	
	/**
	 * 获取项目组审批人员列表
	 * @param codez
	 * @return
	 */
	public Set<LocalAuthDto> getSubEnterpriseApprovalPersons(String tenantId, String code){		
		/*Set<LocalAuthDto> set = new HashSet<LocalAuthDto>();
		LocalAuthDto localAuthDto = new LocalAuthDto();
		localAuthDto.setNickName("mac");
		localAuthDto.setId(2);
		localAuthDto.setRealName("马成");
		set.add(localAuthDto);
		return set;*/
		//获取拥有code的权限集合
		List<Integer> funcIds = functionInfoDao.selectApproveFuncIds(code);
		//租户下拥有特定权限人员集合
		List<Integer> userIds = subTenantUserRoleDao.getUserIdsByFuncIds(tenantId,funcIds);		
		UsersQueryDto usersQueryDto = new UsersQueryDto();
		usersQueryDto.setUserIds(userIds);
		usersQueryDto.setLoginType(LoginType.WECHAT);
		List<LocalAuthDto> localAuthDtos0 = userServiceClient.selectUserInfos(usersQueryDto, tenantId);
		Set<LocalAuthDto> localAuthDtos = new HashSet<>(localAuthDtos0);
		return localAuthDtos;			
	}
	
	/**
	 * 获取项目组权限获取情况列表
	 * @return
	 */
	public List<FunctionInfoDto> getAllPermissionStatus(){
		List<Integer> subTenantPermissions = subTenantPermissionDao.selectAllSubTenantPermission();
		List<FunctionInfo> functionInfos = functionInfoDao.selectAllFunction();
		List<FunctionInfoDto> functionInfoDtos = new ArrayList<FunctionInfoDto>();
		for (FunctionInfo functionInfo : functionInfos) {
			FunctionInfoDto functionInfoDto = convertPermissionDto(functionInfo);
			if(subTenantPermissions.contains(functionInfo.getId())) {				
				functionInfoDto.setIsSubscribed(YesOrNo.YES);
			}else {
				functionInfoDto.setIsSubscribed(YesOrNo.NO);
			}
			functionInfoDtos.add(functionInfoDto);
		}
		return functionInfoDtos;
	}
	
	/**
	 * 创建项目组角色
	 * @return
	 */
	public ResponseDto createSubTenantRole(SubTenantRoleDto createRoleDto,HttpServletRequest request){
		if(createRoleDto.getPermissionIds().size() > 0 && !StringUtils.isEmpty(createRoleDto.getName())
				&& !StringUtils.isEmpty(createRoleDto.getDes())){
			SubTenantRole subTenantRole = new SubTenantRole();
			int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));	
			subTenantRole.setName(createRoleDto.getName());
			subTenantRole.setDescription(createRoleDto.getDes());
			List<SubTenantPermission> subTenantPermissions = subTenantPermissionDao.
					selectSubTenantPermissionByIds(createRoleDto.getPermissionIds());
			subTenantRole.setSubTenantPermissions(subTenantPermissions);
			subTenantRole.setGroupIds(createRoleDto.getGroupIds());
			subTenantRole.setCreateBy(userId);
			subTenantRole.setCreateDate(System.currentTimeMillis());
			subTenantRoleDao.saveOrUpdateSubTenantRole(subTenantRole);
			return ResponseDto.builder(ErrorMsg.OK);
		}else {
			return ResponseDto.builder(ErrorMsg.CREATE_SUBTENANT_ROLE_FAILED);
		}
	}
	
	/**
	 * 获取项目组已获得功能列表（角色创建使用）
	 * @return
	 */
	public List<FunctionInfo> getSubTenantPermissions(){
		List<Integer> codes = subTenantPermissionDao.selectAllSubTenantPermission();
		Set<Integer> set = new HashSet<Integer>(codes);
		return functionInfoDao.selectFunctionByIds(set);	
	}
	
	/**
	 * 将FunctionInfo 转为 FunctionInfoDto
	 * @param userOAuth
	 * @return
	 */
	public FunctionInfoDto convertPermissionDto(FunctionInfo functionInfo) {
		FunctionInfoDto functionInfoDto = null;	
		if(null != functionInfo) {		
			functionInfoDto = new FunctionInfoDto();
			BeanCopier copy=BeanCopier.create(FunctionInfo.class, FunctionInfoDto.class, false);
			copy.copy(functionInfo, functionInfoDto, null);
		}		
		return functionInfoDto;
	}
	
	/**
	 * 删除项目组角色
	 * @param id
	 * @return
	 */
	public ResponseDto deleteSubTenantRole(IdsDto idDto) {
		try {
			if(idDto.getIds().size() == 0) {
				return ResponseDto.builder(ErrorMsg.DELETE_SUBTENANT_ROLE_FAILED);
			}
			subTenantRoleDao.deleteSubTenantRole(idDto.getIds());
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseDto.builder(ErrorMsg.DELETE_SUBTENANT_ROLE_FAILED);
		}
		
		
	}
	
	/**
	 * 项目组角色编辑
	 * @param createRoleDto
	 * @return
	 */
	public ResponseDto editSubTenantRole(SubTenantRoleDto createRoleDto) {
		try {
			if(createRoleDto.getId() == 0 || StringUtils.isEmpty(createRoleDto.getName()) 
					|| StringUtils.isEmpty(createRoleDto.getDes())
					|| createRoleDto.getPermissionIds().size() == 0) {
				return ResponseDto.builder(ErrorMsg.INFORMATION_INCOMPLETE);
			}
			SubTenantRole subTenantRole = subTenantRoleDao.selectSubTenantRoleById(createRoleDto.getId());
			List<SubTenantPermission> permissions = subTenantPermissionDao.selectSubTenantPermissionByIds(createRoleDto.getPermissionIds());
			subTenantRole.setGroupIds(createRoleDto.getGroupIds());
			subTenantRole.setName(createRoleDto.getName());
			subTenantRole.setDescription(createRoleDto.getDes());
			subTenantRole.setSubTenantPermissions(permissions);
			subTenantRoleDao.saveOrUpdateSubTenantRole(subTenantRole);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseDto.builder(ErrorMsg.EDIT_SUBTENANT_ROLE_FAILED);
		}
	}
	
	/**
	 * 获取项目组下角色列表
	 * @return
	 */
	public ResponseDto getSubTenantRoles() {
		try {
			List<SubTenantRole> subTenantRoles = subTenantRoleDao.selectAllSubTenantRole();	
			List<SubTenantRoleFunctionDto> subTenantRoleFunctionDtos = new ArrayList<SubTenantRoleFunctionDto>();
			if(!CollectionUtils.isEmpty(subTenantRoles)) {
				for (SubTenantRole subTenantRole : subTenantRoles) {
					List<SubTenantPermission> subTenantPermissions = subTenantRole.getSubTenantPermissions();
					Set<Integer> idsSet = new HashSet<>();
					for (SubTenantPermission subTenantPermission : subTenantPermissions) {
						idsSet.add(subTenantPermission.getGroupPermissionId());
					}
					SubTenantRoleFunctionDto subTenantRoleFunctionDto = new SubTenantRoleFunctionDto();
					List<FunctionInfo> functionInfos = functionInfoDao.selectFunctionByIds(idsSet);
					subTenantRoleFunctionDto.setId(subTenantRole.getId());
					subTenantRoleFunctionDto.setDes(subTenantRole.getDescription());
					subTenantRoleFunctionDto.setName(subTenantRole.getName());
					Set<FunctionInfo> result = new HashSet<FunctionInfo>(functionInfos);
					subTenantRoleFunctionDto.setFunctionInfos(result);
					subTenantRoleFunctionDtos.add(subTenantRoleFunctionDto);
				}
			}
			
			return ResponseDto.builder(ErrorMsg.OK, subTenantRoleFunctionDtos);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseDto.builder(ErrorMsg.GET_SUBTENANT_ROLE_FAILED);
		}
	}
	
	/**
	 * 项目组人员权限赋予
	 * @param subTenantUserRoleDto
	 * @return
	 */
	public ResponseDto editSubTenantPersonRole(SubTenantUserRoleDto subTenantUserRoleDto) {
		try {
			if(subTenantUserRoleDto.getUserId() == 0 || subTenantUserRoleDto.getRoleIds().size() == 0) {
				return ResponseDto.builder(ErrorMsg.INFORMATION_INCOMPLETE);
			}
			SubTenantUserRole userRole = subTenantUserRoleDao.selectUserRoleByUserId(subTenantUserRoleDto.getUserId());
			if(null == userRole) {
				userRole = new SubTenantUserRole();
			}
			List<SubTenantRole> subTenantRoles = subTenantRoleDao.selectSubTenantRoleByIds(subTenantUserRoleDto.getRoleIds());
			userRole.setSubTenantRoles(subTenantRoles);
			userRole.setUserId(subTenantUserRoleDto.getUserId());
			subTenantUserRoleDao.save(userRole);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseDto.builder(ErrorMsg.GIVE_USER_ROLE_FAILED);
		}
	}
	
	/**
	 * 获取子项目展示的角色列表
	 * @param serviceType
	 * @return
	 */
	public List<SubTenantRoleFunctionDto> getSubprojectRoles(ServiceType serviceType, HttpServletRequest req) {
		//获取serviceType，以及各基础权限的功能集合
		String curTenantId = JWTUtil.getJWtJson(req.getHeader("Authorization")).getString("curTenantId");
		List<ServiceType> services = new ArrayList<ServiceType>();
		services.add(serviceType);
		services.add(ServiceType.GROUP);
		services.add(ServiceType.PERMISSION);
		List<FunctionInfo> functions =  functionInfoDao.selectFunctionByServiceType(services);
		//获取租户所获取的权限信息集
		List<Integer> functionIds = subTenantPermissionDao.selectAllSubTenantPermissionByTenId(curTenantId);
		Set<Integer> subFunctionIds = new HashSet<Integer>();
		//将基础code与租户code取交集
		for (FunctionInfo functionInfo : functions) {
			if(functionIds.contains(functionInfo.getId())) { 
				subFunctionIds.add(functionInfo.getId());
			}
		}
		Set<Integer> set = new HashSet<Integer>(subFunctionIds);	
		//List<SubTenantRole> subTenantRoles = subTenantRoleDao.selectSubTenantRoleByIds(set);
		List<Integer> permissionIds = subTenantRoleDao.selectSubTenantRoleIds(set);
		Set<Integer> permissionIdsSet = new HashSet<Integer>(permissionIds);
		List<SubTenantRole> subTenantRoles = subTenantRoleDao.selectSubTenantRoleByIds(permissionIdsSet);
		List<SubTenantRoleFunctionDto> subTenantRoleFunctionDtos = new ArrayList<SubTenantRoleFunctionDto>();
		if(!CollectionUtils.isEmpty(subTenantRoles)) {
			for (SubTenantRole subTenantRole : subTenantRoles) {
				List<SubTenantPermission> subTenantPermissions = subTenantRole.getSubTenantPermissions();
				Set<Integer> idsSet = new HashSet<>();
				for (SubTenantPermission subTenantPermission : subTenantPermissions) {
					idsSet.add(subTenantPermission.getGroupPermissionId());
				}
				SubTenantRoleFunctionDto subTenantRoleFunctionDto = new SubTenantRoleFunctionDto();
				List<FunctionInfo> functionInfos = functionInfoDao.selectFunctionByIds(idsSet);
				subTenantRoleFunctionDto.setId(subTenantRole.getId());
				subTenantRoleFunctionDto.setDes(subTenantRole.getDescription());
				subTenantRoleFunctionDto.setName(subTenantRole.getName());
				Set<FunctionInfo> result = new HashSet<FunctionInfo>(functionInfos);
				subTenantRoleFunctionDto.setFunctionInfos(result);
				subTenantRoleFunctionDtos.add(subTenantRoleFunctionDto);
			}
		}
		return subTenantRoleFunctionDtos;
		
	}
	
	/**
	 * 获取人员角色列表
	 * @param userId
	 * @return
	 */
	public Set<com.iv.outer.dto.SubTenantRoleDto> selectPersonRole(int userId, String tenantId){
		List<SubTenantRole> subTenantRoles = subTenantUserRoleDao.selectUserRoleByTenantId(userId,tenantId);
		Set<com.iv.outer.dto.SubTenantRoleDto> subTenantRoleDtos = new HashSet<com.iv.outer.dto.SubTenantRoleDto>();
		for (SubTenantRole subTenantRole : subTenantRoles) {
			com.iv.outer.dto.SubTenantRoleDto subTenantRoleDto = new com.iv.outer.dto.SubTenantRoleDto();
			subTenantRoleDto.setId(subTenantRole.getId());
			subTenantRoleDto.setName(subTenantRole.getName());
			subTenantRoleDtos.add(subTenantRoleDto);
		}
		return subTenantRoleDtos;
	}
	
	/**
	 * 获取用户获取的功能权限集
	 * @param UserId
	 * @return
	 */
	public List<PermissionDto> getPersonPermissions(int userId, String tenantId){
		List<SubTenantRole> subTenantRoles = subTenantUserRoleDao.selectUserRoleByTenantId(userId,tenantId);
		if(CollectionUtils.isEmpty(subTenantRoles)) {
			return null;
		}
		Set<Integer> functionIds = new HashSet<Integer>();
		for (SubTenantRole subTenantRole : subTenantRoles) {
			List<SubTenantPermission> functions = subTenantRole.getSubTenantPermissions();
			for (SubTenantPermission subTenantPermission : functions) {
				functionIds.add(subTenantPermission.getGroupPermissionId());
			}
		}
		List<FunctionInfo> functions = functionInfoDao.selectFunctionByIds(functionIds);
		List<PermissionDto> permissionDtos = new ArrayList<PermissionDto>();
		for (FunctionInfo functionInfo : functions) {
			List<PermissionInfo> permissions = functionInfo.getPermissionInfos();
			for (PermissionInfo permissionInfo : permissions) {
				PermissionDto dto = new PermissionDto();
				BeanCopier copy=BeanCopier.create(PermissionInfo.class, PermissionDto.class, false);
				copy.copy(permissionInfo, dto, null);
				permissionDtos.add(dto);
			}
		}		
		
		return permissionDtos;
		
	}
	
	/**
	 * 子系统权限集合
	 * @param serviceType
	 * @return
	 */
	public List<FunctionInfo> getFormSysPermissions(ServiceType serviceType, HttpServletRequest req){
		//获取serviceType，以及各基础权限的功能集合
		String curTenantId = JWTUtil.getJWtJson(req.getHeader("Authorization")).getString("curTenantId");
		List<ServiceType> services = new ArrayList<ServiceType>();
		services.add(serviceType);
		services.add(ServiceType.PERMISSION);
		services.add(ServiceType.GROUP);
		List<FunctionInfo> functions =  functionInfoDao.selectFunctionByServiceType(services);
		//获取租户所获取的权限信息集
		List<Integer> functionIds = subTenantPermissionDao.selectAllSubTenantPermissionByTenId(curTenantId);
		Set<Integer> subFunctionIds = new HashSet<Integer>();
		//将基础code与租户code取交集
		for (FunctionInfo functionInfo : functions) {
			if(functionIds.contains(functionInfo.getId())) {
				subFunctionIds.add(functionInfo.getId());
			}
		}
		Set<Integer> set = new HashSet<Integer>(subFunctionIds);
		List<FunctionInfo> subFunctions = functionInfoDao.selectFunctionByIds(subFunctionIds);
		//subFunctions.retainAll(functions);
		//去functions subFunctions的交集
		
		return subFunctions;
	}
	
	/**
	 * 查询组内拥有审批工单权限的人员列表-工单服务调用
	 * @param userId
	 * @param tenantId
	 * @param groupId
	 * @return
	 */
	public Set<LocalAuthDto> approveFormPerson(String code, String tenantId, short groupId){
		//获取拥有code的权限集合
		List<Integer> funcIds = functionInfoDao.selectApproveFuncIds(code);
		//租户下拥有审批工单权限的人员集合
		List<Integer> userIds = subTenantUserRoleDao.getUserIdsByFuncIds(tenantId,funcIds);
		//GroupEntityDto GroupEntityDto = groupService.selectGroupInfo(tenantId, groupId);
		//组内所有人员id集合
		//List<Integer> groupUserIds = GroupEntityDto.getUserId();
		List<Integer> groupUserIds = groupService.selectGroupUserIds(tenantId, groupId);
		//组内含有审批工单权限的人员id集合
		List<Integer> userOutIds = new ArrayList<Integer>();
		for (Integer userId0 : userIds) {
			if(groupUserIds.contains(userId0)) {
				userOutIds.add(userId0);
			}
		}
		UsersQueryDto usersQueryDto = new UsersQueryDto();
		usersQueryDto.setUserIds(userOutIds);
		usersQueryDto.setLoginType(LoginType.WECHAT);
		List<LocalAuthDto> localAuthDtos0 = userServiceClient.selectUserInfos(usersQueryDto, tenantId);
		Set<LocalAuthDto> localAuthDtos = new HashSet<>(localAuthDtos0);
		return localAuthDtos;		
	}
}
