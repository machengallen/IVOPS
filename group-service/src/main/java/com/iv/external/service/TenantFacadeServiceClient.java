package com.iv.external.service;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.tenant.api.service.ITenantFacadeService;

@FeignClient(value = "tenant-mgt-service", fallback = TenantFacadeServiceFallBack.class, configuration= FeignClientsConfigurationCustom.class)
public interface TenantFacadeServiceClient extends ITenantFacadeService{

}
