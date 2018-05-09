package com.iv.permission.api.service;

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
	 * 创建企业管理员：入参：企业id（int），用户id（int）
		3、获取项目组管理员：入参：项目组id（int）
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/create/enterpriseAdmin", method = RequestMethod.GET)
	ResponseDto createEnterpriseAdmin(@RequestParam("tenantId") int tenantId, @RequestParam("userId") int userId);
	
	/**
	 * 创建项目组管理员：入参：项目组id（int），用户id（int）
	 * @param subTenantId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/create/subEnterpriseAdmin", method = RequestMethod.GET)
	ResponseDto createSubEnterpriseAdmin(@RequestParam("subTenantId") int subTenantId, @RequestParam("userId") int userId);
	
	/**
	 * 创建项目组普通成员角色：入参：项目组id（int），用户id（int）
	 * @param subTenantId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/create/subEnterpriseCommonUser", method = RequestMethod.GET)
	ResponseDto createSubEnterpriseCommonUser(@RequestParam("subTenantId") int subTenantId, @RequestParam("userId") int userId);
	
	/**
	 * 获取企业管理员：入参：企业id（int）
	 * @param subTenantId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/get/enterpriseAdmin", method = RequestMethod.GET)
	LocalAuthDto getEnterpriseAdmin(@RequestParam("tenantId") int tenantId);
	
	/**
	 * 获取项目组管理员：入参：项目组id（int）
	 * @param subTenantId
	 * @return
	 */
	@RequestMapping(value = "/get/subEnterpriseAdmin", method = RequestMethod.GET)
	LocalAuthDto getSubEnterpriseAdmin(@RequestParam("subTenantId") int subTenantId);
}
