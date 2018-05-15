package com.iv.permission.api.service;

import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.response.ResponseDto;
import com.iv.outer.dto.LocalAuthDto;

/**
 * 权限管理api
 * @author zhangying
 * 2018年5月7日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 */
public interface IPermissionService {
	
	/**
	 * 设置企业创建者：入参：企业id（int），用户id（int），基础库
	 * 基础库预设企业管理员角色
	 * userID tenant_admin tenantID	
	 * @param tenantId
	 * @param userId
	 * @return
	 *//*
	@RequestMapping(value = "/create/enterpriseAdmin", method = RequestMethod.GET)
	ResponseDto createEnterpriseAdmin(@RequestParam("tenantId") int tenantId, @RequestParam("userId") int userId);
	*/	
		
	
	/**
	 * 获取企业创建者：入参：企业id（int）基础库
	 * 搜索条件：role=tenant_admin  id in {tenantids}
	 * @param subTenantId
	 * @param userId
	 * @return
	 *//*
	@RequestMapping(value = "/get/enterpriseAdmin", method = RequestMethod.GET)
	LocalAuthDto getEnterpriseAdmin(@RequestParam("tenantId") int tenantId);*/
	
	
	
	/**
	 * 获取项目组管理者：入参：项目组id（int）
	 * @param subTenantId
	 * @return
	 *//*
	@RequestMapping(value = "/get/subEnterpriseManagers", method = RequestMethod.GET)
	Set<LocalAuthDto> getSubEnterpriseManagers(@RequestParam("subTenantId") int subTenantId);*/
	
	/**
	 * 获取企业有审批权限的人员列表：入参：企业id（int），审批权限code
	 * @param tenantId
	 * @return
	 *//*
	@RequestMapping(value = "/get/enterpriseManagers", method = RequestMethod.GET)
	Set<LocalAuthDto> getEnterpriseManagers(@RequestParam("tenantId") int tenantId, @RequestParam("code") String code);*/
	/**
	 * 设置项目组创建者：入参：项目组id（int），用户id（int），项目组DB，默认为admin
	 * @param subTenantId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/create/subEnterpriseAdmin", method = RequestMethod.GET)
	ResponseDto createSubEnterpriseAdmin(@RequestParam("subTenantId") int subTenantId, @RequestParam("userId") int userId);
	
	/**
	 * 获取项目组创建者：入参：项目组id（int）
	 * 默认：项目组创建者角色名称:subTenant_admin
	 * @param subTenantId
	 * @return
	 */
	@RequestMapping(value = "/get/subEnterpriseAdmin", method = RequestMethod.GET)
	LocalAuthDto getSubEnterpriseAdmin(@RequestParam("subTenantId") int subTenantId);	
	
	/**
	 * 从基础库获取有审批权限code的人员列表
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/get/approvalPersons", method = RequestMethod.GET)
	Set<LocalAuthDto> getApprovalPersons(@RequestParam("code") String code);
	
}
