package com.iv.form.feign.clients;

import com.iv.common.requestInterceptor.FeignClientsConfigurationCustom;
import com.iv.form.feign.fallback.GroupServiceClientFallBack;
import com.iv.service.IGroupService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 组相关
 * @author liangk
 * @create 2018年 05月 23日
 **/
@FeignClient(value = "group-service", fallback = GroupServiceClientFallBack.class, configuration = FeignClientsConfigurationCustom.class)
public interface GroupServiceClient extends IGroupService {

}
