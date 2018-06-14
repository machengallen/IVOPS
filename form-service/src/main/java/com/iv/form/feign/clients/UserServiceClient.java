package com.iv.form.feign.clients;


import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.form.feign.fallback.UserServiceClientFallBack;
import com.iv.service.IUserService;
import org.springframework.cloud.netflix.feign.FeignClient;
/**
 * @author liangk
 * 2018年5月7日
 * form-service-api-1.0.0-SNAPSHOT
 */
@FeignClient(value = "user-service", fallback = UserServiceClientFallBack.class, configuration= FeignClientsConfigurationCustom.class)
public interface UserServiceClient extends IUserService {

}
