package com.iv.external.service;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.service.IUserService;

@FeignClient(value = "user-service", fallback = UserServiceClientFallBack.class)
public interface UserServiceClient extends IUserService {

}
