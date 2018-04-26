package com.iv.facade.feign.fallback;

import org.springframework.stereotype.Component;

import com.iv.aggregation.api.constant.StrategyCycle;
import com.iv.aggregation.api.dto.AlarmQueryDto;
import com.iv.aggregation.api.dto.AlarmTransferDto;
import com.iv.common.response.ResponseDto;
import com.iv.facade.feign.client.IAlarmAggregationClient;

@Component
public class AlarmAggregationClientFallBack implements IAlarmAggregationClient {

	@Override
	public ResponseDto claimAlarm(String token, String lifeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto transferAlarm(AlarmTransferDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getMyAlarmPaging(AlarmQueryDto query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto getAlarmPaging(AlarmQueryDto query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto updateAlarmCleanQuartz(String exp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseDto updateAlarmCleanCycle(StrategyCycle cycle) {
		// TODO Auto-generated method stub
		return null;
	}

}
