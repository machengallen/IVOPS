package com.iv.permission.api.service;

import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.response.ResponseDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.CreateRoleDto;
import com.iv.permission.api.dto.PermissionDto;

/**
 * 权限管理api
 * @author zhangying
 * 2018年5月7日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
public interface IPermissionService {

	/**
	 * 预设项目组创建者：入参：项目组id（int），用户id（int），项目组DB，默认为subEnterprise_admin
	 * @param subTenantId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/create/subEnterpriseAdmin", method = RequestMethod.GET)
	ResponseDto createSubEnterpriseAdmin(@RequestParam("subTenantId") String subTenantId, @RequestParam("userId") int userId);
	
	/**
	 * 获取项目组审批人员列表：入参：审批项目组权限code
	 * @param subTenantId
	 * @return
	 */
	@RequestMapping(value = "/subEnterprise/ApprovalPersons", method = RequestMethod.GET)
	Set<LocalAuthDto> getSubEnterpriseApprovalPersons(@RequestParam("subTenantId") String subTenantId, @RequestParam("code") String code);	
	
	/**
	 * 从基础库获取有审批权限code的人员列表
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/get/approvalPersons", method = RequestMethod.GET)
	Set<LocalAuthDto> getApprovalPersons(@RequestParam("code") String code);
	
	/**
	 * 获取运营权限列表
	 * @return
	 */
	@RequestMapping(value = "/get/permissionList", method = RequestMethod.GET)
	ResponseDto getPermissionList(); 
	
	/**
	 * 获取项目组权限列表（包括已获取、未订阅）
	 * @return
	 */
	@RequestMapping(value = "/get/allPermissionStatus", method = RequestMethod.GET)
	ResponseDto getAllPermissionStatus();
	/**
	 * 权限编辑（基础库）
	 * @return
	 */
	ResponseDto editPermission(@RequestParam("code") String code);
	
	/**
	 * 权限增加（基础库）
	 * @return
	 */
	ResponseDto addPermission(@RequestBody PermissionDto permissionDto);
	
	/**
	 * 角色编辑（基础库）
	 * @param roleId
	 * @return
	 */
	ResponseDto editGlobalRole(@RequestParam("roleId") int roleId);
	
	/**
	 * 角色编辑（项目组库）
	 * @param roleId
	 * @return
	 */
	ResponseDto editSubTenantRole(@RequestParam("roleId") int roleId);
	
	/**
	 * 创建项目组角色
	 * @param createRoleDto
	 * @return
	 */
	@RequestMapping(value = "/create/subTenantRole", method = RequestMethod.POST)
	ResponseDto createSubTenantRole(@RequestBody CreateRoleDto createRoleDto);
	
	/**
	 * 获取项目组下角色列表
	 * @return
	 */
	@RequestMapping(value = "/select/subTenantRoles", method = RequestMethod.GET)
	ResponseDto selectSubTenantRoles();
	
	/**
	 * 创建运营角色
	 * @param createRoleDto
	 * @return
	 */
	ResponseDto createGlobalRole(@RequestBody CreateRoleDto createRoleDto);
	
	/**
	 * 创建项目组角色
	 * @return
	 */
	@RequestMapping(value = "/subTenant/permissions", method = RequestMethod.GET)
	ResponseDto getSubTenantPermissions();
}
