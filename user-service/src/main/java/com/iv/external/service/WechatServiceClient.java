package com.iv.external.service;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.service.IWechatService;

@FeignClient(value = "wechat-service", fallback = WechatServiceClientFallBack.class,configuration= FeignClientsConfigurationCustom.class)
public interface WechatServiceClient extends IWechatService {

}
