package com.iv.zuul.conf.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
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
		//添加head参数start  
        ParameterBuilder tokenPar = new ParameterBuilder();  
        List<Parameter> pars = new ArrayList<Parameter>();  
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();  
        pars.add(tokenPar.build());  
		return new Docket(DocumentationType.SWAGGER_2)
				/*.groupName("dev")
				.genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(true)
                .forCodeGeneration(true)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.iv"))
                .build()
                .globalOperationParameters(pars)  
                .apiInfo(devApiInfo());*/
				 .apiInfo(devApiInfo())
		         .select()
		         .apis(RequestHandlerSelectors.basePackage("com.iv"))
		         .paths(PathSelectors.any())
		         .build();
	}
	
	 private ApiInfo devApiInfo() {
	        return new ApiInfoBuilder()
	            .title("运维机器人-智能监控(mas) API")//大标题
	            .description("MAS Platform REST API, all the applications could access the Object model data via JSON.")//详细描述
	            .version("1.0.0")//版本
	            .termsOfServiceUrl("http://localhost:8121")
	            .contact(new Contact("产品开发部", "http://mas.ivops.cn/login", "mac@inno-view.com"))//作者
	            .license("The Apache License, Version 2.0")
	            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
	            .build();		 
	 }
	 
	 @Bean
	    UiConfiguration uiConfig() {
	        return new UiConfiguration(null, "list", "alpha", "schema",
	                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
	    }
}
