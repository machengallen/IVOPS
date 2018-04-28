package com.iv.tenant.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.dto.IdListDto;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.tenant.api.constant.ErrorMsg;
import com.iv.tenant.api.dto.ProcessDataDto;
import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.api.dto.SubEnterpriseInfoDto;
import com.iv.tenant.api.dto.SubTenantInfoDto;
import com.iv.tenant.api.dto.TenantApproveReq;
import com.iv.tenant.api.dto.TenantInfoDto;
import com.iv.tenant.api.service.ITenantFacadeService;
import com.iv.tenant.service.TenantService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * rest api 租户管理
 * @author macheng
 * 2018年4月4日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@RestController
@Api(description = "租户管理系列接口")
public class TenantController implements ITenantFacadeService{

	@Autowired
	private TenantService service;
	private static final Logger LOGGER = LoggerFactory.getLogger(TenantController.class);

	@ApiOperation("获取已注册的企业主体信息")
	@Override
	public ResponseDto getEnterNameList() {
		ResponseDto response = null;
		try {
			response = new ResponseDto();
			response.setData(service.getEnterprisesList());
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取企业名称列表失败", e);
			return ResponseDto.builder(ErrorMsg.ENTER_LIST_GET_FAILED);
		}
	}

	@ApiOperation("获取企业信息")
	@Override
	public ResponseDto getTenant(@RequestBody QueryEnterReq req) {
		ResponseDto response = null;
		try {
			response = new ResponseDto();
			response.setData(service.getTenant(req));
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取租户信息失败", e);
			return ResponseDto.builder(ErrorMsg.ENTER_LIST_GET_FAILED);
		}
	}

	/*@ApiOperation("查询用户信息")
	//@PreAuthorize("hasAnyRole('administrator','admin','creator')")
	@PostMapping("/get/userInfo")
	public ResponseDto getUserId(@RequestBody QueryUserId query) {
		ResponseDto response = null;
		try {
			response = new ResponseDto();
			response.setData(service.getUserId(query));
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取用户信息失败", e);
			return ResponseDto.builder(ErrorMsg.USERS_LIST_GET_FAILED);
		}
	}*/

	@ApiOperation("获取项目组信息")
	//@PreAuthorize("hasAnyRole('administrator','admin','creator')")
	@Override
	public ResponseDto getTenantByTenantId(HttpServletRequest request) {
		ResponseDto response = null;
		try {
			response = new ResponseDto();
			String tenantId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("curTenantId");
			response.setData(service.getTenantByTenantId(tenantId));
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取项目组信息失败", e);
			return ResponseDto.builder(ErrorMsg.GET_SUBTENANT_INFO_FAILED);
		}
	}

	/**
	 * 申请创建租户
	 * 
	 * @param dto
	 * @return
	 */
	@ApiOperation("申请创建租户")
	@Override
	public ResponseDto newTenantApply(@RequestParam("request") HttpServletRequest request, @RequestBody TenantInfoDto dto) {
		//request.setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID);
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			return service.applyNewTenant(userId, dto);
		} catch (Exception e) {
			LOGGER.error("申请租户失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.TENANT_CREATE_FAILED);
			return response;
		}
	}

	@ApiOperation("《管理员》申请流审批")
	//@PreAuthorize("hasAnyRole('administrator','admin','creator')")
	@Override
	public ResponseDto taskApprove(@RequestParam("request") HttpServletRequest request, @RequestBody TenantApproveReq dto) {
		//request.setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID);
		ResponseDto response = null;
		try {
			service.taskApprove(dto.getTaskId(), dto.isApproved(), dto.getRemark());
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("审批租户失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.TENANT_APPROVE_FAILED);
			return response;
		}
	}

	@ApiOperation("《管理员》获取待审批的租户创建申请")
	//@PreAuthorize("hasAnyRole('administrator','admin')")
	@Override
	public ResponseDto getUserTasksRegis(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max) {
		//request.setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID);
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto tasks = service.getUserTasksRegis(userId, first, max);
			response = new ResponseDto();
			response.setData(tasks);
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取审批任务失败", e);
			response = new ResponseDto();
			response.setErrorMsg(com.iv.common.response.ErrorMsg.GET_DATA_FAILED);
			return response;
		}
	}

	@ApiOperation("获取租户创建申请的历史工作流")
	@Override
	public ResponseDto getHistoryProcessRegis(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max) {
		//request.setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID);
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto data = service.getNewTenantHisPro(userId, first, max);
			response = new ResponseDto();
			response.setData(data);
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取历史工作流失败", e);
			response = new ResponseDto();
			response.setErrorMsg(com.iv.common.response.ErrorMsg.GET_DATA_FAILED);
			return response;
		}
	}

	@ApiOperation("申请加入项目组")
	@Override
	public ResponseDto applyJoinTenant(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "tenantId", required = true) String tenantId) {
		//request.setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID);
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			return service.applyJoinTenant(userId, tenantId);

		} catch (Exception e) {
			LOGGER.error("添加用户失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.APPLY_JOIN_ENTER_FAILED);
			return response;
		}
	}

	@ApiOperation("《管理员》获取待审批的加入租户申请")
	//@PreAuthorize("hasAnyRole('administrator','admin','creator')")
	@Override
	public ResponseDto getUserTasksJoin(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max) {
		//request.setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID);
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto tasks = service.getUserTasksJoin(userId, first, max);
			response = new ResponseDto();
			response.setData(tasks);
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取待审批任务失败", e);
			response = new ResponseDto();
			response.setErrorMsg(com.iv.common.response.ErrorMsg.GET_DATA_FAILED);
			return response;
		}
	}

	@ApiOperation("获取加入租户申请的历史工作流")
	@Override
	public ResponseDto getHistoryProcessJoin(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "first", required = true) int first,
			@PathVariable(value = "max", required = true) int max) {
		//request.setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID);
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto data = service.getJoinTenantHisPro(userId, first, max);
			response = new ResponseDto();
			response.setData(data);
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取历史工作流失败", e);
			response = new ResponseDto();
			response.setErrorMsg(com.iv.common.response.ErrorMsg.GET_DATA_FAILED);
			return response;
		}
	}

	@ApiOperation("《管理员》项目组管理员手动添加用户")
	//@PreAuthorize("hasRole('admin')")
	public ResponseDto manuallyAddUser(HttpServletRequest request, @RequestBody IdListDto userIds) {

		try {
			String tenantId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("curTenantId");
			return service.manuallyAddUser(tenantId, userIds.getIds());
		} catch (Exception e) {
			LOGGER.error("添加用户失败", e);
			return ResponseDto.builder(ErrorMsg.TENANT_USERADD_FAILED);
		}
	}

	@ApiOperation("用户切换租户")
	public ResponseDto switchTenant(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "tenantId", required = true) String tenantId) {

		ResponseDto response = null;
		try {
			return service.switchTenant(tenantId, request);
		} catch (Exception e) {
			LOGGER.error("切换租户失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.SWITCH_TENANT_FAILED);
			return response;
		}

	}

	@ApiOperation("创建租户下项目组")
	//@PreAuthorize("hasAnyRole('admin','creator')")
	public ResponseDto createSubTenant(HttpServletRequest request, @RequestBody SubTenantInfoDto dto) {
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			return service.createSubTenant(userId, dto.getName(), dto.getUserIds());
		} catch (Exception e) {
			LOGGER.error("创建项目组失败", e);
			return ResponseDto.builder(ErrorMsg.CREATE_SUBTENANT_FAILED);
		}
	}

	/*@ApiOperation("《管理员》管理员手动添加用户至项目组")
	//@PreAuthorize("hasAnyRole('admin','creator')")
	public ResponseDto manuallyAddUserToSubTenant(HttpServletRequest request, @RequestBody IdListDto userIds) {

		try {
			String subTenantId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("curTenantId");
			service.manuallyAddUserToSubTenant(subTenantId, userIds.getIds());
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("添加用户失败", e);
			return ResponseDto.builder(ErrorMsg.TENANT_USERADD_FAILED);
		}
	}*/

	@ApiOperation("《管理员》删除项目组")
	//@PreAuthorize("hasAnyRole('admin')")
	public ResponseDto deleteSubTenant(@RequestParam("request") HttpServletRequest request,
			@PathVariable(value = "id", required = true) int id) {
		try {
			service.deleteSubTenant(id, request);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("删除项目组失败", e);
			return ResponseDto.builder(ErrorMsg.DEL_SUBTENANT_FAILED);
		}
	}

	@ApiOperation("获取所有项目组信息")
	@Override
	public List<SubEnterpriseInfoDto> getSubEnterpriseAll() {
		try {
			return service.getSubEnterpriseAll();
		} catch (Exception e) {
			LOGGER.error("获取项目组列表失败", e);
			return null;
		}
	}

}
