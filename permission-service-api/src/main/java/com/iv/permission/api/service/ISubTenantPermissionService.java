package com.iv.permission.api.service;

import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.response.ResponseDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.permission.api.dto.SubTenantRoleDto;
import com.iv.permission.api.dto.SubTenantUserRoleDto;
import com.iv.permission.api.dto.PageQueryDto;

/**
 * 项目组权限api
 * @author zhangying
 * 2018年5月21日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
public interface ISubTenantPermissionService {
	
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
	 * 获取项目组权限列表（包括已获取、未订阅）
	 * @return
	 */
	@RequestMapping(value = "/get/allPermissionStatus", method = RequestMethod.POST)
	ResponseDto getAllPermissionStatus(@RequestBody PageQueryDto pageQueryDto);
	
	/**
	 * 创建项目组角色
	 * @param createRoleDto
	 * @return
	 */
	@RequestMapping(value = "/create/subTenantRole", method = RequestMethod.POST)
	ResponseDto createSubTenantRole(@RequestBody SubTenantRoleDto createRoleDto);
	
	/**
	 * 项目组已获取的权限
	 * @return
	 */
	@RequestMapping(value = "/subTenant/permissions", method = RequestMethod.GET)
	ResponseDto getSubTenantPermissions();
	
	/**
	 * 删除角色
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/delete/subTenantRole", method = RequestMethod.GET)
	ResponseDto deleteSubTenantRole(@RequestParam("roleId") int roleId);
	
	/**
	 * 编辑项目组角色
	 * @param createRoleDto
	 * @return
	 */
	@RequestMapping(value = "/edit/subTenantRole", method = RequestMethod.POST)
	ResponseDto editSubTenantRole(@RequestBody SubTenantRoleDto createRoleDto);
	
	/**
	 * 获取项目组下角色列表
	 * @return
	 */
	@RequestMapping(value = "/get/subTenantRoles", method = RequestMethod.GET)
	ResponseDto getSubTenantRoles();
	
	
	/**
	 * 编辑人员角色
	 * @return
	 */
	@RequestMapping(value = "/edit/subTenantPersonRole", method = RequestMethod.GET)
	ResponseDto editSubTenantPersonRole(@RequestBody SubTenantUserRoleDto subTenantUserRoleDto);
}
