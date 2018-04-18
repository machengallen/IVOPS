package com.iv.aggregation.feign.clients;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.analysis.api.service.IAlarmAnalysisService;

@FeignClient
public interface IAlarmAnalysisClient extends IAlarmAnalysisService {

}
