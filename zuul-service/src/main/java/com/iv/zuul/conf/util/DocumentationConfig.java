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
        resources.add(swaggerResource("团队服务", "/team/v2/api-docs", "1.0"));
        resources.add(swaggerResource("用户服务", "/user/v2/api-docs", "1.0"));
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
