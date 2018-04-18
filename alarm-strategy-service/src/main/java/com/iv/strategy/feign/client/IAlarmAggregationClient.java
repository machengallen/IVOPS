package com.iv.strategy.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.aggregation.api.service.IAlarmAggregationService;
import com.iv.strategy.feign.fallback.AlarmAggregationClientFallBack;

@FeignClient(value = "alarm-aggregation-service", fallback = AlarmAggregationClientFallBack.class)
public interface IAlarmAggregationClient extends IAlarmAggregationService {

}
