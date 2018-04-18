package com.iv.aggregation.feign.clients;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.aggregation.feign.fallback.EmailServiceClientFallBack;
import com.iv.service.IEmailService;
@FeignClient(value = "email-service", fallback = EmailServiceClientFallBack.class)
public interface IEmailServiceClient extends IEmailService {

}
