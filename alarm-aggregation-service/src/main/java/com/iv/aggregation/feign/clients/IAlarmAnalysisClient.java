package com.iv.aggregation.feign.clients;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.aggregation.feign.fallback.AlarmAnalysisClientFallBack;
import com.iv.analysis.api.service.IAlarmAnalysisService;
import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;

@FeignClient(value = "alarm-analysis", fallback = AlarmAnalysisClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface IAlarmAnalysisClient extends IAlarmAnalysisService {

}
