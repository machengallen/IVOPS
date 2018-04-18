package com.iv.aggregation.feign.fallback;

import org.springframework.stereotype.Component;

import com.iv.aggregation.feign.clients.IAlarmAnalysisClient;
import com.iv.analysis.api.dto.AlarmInfoDto;
import com.iv.common.response.ErrorMsg;
import com.iv.common.response.ResponseDto;

@Component
public class AlarmAnalysisClientFallBack implements IAlarmAnalysisClient {

	@Override
	public ResponseDto alarmAnalysis(AlarmInfoDto infoDto) {
		
		return ResponseDto.builder(ErrorMsg.UNKNOWN);
	}

}
