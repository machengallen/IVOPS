package com.iv.tenant.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.service.IUserService;
import com.iv.tenant.feign.fallback.UserServiceClientFallBack;

@FeignClient(value = "user-service", fallback = UserServiceClientFallBack.class)
public interface IUserServiceClient extends IUserService{

}
