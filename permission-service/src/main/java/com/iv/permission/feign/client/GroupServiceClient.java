package com.iv.permission.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.permission.feign.fallback.GroupServiceClientFallBack;
import com.iv.service.IGroupService;

@FeignClient(value = "group-service", fallback = GroupServiceClientFallBack.class, configuration= FeignClientsConfigurationCustom.class)
public interface GroupServiceClient extends IGroupService{

}
