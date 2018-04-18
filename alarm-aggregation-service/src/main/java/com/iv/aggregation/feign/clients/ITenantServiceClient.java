package com.iv.aggregation.feign.clients;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.aggregation.feign.fallback.TenantServiceClientFallBack;
import com.iv.tenant.api.service.ITenantFacadeService;

@FeignClient(value = "tenant-mgt-service", fallback = TenantServiceClientFallBack.class)
public interface ITenantServiceClient extends ITenantFacadeService {

}
