package com.iv.tenant.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.iv.common.dto.IdListDto;
import com.iv.common.response.ResponseDto;
import com.iv.tenant.api.dto.EnterpriseInfoDto;
import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.api.dto.SubEnterpriseInfoDto;
import com.iv.tenant.api.dto.SubTenantInfoDto;
import com.iv.tenant.api.dto.TenantApproveReq;
import com.iv.tenant.api.dto.TenantInfoDto;

/**
 * 租户管理
 * 
 * @author macheng 2018年4月4日
 *  alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
public interface ITenantFacadeService {

	/**
	 * 获取已注册租户的主体信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get/enterprises", method = RequestMethod.GET)
	ResponseDto getEnterNameList();

	/**
	 * 获取租户信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/get/targetEnter", method = RequestMethod.POST)
	ResponseDto getTenant(@RequestBody QueryEnterReq req);

	/**
	 * 获取租户下所有项目组信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get/subTenant/list", method = RequestMethod.GET)
	ResponseDto getSubTenant(HttpServletRequest request);
	
	/**
	 * 获取指定项目组信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get/subTenant", method = RequestMethod.GET)
	ResponseDto getSubTenant(String subTenantId);

	/**
	 * 申请创建租户
	 * 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mgt/apply/regis", method = RequestMethod.POST)
	ResponseDto newTenantApply(@RequestBody TenantInfoDto dto, HttpServletRequest request);

	/**
	 * 申请流审批(管理员权限)
	 * 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mgt/approve", method = RequestMethod.POST)
	ResponseDto taskApprove(@RequestBody TenantApproveReq dto, HttpServletRequest request);

	/**
	 * 获取待审批的租户创建申请(管理员权限)
	 * 
	 * @param first
	 * @param max
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mgt/usertask/regis/{first}/{max}", method = RequestMethod.GET)
	ResponseDto getUserTasksRegis(@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max, HttpServletRequest request);

	/**
	 * 获取租户创建申请的历史工作流
	 * 
	 * @param first
	 * @param max
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mgt/hisFlow/regis/{first}/{max}", method = RequestMethod.GET)
	ResponseDto getHistoryProcessRegis(@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max, HttpServletRequest request);

	/**
	 * 申请加入租户
	 * 
	 * @param tenantId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mgt/apply/join/{tenantId}", method = RequestMethod.GET)
	ResponseDto applyJoinTenant(@PathVariable(value = "tenantId", required = true) String tenantId,
			HttpServletRequest request);

	/**
	 * 获取待审批的加入租户申请(管理员权限)
	 * 
	 * @param first
	 * @param max
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mgt/usertask/join/{first}/{max}", method = RequestMethod.GET)
	ResponseDto getUserTasksJoin(@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max, HttpServletRequest request);

	/**
	 * 获取加入租户申请的历史工作流
	 * @param first
	 * @param max
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mgt/hisFlow/join/{first}/{max}", method = RequestMethod.GET)
	ResponseDto getHistoryProcessJoin(@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max, HttpServletRequest request);

	/**
	 * 租户创建者手动添加用户至租户(管理员权限)
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value = "/mgt/adduser/ten", method = RequestMethod.POST)
	ResponseDto manuallyAddUser(HttpServletRequest request, @RequestBody IdListDto userIds);
	
	/**
	 * 用户切换租户
	 * @param tenantId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mgt/switch/{tenantId}", method = RequestMethod.GET)
	ResponseDto switchTenant(@PathVariable(value = "tenantId", required = true) String tenantId, HttpServletRequest request);

	/**
	 * 创建租户下项目组
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/mgt/create/sub", method = RequestMethod.POST)
	ResponseDto createSubTenant(HttpServletRequest request, @RequestBody SubTenantInfoDto dto);
	
	/**
	 * 管理员手动添加用户至项目组
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value = "/mgt/adduser/sub", method = RequestMethod.POST)
	ResponseDto manuallyAddUserToSubTenant(HttpServletRequest request, @RequestBody IdListDto userIds);
	
	/**
	 * 删除项目组(管理员)
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mgt/del/sub/{id}", method = RequestMethod.GET)
	ResponseDto deleteSubTenant(@PathVariable(value = "id", required = true) int id, HttpServletRequest request);
	
	/**
	 * 获取所有租户信息
	 * @return
	 */
	@RequestMapping(value = "/get/tenant/all", method = RequestMethod.GET)
	List<EnterpriseInfoDto> getEnterpriseAll();
	
	/**
	 * 获取所有项目组信息
	 * @return
	 */
	@RequestMapping(value = "/get/subTenant/all", method = RequestMethod.GET)
	List<SubEnterpriseInfoDto> getSubEnterpriseAll();
	
}