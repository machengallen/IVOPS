package com.iv.facade.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
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
		ParameterBuilder tokenPar = new ParameterBuilder();  
        List<Parameter> pars = new ArrayList<Parameter>();  
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();  
        pars.add(tokenPar.build());
		return new Docket(DocumentationType.SWAGGER_2)
				.genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(true)
                .forCodeGeneration(true)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.iv"))
                .build()
                .globalOperationParameters(pars)
                .apiInfo(devApiInfo());
	}
	
	 private ApiInfo devApiInfo() {
	        return new ApiInfoBuilder()
	            .title("告警北向服务API(/v1/alarm/facade/**)")//大标题
	            .description("MAS Platform REST API, all the applications could access the Object model data via JSON.")//详细描述
	            .version("1.0.0")//版本
	            .termsOfServiceUrl("NO terms of service")
	            .contact(new Contact("产品开发部", "http://mas.ivops.cn/login", "mac@inno-view.cn"))//作者
	            .license("The Apache License, Version 2.0")
	            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
	            .build();
	 }
}
