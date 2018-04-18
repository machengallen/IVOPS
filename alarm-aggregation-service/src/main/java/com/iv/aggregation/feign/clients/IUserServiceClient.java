package com.iv.aggregation.feign.clients;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.aggregation.feign.fallback.UserServiceClientFallBack;
import com.iv.service.IUserService;

@FeignClient(value = "user-service", fallback = UserServiceClientFallBack.class)
public interface IUserServiceClient extends IUserService {

}
