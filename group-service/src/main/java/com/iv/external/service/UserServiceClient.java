package com.iv.external.service;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.service.IUserService;

@FeignClient(value = "user-service", fallback = UserServiceClientFallBack.class, configuration= FeignClientsConfigurationCustom.class)
public interface UserServiceClient extends IUserService {

}
