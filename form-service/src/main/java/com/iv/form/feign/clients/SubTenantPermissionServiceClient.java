package com.iv.form.feign.clients;

import com.iv.form.feign.fallback.SubTenantPermissionServiceClientFallBack;
import com.iv.permission.api.service.ISubTenantPermissionService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author liangk
 * @create 2018年 06月 13日
 **/
@FeignClient(value = "permission-service", fallback = SubTenantPermissionServiceClientFallBack.class)
public interface SubTenantPermissionServiceClient extends ISubTenantPermissionService {
}


