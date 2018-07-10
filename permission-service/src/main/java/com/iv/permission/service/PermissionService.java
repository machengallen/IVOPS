package com.iv.permission.service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iv.common.enumeration.YesOrNo;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.CodesDto;
import com.iv.permission.api.dto.FunctionDto;
import com.iv.permission.api.dto.FunctionInfoDto;
import com.iv.permission.api.dto.GlobalRoleCreate;
import com.iv.permission.api.dto.IdsDto;
import com.iv.permission.api.dto.PageQueryDto;
import com.iv.permission.api.dto.PermissionDto;
import com.iv.permission.api.dto.PersonRoleDto;
import com.iv.permission.dao.impl.FunctionInfoDaoImpl;
import com.iv.permission.dao.impl.GlobalRoleDaoImpl;
import com.iv.permission.dao.impl.LocalAuthRoleDaoImpl;
import com.iv.permission.dao.impl.PermissionDaoImpl;
import com.iv.permission.dto.PermissionPageDto;
import com.iv.permission.entity.FunctionInfo;
import com.iv.permission.entity.GlobalRole;
import com.iv.permission.entity.LocalAuthRole;
import com.iv.permission.entity.PermissionInfo;
import com.iv.permission.feign.client.UserServiceClient;
import com.iv.permission.response.ErrorMsg;

@Service
public class PermissionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionService.class);
	@Autowired
	private PermissionDaoImpl permissionDao;			
	@Autowired
	private LocalAuthRoleDaoImpl localAuthRoleDao;
	@Autowired
	private UserServiceClient userServiceClient;
	@Autowired
	private FunctionInfoDaoImpl  functionInfoDao;
	@Autowired
	private GlobalRoleDaoImpl  globalRoleDao;
	/**
	 * 获取基础库权限列表
	 * @return
	 */
	public PermissionPageDto getPermissionList(PageQueryDto pageQueryDto){
		return permissionDao.getPermissionPageList((pageQueryDto.getCurPage() - 1) * pageQueryDto.getItems(), pageQueryDto.getItems());		
	}	
	
	/**
	 * 获取运营审批人员列表
	 * @param code
	 * @return
	 */
	public Set<LocalAuthDto> getApprovalPersons(String code){			
		Set<LocalAuthDto> set = new HashSet<LocalAuthDto>();
		LocalAuthDto localAuthDto = userServiceClient.selectLocalauthInfoByName("admin");
		set.add(localAuthDto);
		return set;
	}		
	
	/**
	 * 创建功能权限
	 * @param createFunctionDto
	 * @return
	 */
	public ResponseDto createFunction(FunctionDto createFunctionDto,HttpServletRequest request) {
		try {
			if(StringUtils.isEmpty(createFunctionDto.getName()) 
					|| createFunctionDto.getPermissionIds().size() <= 0
					|| StringUtils.isEmpty(createFunctionDto.getServiceType().toString())) {
				return ResponseDto.builder(ErrorMsg.INFORMATION_INCOMPLETE);
			}
			FunctionInfo functionInfo = new FunctionInfo();
			List<String> codes = createFunctionDto.getPermissionIds();		
			List<PermissionInfo> permissions = permissionDao.getPermissionsByIds(codes);
			functionInfo.setName(createFunctionDto.getName());
			functionInfo.setServiceType(createFunctionDto.getServiceType());
			functionInfo.setPermissionInfos(permissions);
			functionInfo.setIsValid(createFunctionDto.getIsValid());
			functionInfo.setNeedSubscribe(createFunctionDto.getNeedSubscribe());
			functionInfo.setReleaseOrNot(createFunctionDto.getReleaseOrNot());
			int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));	
			functionInfo.setCreateBy(userId);
			functionInfo.setCreateDate(System.currentTimeMillis());
			functionInfoDao.saveFunction(functionInfo);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("功能权限创建失败");
			return ResponseDto.builder(ErrorMsg.CREATE_FUNCTION_FAILED);
		}		
	}
	
	/**
	 * 角色创建（基础库）
	 * @param globalRoleCreate
	 * @return
	 */
	public ResponseDto createGlobalRole(GlobalRoleCreate globalRoleCreate,HttpServletRequest request) {
		try {
			if(StringUtils.isEmpty(globalRoleCreate.getName()) || StringUtils.isEmpty(globalRoleCreate.getDescription())
					|| globalRoleCreate.getFunctionIds().size() == 0) {
				return ResponseDto.builder(ErrorMsg.INFORMATION_INCOMPLETE);
			}
			int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));	
			GlobalRole globalRole = new GlobalRole();
			globalRole.setName(globalRoleCreate.getName());
			globalRole.setDescription(globalRoleCreate.getDescription());
			List<FunctionInfo> functions = functionInfoDao.selectFunctionByIds(globalRoleCreate.getFunctionIds());
			globalRole.setFunctionInfos(functions);
			globalRole.setSubTenantIds(globalRoleCreate.getSubTenantIds());
			globalRole.setCreateDate(System.currentTimeMillis());
			globalRole.setCreateBy(userId);
			globalRoleDao.saveOrUpdate(globalRole);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseDto.builder(ErrorMsg.CREATE_SUBTENANT_ROLE_FAILED);
		}
		
	}
	
	/**
	 * 角色编辑（基础库）
	 * @param globalRoleCreate
	 * @return
	 */
	public ResponseDto editGlobalRole(GlobalRoleCreate globalRoleCreate) {
		try {
			if(globalRoleCreate.getId() == 0) {
				return ResponseDto.builder(ErrorMsg.INFORMATION_INCOMPLETE);
			}
			GlobalRole globalRole = globalRoleDao.selectById(globalRoleCreate.getId());
			globalRole.getFunctionInfos().clear();
			globalRoleDao.saveOrUpdate(globalRole);
			globalRole.setName(globalRoleCreate.getName());
			globalRole.setDescription(globalRoleCreate.getDescription());
			List<FunctionInfo> functions = functionInfoDao.selectFunctionByIds(globalRoleCreate.getFunctionIds());
			globalRole.setFunctionInfos(functions);
			globalRole.setSubTenantIds(globalRoleCreate.getSubTenantIds());
			globalRoleDao.saveOrUpdate(globalRole);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseDto.builder(ErrorMsg.EDIT_SUBTENANT_ROLE_FAILED);
		}
	}
	
	/**
	 * 角色删除（基础库）
	 * @param id
	 */
	public void deleteGlobalRole(IdsDto idsDto) {
		globalRoleDao.deleteGlobalRoleByIds(idsDto.getIds());
	}
	
	/**
	 * 角色赋予
	 * @return
	 */
	public ResponseDto GivePersonRole(PersonRoleDto personRoleDto) {
		try {
			if(personRoleDto.getUserId() == 0 || personRoleDto.getRoleIds().size() == 0) {
				return ResponseDto.builder(ErrorMsg.INFORMATION_INCOMPLETE);
			}			
			LocalAuthRole localAuthRole = localAuthRoleDao.selectLocalAuthRoleById(personRoleDto.getUserId());
			if(null != localAuthRole) {
				localAuthRole.getGlobalRoles().clear();
				localAuthRoleDao.saveOrUpdate(localAuthRole);
			}else {
				localAuthRole = new LocalAuthRole();
			}			
			List<GlobalRole> globals = globalRoleDao.selectGlobalRoleByIds(personRoleDto.getRoleIds());
			localAuthRole.setUserId(personRoleDto.getUserId());
			localAuthRole.setGlobalRoles(globals);
			localAuthRoleDao.saveOrUpdate(localAuthRole);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseDto.builder(ErrorMsg.EDIT_SUBTENANT_ROLE_FAILED);
		}
	}
	
	/**
	 * 权限新增
	 * @param permissionDto
	 * @return
	 */
	public ResponseDto createPermission(PermissionDto permissionDto,HttpServletRequest request) {
		try {
			if(StringUtils.isEmpty(permissionDto.getCode()) || StringUtils.isEmpty(permissionDto.getUrl())
					|| StringUtils.isEmpty(permissionDto.getDescription())) {
				return ResponseDto.builder(ErrorMsg.INFORMATION_INCOMPLETE);
			}
			PermissionInfo permissionInfo = new PermissionInfo();
			permissionInfo.setCode(permissionDto.getCode());
			permissionInfo.setDescription(permissionDto.getDescription());
			permissionInfo.setUrl(permissionDto.getUrl());
			permissionInfo.setIsValid(YesOrNo.YES);	
			permissionInfo.setServiceType(permissionDto.getServiceType());
			//permissionInfo.setCreateDate(System.currentTimeMillis());
			int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));	
			//permissionInfo.setCreateBy(userId);
			permissionDao.saveOrUpdatePermission(permissionInfo);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("权限新增失败");
			return ResponseDto.builder(ErrorMsg.CREATE_PERMISSION_FAILED);
		}
	}
	
	/**
	 * 删除权限
	 * @param code
	 * @return
	 */
	public ResponseDto deletePermission(CodesDto codesDto) {
		try {
			if(codesDto.getCodes().size() == 0) {
				return ResponseDto.builder(ErrorMsg.INFORMATION_INCOMPLETE);
			}
			permissionDao.deletePermissionByIds(codesDto.getCodes());
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("权限删除失败");
			return ResponseDto.builder(ErrorMsg.DELETE_PERMISSION_FAILED);
		}
	}
	
	/**
	 * 权限编辑
	 * @param permissionDto
	 * @return
	 */
	public ResponseDto editPermission(PermissionDto permissionDto){
		try {
			if(StringUtils.isEmpty(permissionDto.getCode())) {
				return ResponseDto.builder(ErrorMsg.INFORMATION_INCOMPLETE);
			}
			PermissionInfo permissionInfo = permissionDao.getPermissionsById(permissionDto.getCode());
			if(!StringUtils.isEmpty(permissionDto.getDescription())) {
				permissionInfo.setDescription(permissionDto.getDescription());
			}
			if(!StringUtils.isEmpty(permissionDto.getUrl())) {
				permissionInfo.setUrl(permissionDto.getUrl());
			}
			if(!StringUtils.isEmpty(permissionDto.getIsValid().toString())) {
				permissionInfo.setIsValid(permissionDto.getIsValid());
			}				
			if(!StringUtils.isEmpty(permissionDto.getServiceType())) {
				permissionInfo.setServiceType(permissionDto.getServiceType());
			}
			permissionDao.saveOrUpdatePermission(permissionInfo);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("编辑权限失败");
			return ResponseDto.builder(ErrorMsg.EDIT_PERMISSION_FAILED);
		}
	}
	
	/**
	 * 获取权限列表（无分页）
	 * @return
	 */
	public ResponseDto getPermissionList() {
		try {
			return ResponseDto.builder(ErrorMsg.OK, permissionDao.getPermissionList());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("权限列表获取失败");
			return ResponseDto.builder(ErrorMsg.GET_OPERATION_AUTHORITY);
		}
	}
	
	/**
	 * 权限删除
	 * @param idsDto
	 * @return
	 */
	public ResponseDto deleteFunction(IdsDto idsDto) {
		try {
			if(idsDto.getIds().size() == 0) {
				return ResponseDto.builder(ErrorMsg.INFORMATION_INCOMPLETE);
			}
			functionInfoDao.deleteFunctions(idsDto.getIds());
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("权限删除失败");
			return ResponseDto.builder(ErrorMsg.DELETE_FUNCTION_FAILED);
		}
	}
	
	/**
	 * 权限编辑
	 * @param functionDto
	 * @return
	 */
	public ResponseDto editFunction(FunctionDto functionDto) {
		try {
			if(StringUtils.isEmpty(functionDto.getServiceType().toString()) || functionDto.getPermissionIds().size() == 0) {
				return ResponseDto.builder(ErrorMsg.INFORMATION_INCOMPLETE);
			}
			FunctionInfo functionInfo = functionInfoDao.selectFunctionById(functionDto.getId());
			List<PermissionInfo> permissions = permissionDao.getPermissionsByIds(functionDto.getPermissionIds());
			functionInfo.setPermissionInfos(permissions);
			functionInfo.setServiceType(functionDto.getServiceType());		
			functionInfo.setName(functionDto.getName());
			functionInfo.setIsValid(functionDto.getIsValid());
			functionInfo.setNeedSubscribe(functionDto.getNeedSubscribe());
			functionInfo.setReleaseOrNot(functionDto.getReleaseOrNot());
			functionInfoDao.saveFunction(functionInfo);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("权限编辑失败");
			return ResponseDto.builder(ErrorMsg.EDIT_FUNCTION_FAILED);
		}
	}
	
	/**
	 * 权限列表
	 */
	public ResponseDto getFunction() {
		try {
			return ResponseDto.builder(ErrorMsg.OK, functionInfoDao.selectAllFunction());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("权限列表失败");
			return ResponseDto.builder(ErrorMsg.GET_OPERATION_AUTHORITY);
		}
	}
	
	/**
	 * 角色列表
	 * @return
	 */
	public ResponseDto getGlobalRole() {
		try {
			return ResponseDto.builder(ErrorMsg.OK, globalRoleDao.selectGlobalRoles());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("角色列表失败");
			return ResponseDto.builder(ErrorMsg.GET_GLOBAL_ROLE_FAILED);
		}
	}
}
