package com.iv.form.feign.clients;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.form.feign.fallback.WechatServiceClientFallBack;
import com.iv.service.IWechatService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author liangk
 * @create 2018年 05月 30日
 **/
@FeignClient(value = "wechat-service", fallback = WechatServiceClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface WechatServiceClient extends IWechatService {
}
