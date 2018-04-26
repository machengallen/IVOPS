package com.iv.aggregation.feign.clients;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.aggregation.feign.fallback.AlarmStrategyClientFallBack;
import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.strategy.api.service.IAlarmStrategyService;

@FeignClient(value = "alarm-strategy-service", fallback = AlarmStrategyClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface IAlarmStrategyClient extends IAlarmStrategyService {

}
