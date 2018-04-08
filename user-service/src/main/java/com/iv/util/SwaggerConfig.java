package com.iv.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置类
 * 
 * @author admin
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket devApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("dev")
				.genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(true)
                .forCodeGeneration(true)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .build()
                .apiInfo(devApiInfo());
	}
	
	 private ApiInfo devApiInfo() {
	        return new ApiInfoBuilder()
	            .title("运维机器人-智能监控(mas) API")//大标题
	            .description("MAS Platform REST API, all the applications could access the Object model data via JSON.")//详细描述
	            .version("1.0.0")//版本
	            .termsOfServiceUrl("NO terms of service")
	            .contact(new Contact("产品开发部", "http://mas.ivops.cn/login", "mac@inno-view.com"))//作者
	            .license("The Apache License, Version 2.0")
	            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
	            .build();
	 }
}
