package com.iv.zuul.conf.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

	@Override
	public List<SwaggerResource> get() {
		// TODO Auto-generated method stub
		List<SwaggerResource> resources = new ArrayList<SwaggerResource>();
		resources.add(swaggerResource("用户服务", "/v1/user/v2/api-docs", "1.0"));
        resources.add(swaggerResource("微信服务", "/v1/wechat/v2/api-docs", "1.0"));
        resources.add(swaggerResource("告警南向服务", "/v1/alarm/aggregation/v2/api-docs", "1.0"));
        resources.add(swaggerResource("告警北向服务", "/v1/alarm/facade/v2/api-docs", "1.0"));
        resources.add(swaggerResource("策略服务", "/v1/alarm/strategy/v2/api-docs", "1.0"));
        resources.add(swaggerResource("租户服务", "/v1/tenant/mgt/v2/api-docs", "1.0"));
        resources.add(swaggerResource("邮箱服务", "/v1/email/v2/api-docs", "1.0"));
        resources.add(swaggerResource("团队服务", "/v1/group/v2/api-docs", "1.0"));
        resources.add(swaggerResource("消息服务", "/v1/message/v2/api-docs", "1.0"));
        resources.add(swaggerResource("告警报表服务", "/v1/alarm/report/v2/api-docs", "1.0"));
        resources.add(swaggerResource("工单服务", "/v1/form/v2/api-docs", "1.0"));
        resources.add(swaggerResource("脚本服务", "/v1/script/v2/api-docs", "1.0"));
        resources.add(swaggerResource("权限服务", "/v1/permission/v2/api-docs", "1.0"));
        resources.add(swaggerResource("脚本作业服务", "/v1/operation/script/v2/api-docs", "1.0"));
        resources.add(swaggerResource("vmware虚拟化作业服务", "/v1/operation/vsphere/v2/api-docs", "1.0"));
        resources.add(swaggerResource("资源服务", "/v1/resource/v2/api-docs", "1.0"));
        return resources;
	}
	private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
