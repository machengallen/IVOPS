package com.iv.message.service;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iv.common.enumeration.WorkflowType;
import com.iv.message.api.constant.MsgSysType;
import com.iv.message.api.dto.AlarmMsgDto;
import com.iv.message.api.dto.MonitorSysMsgDto;
import com.iv.message.api.dto.OperationSysMsgDto;
import com.iv.message.api.dto.TenantSysMsgDto;
import com.iv.message.dao.impl.AlarmMsgDaoImpl;
import com.iv.message.dao.impl.ApplyFlowMsgDaoImpl;
import com.iv.message.dao.impl.ApproveFlowMsgDaoImpl;
import com.iv.message.entity.AlarmMsgEntity;
import com.iv.message.entity.ApplyFlowMsgEntity;
import com.iv.message.entity.ApproveFlowMsgEntity;


/**
 * 用户系统消息服务
 * 
 * @author macheng 2018年1月26日 aggregation-1.3.0-SNAPSHOT
 * 
 */
@Service
public class MsgCenterService {

	private final static Logger LOGGER = LoggerFactory.getLogger(MsgCenterService.class);
	@Autowired
	private ApproveFlowMsgDaoImpl approveFlowMsgDaoImpl;
	@Autowired
	private ApplyFlowMsgDaoImpl applyFlowMsgDaoImpl;
	@Autowired
	private AlarmMsgDaoImpl alarmMsgDaoImpl;

	/**
	 * @param type ALARM:告警通知，APPLY：审批结果通知，ALL：所有通知
	 * @param page
	 * @param num 参数为-1则查询所有
	 * @return
	 */
	public MonitorSysMsgDto monitorSys(int userId, MsgSysType type, int page, int num) {

		int firstResult = (page - 1) * num;
		int maxResults = num;
		MonitorSysMsgDto msgDto = new MonitorSysMsgDto();

		switch (type) {
		case ALARM:
			msgDto.setAlarmMsg(alarmMsgDaoImpl.selectAllByUserId(userId, firstResult, maxResults));
			break;
		case APPLY:
			msgDto.setApplyFlowMsg(applyFlowMsgDaoImpl.selectByUserId(userId, firstResult, maxResults));
			break;
		case ALL:
			msgDto.setApplyFlowMsg(applyFlowMsgDaoImpl.selectByUserId(userId, firstResult, maxResults));
			msgDto.setAlarmMsg(alarmMsgDaoImpl.selectAllByUserId(userId, firstResult, maxResults));
			msgDto.setAllTotal();
			break;
		default:
			break;
		}
		return msgDto;
	}

	/**
	 * @param type ALARM:告警通知，APPLY：审批结果通知，ALL：所有通知
	 * @param page
	 * @param num 参数为-1则查询所有
	 * @return
	 */
	public TenantSysMsgDto tenantSys(int userId, MsgSysType type, int page, int num) {
		int firstResult = (page - 1) * num;
		int maxResults = num;
		TenantSysMsgDto msgDto = new TenantSysMsgDto();
		
		switch (type) {
		case APPLY:
			msgDto.setApplyFlowMsg(applyFlowMsgDaoImpl.selectByUserId(userId, firstResult, maxResults));
			break;
		case APPROVE:
			msgDto.setApproveFlowMsg(approveFlowMsgDaoImpl.selectByUserId(userId, firstResult, maxResults));
			break;
		case ALL:
			msgDto.setApplyFlowMsg(applyFlowMsgDaoImpl.selectByUserId(userId, firstResult, maxResults));
			msgDto.setApproveFlowMsg(approveFlowMsgDaoImpl.selectByUserId(userId, firstResult, maxResults));
			msgDto.setAllTotal();
			break;
		default:
			break;
		}
		return msgDto;
	}

	/**
	 * @param type ALARM:告警通知，APPLY：审批结果通知，ALL：所有通知
	 * @param page
	 * @param num 参数为-1则查询所有
	 * @return
	 */
	public OperationSysMsgDto operationSys(int userId, MsgSysType type, int page, int num) {
		int firstResult = (page - 1) * num;
		int maxResults = num;
		OperationSysMsgDto msgDto = new OperationSysMsgDto();
		
		switch (type) {
		case APPROVE:
			msgDto.setApproveFlowMsg(approveFlowMsgDaoImpl.selectByUserId(userId, firstResult, maxResults));
			break;
		case ALL:
			msgDto.setApproveFlowMsg(approveFlowMsgDaoImpl.selectByUserId(userId, firstResult, maxResults));
			msgDto.setAllTotal();
			break;
		default:
			break;
		}
		return msgDto;
	}

	public void clear(int userId, MsgSysType type) {
		switch (type) {
		case ALARM:
			alarmMsgDaoImpl.updateConfirmedAllByUserId(userId, true);
			break;
		case APPLY:
			applyFlowMsgDaoImpl.updateConfirmedAllByUserId(userId, true);
			break;
		case APPROVE:
			approveFlowMsgDaoImpl.updateConfirmedAllByUserId(userId, true);
			break;
		case ALL:
			alarmMsgDaoImpl.updateConfirmedAllByUserId(userId, true);
			applyFlowMsgDaoImpl.updateConfirmedAllByUserId(userId, true);
			approveFlowMsgDaoImpl.updateConfirmedAllByUserId(userId, true);
			break;

		default:
			break;
		}

	}
	
	public void clearWithId(MsgSysType type, String id) {
		switch (type) {
		case ALARM:
			alarmMsgDaoImpl.updateConfirmed(Arrays.asList(id), true);
			break;
		case APPLY:
			applyFlowMsgDaoImpl.updateConfirmed(Arrays.asList(id), true);
			break;
		case APPROVE:
			approveFlowMsgDaoImpl.updateConfirmed(Arrays.asList(id), true);
			break;

		default:
			break;
		}
	}

	/**
	 * 生产待审批流消息体
	 * 
	 * @param applicant
	 * @param approverId
	 * @param enterprise
	 * @param workflowType
	 */
	public void produceApproveFlowMsg(String applicant, int approverId, String subEnterprise, String enterprise, WorkflowType workflowType) {

		try {
			long time = System.currentTimeMillis();
			ApproveFlowMsgEntity msgEntity = new ApproveFlowMsgEntity();
			msgEntity.setApplicant(applicant);
			msgEntity.setApplyTime(time);
			msgEntity.setConfirmed(false);
			msgEntity.setEnterprise(enterprise);
			msgEntity.setSubEnterprise(subEnterprise);
			msgEntity.setMsgDate(time);
			msgEntity.setType(workflowType);
			msgEntity.setUserId(approverId);
			approveFlowMsgDaoImpl.save(msgEntity);

		} catch (Exception e) {
			LOGGER.error("用户消息生产-审批提示-失败", e);
		}
	}

	/**
	 * 生产审批后通知消息体
	 * 
	 * @param userId
	 * @param approved
	 * @param enterprise
	 * @param remark
	 * @param workflowType
	 */
	public void produceApplyFlowMsg(int userId, boolean approved, String subEnterpriseName, String enterprise, String remark,
			WorkflowType workflowType) {

		try {
			long time = System.currentTimeMillis();
			ApplyFlowMsgEntity msgEntity = new ApplyFlowMsgEntity();
			msgEntity.setApproved(approved);
			msgEntity.setApproveTime(time);
			msgEntity.setConfirmed(false);
			msgEntity.setEnterprise(enterprise);
			msgEntity.setSubEnterprise(subEnterpriseName);
			msgEntity.setMsgDate(time);
			msgEntity.setRemark(remark);
			msgEntity.setType(workflowType);
			msgEntity.setUserId(userId);
			applyFlowMsgDaoImpl.save(msgEntity);

		} catch (Exception e) {
			LOGGER.error("用户消息生产-审批结果通知-失败", e);
		}
	}
	
	public void produceAlarmMsg(AlarmMsgDto alarmMsgDto) {
		for (Integer userId : alarmMsgDto.getUserIds()) {
			// 已存在的告警通知
			AlarmMsgEntity existed  = alarmMsgDaoImpl.selectUnconfirmedMsgById(
					alarmMsgDto.getAlarmId(), userId, alarmMsgDto.getTenantId());
			if (null == existed) {
				AlarmMsgEntity alarmMsgEntity = new AlarmMsgEntity();
				alarmMsgEntity.setUserId(userId);
				alarmMsgEntity.setConfirmed(false);
				alarmMsgEntity.setMsgDate(System.currentTimeMillis());
				alarmMsgEntity.setAlarmId(alarmMsgDto.getAlarmId());
				alarmMsgEntity.setType(alarmMsgDto.getType());
				alarmMsgEntity.setTitle(alarmMsgDto.getTitle());
				alarmMsgEntity.setHostName(alarmMsgDto.getHostName());
				alarmMsgEntity.setHostIp(alarmMsgDto.getHostIp());
				alarmMsgEntity.setTriDate(alarmMsgDto.getTriDate());
				alarmMsgEntity.setAlarmStatus(alarmMsgDto.getAlarmStatus());
				alarmMsgDaoImpl.save(alarmMsgEntity, alarmMsgDto.getTenantId());
			}
		}
		
	}
}
