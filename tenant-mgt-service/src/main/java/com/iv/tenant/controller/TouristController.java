package com.iv.tenant.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.common.util.spring.JWTUtil;
import com.iv.tenant.api.constant.ErrorMsg;
import com.iv.tenant.api.dto.ProcessDataDto;
import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.api.dto.TenantInfoDto;
import com.iv.tenant.service.TenantService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * rest api 游客访问
 * @author macheng
 * 2018年4月4日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@RestController
@RequestMapping("/tourist")
@Api(description = "游客访问接口")
public class TouristController {
	
	@Autowired
	private TenantService tenantService;
	private static final Logger LOGGER = LoggerFactory.getLogger(TouristController.class);
	
	/*@ApiOperation("密码找回")	
	@PostMapping("/resetPassword")
	@ResponseBody
	public ResponseDto resetPassword(HttpSession session,@RequestBody UserSafeDto userInfo) {
		ResponseDto dto = null;
		try {
			return commonService.userInfo(session,userInfo);
		}catch (Exception e) {
			dto = new ResponseDto();
			LOGGER.error("系统错误:用户找回密码失败", e);
			dto.setErrorMsg(ErrorMsg.UNKNOWN);
			return dto;
		}				
	}*/
	
	@ApiOperation("获取租户信息")
	@PostMapping("/tenant/get/targetEnter")
	public ResponseDto getTenant(@RequestBody QueryEnterReq req) {
		ResponseDto response = null;
		try {
			response = new ResponseDto();
			response.setData(tenantService.getTenant(req));
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取租户信息失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.TENANT_INFO_GET_FAILED);
			return response;
		}
	}
	
	/**
	 * 申请创建租户
	 * 
	 * @param dto
	 * @return
	 */
	@ApiOperation("申请创建租户")
	@PostMapping("/tenant/mgt/apply/regis")
	public ResponseDto newTenantApply(@RequestBody TenantInfoDto dto, HttpServletRequest request) {
		request.setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID);
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			return tenantService.applyNewTenant(userId, dto);
		} catch (Exception e) {
			LOGGER.error("申请租户失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.TENANT_CREATE_FAILED);
			return response;
		}
	}

	@ApiOperation("申请加入租户")
	@GetMapping("/tenant/mgt/apply/join/{tenantId}")
	public ResponseDto applyJoinTenant(@PathVariable(required = true) String tenantId, HttpServletRequest request) {
		request.setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID);
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			return tenantService.applyJoinTenant(userId, tenantId);
			
		} catch (Exception e) {
			LOGGER.error("添加用户失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.TENANT_USERADD_FAILED);
			return response;
		}
	}
	
	@ApiOperation("获取租户创建申请的历史工作流")
	@GetMapping("/tenant/mgt/hisFlow/regis/{first}/{max}")
	public ResponseDto getHistoryProcessRegis(@PathVariable(required = true) int first,
			@PathVariable(required = true) int max, HttpServletRequest request) {
		request.setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID);
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto data = tenantService.getNewTenantHisPro(userId, first, max);
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
	
	@ApiOperation("获取加入租户申请的历史工作流")
	@GetMapping("/tenant/mgt/hisFlow/join/{first}/{max}")
	public ResponseDto getHistoryProcessJoin(@PathVariable(required = true) int first,
			@PathVariable(required = true) int max, HttpServletRequest request) {
		request.setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID);
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto data = tenantService.getJoinTenantHisPro(userId, first, max);
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
	
	/**
	 * 查询用户信息
	 * 
	 * @param session
	 * @return
	 */
	/*@ApiOperation("查询用户信息")
	@GetMapping("/user/info")
	public ResponseDto getUserInfo(HttpSession session) {

		ResponseDto dto = new ResponseDto();
		try {
			LocalAuthDetails user = (LocalAuthDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			dto.setData(commonService.getUserInfo(user.getId()));
			dto.setErrorMsg(ErrorMsg.OK);
			return dto;
		} catch (Exception e) {
			LOGGER.error("系统内部错误", e);
			dto.setErrorMsg(ErrorMsg.UNKNOWN);
			return dto;
		}

	}*/
	
	@ApiOperation("用户切换租户")
	@GetMapping("/tenant/mgt/switch/{tenantId}")
	public ResponseDto switchTenant(@PathVariable(required = true) String tenantId, HttpServletRequest request) {
		
		ResponseDto response = null;
		try {
			tenantService.switchTenant(tenantId, request);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("切换租户失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.SWITCH_TENANT_FAILED);
			return response;
		}
		
	}
	
}
