package com.iv.message.api.dto;

import com.iv.common.dto.ObjectPageDto;

public class OperationSysMsgDto {

	private long allTotal;
	private ObjectPageDto approveFlowMsg;// 审批流通知
	
	public long getAllTotal() {
		return allTotal;
	}
	public void setAllTotal() {
		this.allTotal = approveFlowMsg.getTotal();
	}
	public ObjectPageDto getApproveFlowMsg() {
		return approveFlowMsg;
	}
	public void setApproveFlowMsg(ObjectPageDto approveFlowMsg) {
		this.approveFlowMsg = approveFlowMsg;
	}
	
}
