package com.iv.message.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iv.common.enumeration.WorkflowType;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.message.api.constant.ErrorMsg;
import com.iv.message.api.constant.MsgSysType;
import com.iv.message.api.dto.AlarmMsgDto;
import com.iv.message.api.service.IMessageService;
import com.iv.message.service.MsgCenterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "消息中心系列接口")
public class MsgCenterController implements IMessageService {

	private final static Logger LOGGER = LoggerFactory.getLogger(MsgCenterController.class);
	@Autowired
	private MsgCenterService service;

	@ApiOperation("获取监控系统消息")
	@GetMapping("/monitorSys")
	public ResponseDto monitorSys(HttpServletRequest request, @RequestParam(required = true) MsgSysType type,
			@RequestParam(required = true) int page, @RequestParam(required = true) int num) {
		ResponseDto dto = null;
		try {
			dto = new ResponseDto();
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			dto.setData(service.monitorSys(userId, type, page, num));
			dto.setErrorMsg(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("获取消息失败", e);
			dto = new ResponseDto();
			dto.setErrorMsg(ErrorMsg.GET_SYS_MESSAGE_FAILED);
		}
		return dto;
	}

	@ApiOperation("获取租户管理系统消息")
	@GetMapping("/tenantSys")
	public ResponseDto tenantSys(HttpServletRequest request, @RequestParam(required = true) MsgSysType type,
			@RequestParam(required = true) int page, @RequestParam(required = true) int num) {
		ResponseDto dto = null;
		try {
			dto = new ResponseDto();
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			dto.setData(service.tenantSys(userId, type, page, num));
			dto.setErrorMsg(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("获取消息失败", e);
			dto = new ResponseDto();
			dto.setErrorMsg(ErrorMsg.GET_SYS_MESSAGE_FAILED);
		}
		return dto;
	}

	@ApiOperation("获取运营运维管理系统消息")
	@GetMapping("/operationSys")
	public ResponseDto operationSys(HttpServletRequest request, @RequestParam(required = true) MsgSysType type,
			@RequestParam(required = true) int page, @RequestParam(required = true) int num) {
		ResponseDto dto = null;
		try {
			dto = new ResponseDto();
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			dto.setData(service.operationSys(userId, type, page, num));
			dto.setErrorMsg(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("获取消息失败", e);
			dto = new ResponseDto();
			dto.setErrorMsg(ErrorMsg.GET_SYS_MESSAGE_FAILED);
		}
		return dto;
	}

	@ApiOperation("清除消息")
	@GetMapping("/clear")
	public ResponseDto clear(HttpServletRequest request, @RequestParam(required = true) MsgSysType type,
			@RequestParam(required = false) String id) {
		ResponseDto dto = null;
		try {
			dto = new ResponseDto();
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			if (StringUtils.isEmpty(id)) {
				service.clear(userId, type);
			} else {
				service.clearWithId(type, id);
			}
			dto.setErrorMsg(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("清除消息失败", e);
			dto = new ResponseDto();
			dto.setErrorMsg(ErrorMsg.CLEAR_MSG_FAILED);
		}
		return dto;
	}

	@ApiOperation("生产待审批的申请消息")
	@Override
	public ResponseDto produceApproveMsg(String applicant, int approverId, String subEnterprise, String enterpriseName,
			WorkflowType workflowType) {
		try {
			service.produceApproveFlowMsg(applicant, approverId, subEnterprise, enterpriseName, workflowType);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("消息生成失败", e);
			return ResponseDto.builder(ErrorMsg.PRODUCE_MSG_FAILED);
		}
	}

	@ApiOperation("生产审批结果消息")
	@Override
	public ResponseDto produceApplyMsg(int userId, boolean approved, String subEnterpriseName, String enterpriseName, String remark,
			WorkflowType workflowType) {
		try {
			service.produceApplyFlowMsg(userId, approved, subEnterpriseName, enterpriseName, remark, workflowType);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("消息生成失败", e);
			return ResponseDto.builder(ErrorMsg.PRODUCE_MSG_FAILED);
		}
	}

	@Override
	@ApiOperation("生产告警消息")
	public ResponseDto produceAlarmMsg(@RequestBody AlarmMsgDto alarmMsgDto) {

		try {
			service.produceAlarmMsg(alarmMsgDto);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("消息生成失败", e);
			return ResponseDto.builder(ErrorMsg.PRODUCE_MSG_FAILED);
		}
	}
}
