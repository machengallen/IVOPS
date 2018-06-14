package com.iv.form.feign.clients;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.form.feign.fallback.EmailServiceClientFallBack;
import com.iv.service.IEmailService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author liangk
 * @create 2018年 05月 30日
 **/
@FeignClient(value = "email-service", fallback = EmailServiceClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface EmailServiceClient extends IEmailService {
}
