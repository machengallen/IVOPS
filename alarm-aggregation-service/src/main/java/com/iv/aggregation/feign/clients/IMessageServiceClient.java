package com.iv.aggregation.feign.clients;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.iv.common.enumeration.WorkflowType;
import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.common.response.ResponseDto;
import com.iv.message.api.dto.AlarmMsgDto;
import com.iv.message.api.service.IMessageService;

@FeignClient(value = "message-service", fallback = MessageServiceClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface IMessageServiceClient extends IMessageService {

}

@Component
class MessageServiceClientFallBack implements IMessageServiceClient {

	@Override
	public ResponseDto produceApproveMsg(String applicant, int approverId, String enterpriseName,
			WorkflowType workflowType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto produceApplyMsg(int userId, boolean approved, String enterpriseName, String remark,
			WorkflowType workflowType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto produceAlarmMsg(AlarmMsgDto alarmMsgDto) {
		// TODO Auto-generated method stub
		return null;
	}
	
}