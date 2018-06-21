package com.iv.external.service;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.permission.api.service.ISubTenantPermissionService;
@FeignClient(value = "permission-service", fallback = SubTenantPermissionServiceClientFallBack.class,configuration= FeignClientsConfigurationCustom.class)
public interface SubTenantPermissionServiceClient extends ISubTenantPermissionService {

}
