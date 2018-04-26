package com.iv.message.api.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.enumeration.WorkflowType;
import com.iv.common.response.ResponseDto;
import com.iv.message.api.dto.AlarmMsgDto;

public interface IMessageService {

	/**
	 * 生产待审批的企业注册申请消息
	 * @param applicant
	 * @param approverId
	 * @param enterpriseName
	 * @param workflowType
	 * @return
	 */
	@RequestMapping(value = "/produce/approve", method = RequestMethod.GET)
	ResponseDto produceApproveMsg(@RequestParam("applicant")String applicant, 
			@RequestParam("approverId")int approverId, @RequestParam("enterpriseName")String enterpriseName,
			@RequestParam("workflowType")WorkflowType workflowType);
	
	/**
	 * 生产审批结果消息
	 * @param applicant
	 * @param userId
	 * @param approved
	 * @param enterpriseName
	 * @param remark
	 * @param workflowType
	 * @return
	 */
	@RequestMapping(value = "/produce/apply", method = RequestMethod.GET)
	ResponseDto produceApplyMsg(@RequestParam("userId")int userId, @RequestParam("approved")boolean approved,
			@RequestParam("enterpriseName")String enterpriseName, @RequestParam("remark")String remark,
			@RequestParam("workflowType")WorkflowType workflowType);
	
	/**
	 * 生产告警通知消息
	 * @param alarmMsgDto
	 * @return
	 */
	@RequestMapping(value = "/produce/alarm", method = RequestMethod.POST)
	ResponseDto produceAlarmMsg(@RequestBody AlarmMsgDto alarmMsgDto);
	
	
}
