package com.iv.message.api.dto;

import com.iv.common.dto.ObjectPageDto;

public class TenantSysMsgDto {

	private long allTotal;
	private ObjectPageDto applyFlowMsg;// 申请流通知
	private ObjectPageDto approveFlowMsg;// 审批流通知

	public long getAllTotal() {
		return allTotal;
	}

	public void setAllTotal() {
		this.allTotal = applyFlowMsg.getTotal() + approveFlowMsg.getTotal();
	}

	public ObjectPageDto getApplyFlowMsg() {
		return applyFlowMsg;
	}

	public void setApplyFlowMsg(ObjectPageDto applyFlowMsg) {
		this.applyFlowMsg = applyFlowMsg;
	}

	public ObjectPageDto getApproveFlowMsg() {
		return approveFlowMsg;
	}

	public void setApproveFlowMsg(ObjectPageDto approveFlowMsg) {
		this.approveFlowMsg = approveFlowMsg;
	}

}
