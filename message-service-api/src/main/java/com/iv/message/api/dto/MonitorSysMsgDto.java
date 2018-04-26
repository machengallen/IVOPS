package com.iv.message.api.dto;

import com.iv.common.dto.ObjectPageDto;

/**
 * 监控系统用户消息体
 * 
 * @author macheng 2018年1月26日 aggregation-1.3.0-SNAPSHOT
 * 
 */
public class MonitorSysMsgDto {

	private long allTotal;
	private ObjectPageDto alarmMsg;// 故障告警通知
	private ObjectPageDto applyFlowMsg;// 申请流通知

	public long getAllTotal() {
		return allTotal;
	}

	public void setAllTotal() {
		this.allTotal = alarmMsg.getTotal() + applyFlowMsg.getTotal();
	}

	public ObjectPageDto getAlarmMsg() {
		return alarmMsg;
	}

	public void setAlarmMsg(ObjectPageDto alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	public ObjectPageDto getApplyFlowMsg() {
		return applyFlowMsg;
	}

	public void setApplyFlowMsg(ObjectPageDto applyFlowMsg) {
		this.applyFlowMsg = applyFlowMsg;
	}

}
