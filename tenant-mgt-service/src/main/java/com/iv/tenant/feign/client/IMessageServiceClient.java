package com.iv.tenant.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.common.enumeration.WorkflowType;
import com.iv.common.response.ResponseDto;
import com.iv.message.api.dto.AlarmMsgDto;
import com.iv.message.api.service.IMessageService;

@FeignClient(value = "message-service", fallback = MessageServiceClientFallBack.class)
public interface IMessageServiceClient extends IMessageService {

}

class MessageServiceClientFallBack implements IMessageServiceClient{

	@Override
	public ResponseDto produceApproveMsg(String applicant, int approverId, String subEnterpriseName,
			String enterpriseName, WorkflowType workflowType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto produceApplyMsg(int userId, boolean approved, String subEnterpriseName, String enterpriseName,
			String remark, WorkflowType workflowType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto produceAlarmMsg(AlarmMsgDto alarmMsgDto) {
		// TODO Auto-generated method stub
		return null;
	}

}
