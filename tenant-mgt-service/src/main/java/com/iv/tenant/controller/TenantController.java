package com.iv.tenant.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.iv.tenant.api.dto.TenantApproveReq;
import com.iv.tenant.api.dto.TenantInfoDto;
import com.iv.tenant.api.dto.UserListDto;
import com.iv.tenant.api.service.ITenantFacadeService;
import com.iv.tenant.entity.SubEnterpriseEntity;
import com.iv.tenant.service.TenantService;
import com.iv.tenant.util.Constant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 租户管理
 * 
 * @author macheng 2018年4月4日 alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@RestController
@Api(description = "租户管理系列接口")
public class TenantController implements ITenantFacadeService {

	@Autowired
	private TenantService service;
	private static final Logger LOGGER = LoggerFactory.getLogger(TenantController.class);

	@ApiOperation(value = "企业信息查询（所有）", notes = "82301")
	@Override
	public ResponseDto getEnterprisesAll() {
		ResponseDto response = null;
		try {
			response = new ResponseDto();
			response.setData(service.getEnterprisesAll());
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取企业名称列表失败", e);
			return ResponseDto.builder(ErrorMsg.ENTER_LIST_GET_FAILED);
		}
	}

	@ApiOperation(value = "企业信息查询（条件查询）", notes = "82302")
	@Override
	public ResponseDto getEnterprisesCondition(@RequestBody QueryEnterReq req) {
		ResponseDto response = null;
		try {
			response = new ResponseDto();
			response.setData(service.getEnterprisesCondition(req));
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取企业信息失败", e);
			return ResponseDto.builder(ErrorMsg.ENTER_LIST_GET_FAILED);
		}
	}

	/*
	 * @ApiOperation("获取指定企业信息及其下面的项目组列表")
	 * 
	 * @GetMapping("/get/enterprise/name") public ResponseDto
	 * getEnterprisesByName(@RequestParam String name) { ResponseDto response =
	 * null; try { response = new ResponseDto();
	 * response.setData(service.getEnterpriseWithTenant(name));
	 * response.setErrorMsg(ErrorMsg.OK); return response; } catch (Exception e) {
	 * LOGGER.error("获取企业信息失败", e); return
	 * ResponseDto.builder(ErrorMsg.ENTER_LIST_GET_FAILED); } }
	 */

	@ApiOperation(value = "当前项目组信息查询", notes = "82303")
	@GetMapping("/get/tenant/current")
	public ResponseDto getCurrentSubEnterprise(HttpServletRequest request) {
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

	@ApiIgnore
	@ApiOperation(value = "当前项目组信息查询", notes = "82306")
	@Override
	public SubEnterpriseInfoDto getCurrentSubEnterpriseBack(HttpServletRequest request) {
		try {
			String tenantId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("curTenantId");
			return service.getTenantByTenantId(tenantId);
		} catch (Exception e) {
			LOGGER.error("获取项目组信息失败", e);
			return null;
		}
	}

	@ApiIgnore
	@ApiOperation(value = "当前项目组人员列表查询", notes = "82317")
	@Override
	public UserListDto getCurrentSubEnterpriseUserList(HttpServletRequest request, int page, int items) {
		try {
			String tenantId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("curTenantId");
			return service.getUsersByTenantId(tenantId, page, items);
		} catch (Exception e) {
			LOGGER.error("获取项目组成员失败", e);
			return null;
		}
	}

	@ApiIgnore
	@ApiOperation(value = "查询所有项目组信息", notes = "82304")
	@Override
	public List<SubEnterpriseInfoDto> getSubEnterpriseAll() {
		try {
			return service.getSubEnterpriseAll();
		} catch (Exception e) {
			LOGGER.error("获取项目组列表失败", e);
			return null;
		}
	}

	@ApiOperation(value = "项目组信息查询（条件查询）", notes = "82305")
	@GetMapping("/get/tenant/conditions")
	public ResponseDto getSubEnterpriseCondition(@RequestParam String condition) {
		try {
			List<SubEnterpriseEntity> subEnterprise = service.getSubEnterpriseCondition(condition);
			ResponseDto dto = ResponseDto.builder(ErrorMsg.OK);
			dto.setData(subEnterprise);
			return dto;
		} catch (Exception e) {
			LOGGER.error("获取项目组列表失败", e);
			return ResponseDto.builder(ErrorMsg.GET_SUBTENANT_INFO_FAILED);
		}
	}

	@ApiOperation(value = "用户所加入的项目组列表查询", notes = "82307")
	@GetMapping("/get/tenant/joined")
	public ResponseDto getTenantsJoined(HttpServletRequest request) {
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			List<SubEnterpriseEntity> subEnterprise = service.getTenantsJoined(userId);
			ResponseDto dto = ResponseDto.builder(ErrorMsg.OK);
			dto.setData(subEnterprise);
			return dto;
		} catch (Exception e) {
			LOGGER.error("获取项目组列表失败", e);
			return ResponseDto.builder(ErrorMsg.GET_SUBTENANT_INFO_FAILED);
		}
	}

	/**
	 * 申请创建租户
	 * 
	 * @param dto
	 * @return
	 */
	/*
	 * @ApiOperation("申请创建项目组")
	 * 
	 * @GetMapping("/apply/new/tenant") public ResponseDto
	 * applyNewTenant(HttpServletRequest request, @RequestParam int enterpriseId,
	 * 
	 * @RequestParam String tenantName) { ResponseDto response = null; try { int
	 * userId =
	 * JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
	 * return service.applyNewTenant(userId, enterpriseId, tenantName); } catch
	 * (Exception e) { LOGGER.error("申请租户失败", e); response = new ResponseDto();
	 * response.setErrorMsg(ErrorMsg.TENANT_CREATE_FAILED); return response; } }
	 */

	@ApiOperation(value = "项目组创建申请", notes = "82308")
	@PostMapping("/apply/regis/tenant")
	public ResponseDto applyNewTenant(HttpServletRequest request, @RequestBody TenantInfoDto dto) {
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

	@ApiOperation(value = "项目组加入申请", notes = "82309")
	@GetMapping("/apply/join/tenant")
	public ResponseDto applyJoinTenant(HttpServletRequest request,
			@RequestParam(value = "tenantId", required = true) String tenantId) {
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

	/*
	 * @ApiOperation("《管理员》创建项目组") public ResponseDto
	 * createTenant(HttpServletRequest request, @RequestBody SubTenantInfoDto dto) {
	 * try { int userId =
	 * JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
	 * return service.createSubTenant(userId, dto.getName(), dto.getUserIds()); }
	 * catch (Exception e) { LOGGER.error("创建项目组失败", e); return
	 * ResponseDto.builder(ErrorMsg.CREATE_SUBTENANT_FAILED); } }
	 */

	/*
	 * @ApiOperation("《管理员》删除项目组")
	 * 
	 * @GetMapping("/delete/tenant") public ResponseDto
	 * deleteTenant(HttpServletRequest request, @RequestParam(required = true) int
	 * id,
	 * 
	 * @RequestParam(required = true) String refreshToken) { try {
	 * 
	 * service.deleteSubTenant(id, request.getHeader("Authorization"),
	 * refreshToken); return ResponseDto.builder(ErrorMsg.OK); } catch (Exception e)
	 * { LOGGER.error("删除项目组失败", e); return
	 * ResponseDto.builder(ErrorMsg.DEL_SUBTENANT_FAILED); } }
	 */

	@ApiOperation(value = "用户添加", notes = "82310")
	@PostMapping("/add/user")
	public ResponseDto manuallyAddUser(HttpServletRequest request, @RequestBody IdListDto userIds) {

		try {
			String tenantId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("curTenantId");
			return service.manuallyAddUser(tenantId, userIds.getIds());
		} catch (Exception e) {
			LOGGER.error("添加用户失败", e);
			return ResponseDto.builder(ErrorMsg.TENANT_USERADD_FAILED);
		}
	}

	@ApiOperation(value = "申请流审批", notes = "82311")
	@PostMapping("/approve")
	public ResponseDto taskApprove(HttpServletRequest request, @RequestBody TenantApproveReq dto) {
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			service.taskApprove(userId, dto.getTaskId(), dto.isApproved(), dto.getRemark());
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

	/*
	 * @ApiOperation("《项目组管理员》获取待审批的项目组创建申请")
	 * 
	 * @GetMapping("/get/pending/regis/tenant/{first}/{max}") public ResponseDto
	 * getUserTasksTenantRegis(HttpServletRequest request,
	 * 
	 * @PathVariable(value = "first", required = true) int first,
	 * 
	 * @PathVariable(value = "max", required = true) int max) { ResponseDto response
	 * = null; try { int userId =
	 * JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
	 * ProcessDataDto tasks = service.getUserTasksTenantRegis(userId, first, max);
	 * response = new ResponseDto(); response.setData(tasks);
	 * response.setErrorMsg(ErrorMsg.OK); return response; } catch (Exception e) {
	 * LOGGER.error("获取审批任务失败", e); response = new ResponseDto();
	 * response.setErrorMsg(com.iv.common.response.ErrorMsg.GET_DATA_FAILED); return
	 * response; } }
	 */

	@ApiOperation(value = "我的申请-项目组创建申请", notes = "82313")
	@GetMapping("/get/hisFlow/apply/regis")
	public ResponseDto getMyApplyRegis(HttpServletRequest request,
			@RequestParam(value = "curPage", required = true) int curPage,
			@RequestParam(value = "items", required = true) int items) {
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto data = service.getMyApplyRegis(userId, (curPage - 1) * items, items);
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

	@ApiOperation(value = "我的申请-项目组加入申请", notes = "82315")
	@GetMapping("/get/hisFlow/apply/join")
	public ResponseDto getMyApplyJoin(HttpServletRequest request,
			@RequestParam(value = "curPage", required = true) int curPage,
			@RequestParam(value = "items", required = true) int items) {
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto data = service.getMyApplyJoin(userId, (curPage - 1) * items, items);
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

	/*
	 * @ApiOperation("《项目组管理员》获取项目组创建申请的历史工作流")
	 * 
	 * @GetMapping("/get/hisFlow/regis/tenant/{first}/{max}") public ResponseDto
	 * getHistoryTenantRegis(HttpServletRequest request,
	 * 
	 * @PathVariable(value = "first", required = true) int first,
	 * 
	 * @PathVariable(value = "max", required = true) int max) { ResponseDto response
	 * = null; try { int userId =
	 * JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
	 * ProcessDataDto data = service.getHistoryTenantRegis(userId, first, max);
	 * response = new ResponseDto(); response.setData(data);
	 * response.setErrorMsg(ErrorMsg.OK); return response; } catch (Exception e) {
	 * LOGGER.error("获取历史工作流失败", e); response = new ResponseDto();
	 * response.setErrorMsg(com.iv.common.response.ErrorMsg.GET_DATA_FAILED); return
	 * response; } }
	 */

	@ApiOperation(value = "我的审批-待审批-项目组创建申请", notes = Constant.TENANT_REGIS_CODE)
	@GetMapping("/get/pending/regis")
	public ResponseDto getMyApprovingRegis(HttpServletRequest request,
			@RequestParam(value = "curPage", required = true) int curPage,
			@RequestParam(value = "items", required = true) int items) {
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto tasks = service.getMyApprovingRegis(userId, (curPage - 1) * items, items);
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

	@ApiOperation(value = "我的审批-待审批-项目组加入申请", notes = Constant.JOIN_TASKT_CODE)
	@GetMapping("/get/pending/join")
	public ResponseDto getMyApprovingJoin(HttpServletRequest request,
			@RequestParam(value = "curPage", required = true) int curPage,
			@RequestParam(value = "items", required = true) int items) {
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto tasks = service.getMyApprovingJoin(userId, (curPage - 1) * items, items);
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

	@ApiOperation(value = "我的审批-已审批-项目组创建申请", notes = Constant.TENANT_REGIS_CODE)
	@GetMapping("/get/hisFlow/approve/regis")
	public ResponseDto getMyApprovedRegis(HttpServletRequest request, @RequestParam("curPage") int curPage,
			@RequestParam("items") int items) {
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto tasks = service.getMyApprovedRegis(userId, (curPage - 1) * items, items);
			response = new ResponseDto();
			response.setData(tasks);
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取审批历史失败", e);
			response = new ResponseDto();
			response.setErrorMsg(com.iv.common.response.ErrorMsg.GET_DATA_FAILED);
			return response;
		}
	}

	@ApiOperation(value = "我的审批-已审批-项目组加入申请", notes = Constant.JOIN_TASKT_CODE)
	@GetMapping("/get/hisFlow/approve/join")
	public ResponseDto getMyApprovedJoin(HttpServletRequest request, @RequestParam("curPage") int curPage,
			@RequestParam("items") int items) {
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto tasks = service.getMyApprovedJoin(userId, (curPage - 1) * items, items);
			response = new ResponseDto();
			response.setData(tasks);
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取审批历史失败", e);
			response = new ResponseDto();
			response.setErrorMsg(com.iv.common.response.ErrorMsg.GET_DATA_FAILED);
			return response;
		}
	}

	@ApiOperation(value = "租户切换", notes = "82316")
	@GetMapping("/switch")
	public ResponseDto switchTenant(HttpServletRequest request, @RequestParam(required = true) String tenantId,
			@RequestParam(required = true) String refreshToken) {

		ResponseDto response = null;
		try {
			return service.switchTenant(tenantId, refreshToken, request.getHeader("Authorization"));
		} catch (Exception e) {
			LOGGER.error("切换租户失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.SWITCH_TENANT_FAILED);
			return response;
		}

	}

	@Override
	public SubEnterpriseInfoDto getSubEnterpriseByTenantIdBack(String tenantId) {
		
		try {
			return service.getSubEnterpriseByTenantIdBack(tenantId);
		} catch (Exception e) {
			LOGGER.error("获取项目组信息失败", e);
			return null;
		}

	}

}
