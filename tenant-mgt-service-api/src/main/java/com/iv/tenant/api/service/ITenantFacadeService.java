package com.iv.tenant.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.dto.IdListDto;
import com.iv.common.response.ResponseDto;
import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.api.dto.SubEnterpriseInfoDto;
import com.iv.tenant.api.dto.SubTenantInfoDto;
import com.iv.tenant.api.dto.TenantApproveReq;
import com.iv.tenant.api.dto.TenantInfoDto;

/**
 * 租户管理
 * 
 * @author macheng 2018年4月4日 alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
public interface ITenantFacadeService {

	/**
	 * 获取已注册的企业主体信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get/enterprises", method = RequestMethod.GET)
	ResponseDto getEnterNameList();

	/**
	 * 获取企业信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/get/enterprise/conditions", method = RequestMethod.POST)
	ResponseDto getTenant(@RequestBody QueryEnterReq req);

	/**
	 * 获取某企业所有项目组信息
	 * 
	 * @return
	 */
	/*@RequestMapping(value = "/get/enterprise/tenant/list", method = RequestMethod.GET)
	ResponseDto getSubTenant(@RequestParam("request") HttpServletRequest request);*/

	/**
	 * 获取项目组信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get/tenant", method = RequestMethod.GET)
	ResponseDto getTenantByTenantId(@RequestParam("request") HttpServletRequest request);
	
	/**
	 * 获取项目组信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get/tenant/backend", method = RequestMethod.GET)
	SubEnterpriseInfoDto getSubEnterprise(@RequestParam("request") HttpServletRequest request);

	/**
	 * 申请创建租户
	 * 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/apply/regis", method = RequestMethod.POST)
	ResponseDto newTenantApply(@RequestParam("request") HttpServletRequest request, @RequestBody TenantInfoDto dto);

	/**
	 * 申请流审批(管理员权限)
	 * 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	ResponseDto taskApprove(@RequestParam("request") HttpServletRequest request, @RequestBody TenantApproveReq dto);

	/**
	 * 获取待审批的租户创建申请(管理员权限)
	 * 
	 * @param first
	 * @param max
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/get/pending/regis/{first}/{max}", method = RequestMethod.GET)
	ResponseDto getUserTasksRegis(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max);

	/**
	 * 获取租户创建申请的历史工作流
	 * 
	 * @param first
	 * @param max
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/get/hisFlow/regis/{first}/{max}", method = RequestMethod.GET)
	ResponseDto getHistoryProcessRegis(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max);

	/**
	 * 申请加入租户
	 * 
	 * @param tenantId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/apply/join/{tenantId}", method = RequestMethod.GET)
	ResponseDto applyJoinTenant(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "tenantId", required = true) String tenantId);

	/**
	 * 获取待审批的加入租户申请(管理员权限)
	 * 
	 * @param first
	 * @param max
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/get/pending/join/{first}/{max}", method = RequestMethod.GET)
	ResponseDto getUserTasksJoin(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max);

	/**
	 * 获取加入租户申请的历史工作流
	 * 
	 * @param first
	 * @param max
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/get/hisFlow/join/{first}/{max}", method = RequestMethod.GET)
	ResponseDto getHistoryProcessJoin(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max);

	/**
	 * 租户创建者手动添加用户至租户(管理员权限)
	 * 
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value = "/add/user", method = RequestMethod.POST)
	ResponseDto manuallyAddUser(@RequestParam("request") HttpServletRequest request, @RequestBody IdListDto userIds);

	/**
	 * 用户切换租户
	 * 
	 * @param tenantId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/switch/{tenantId}", method = RequestMethod.GET)
	ResponseDto switchTenant(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "tenantId", required = true) String tenantId);

	/**
	 * 创建项目组
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/create/tenant", method = RequestMethod.POST)
	ResponseDto createSubTenant(@RequestParam("request") HttpServletRequest request, @RequestBody SubTenantInfoDto dto);

	/**
	 * 管理员手动添加用户至项目组
	 * 
	 * @param userIds
	 * @return
	 */
	/*@RequestMapping(value = "/mgt/adduser/sub", method = RequestMethod.POST)
	ResponseDto manuallyAddUserToSubTenant(@RequestParam("request") HttpServletRequest request,
			@RequestBody IdListDto userIds);
*/
	/**
	 * 删除项目组(管理员)
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/del/tenant/{id}", method = RequestMethod.GET)
	ResponseDto deleteSubTenant(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "id", required = true) int id);

	/**
	 * 获取所有项目组信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get/tenant/all", method = RequestMethod.GET)
	List<SubEnterpriseInfoDto> getSubEnterpriseAll();

}
