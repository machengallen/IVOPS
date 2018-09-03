package com.iv;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.iv.common.response.ErrorMsg;
import com.iv.common.response.ResponseDto;
@SpringCloudApplication
@EnableFeignClients
public class Oauth2Application extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Oauth2Application.class, args);
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/oauth/confirm_access").setViewName("authorize");
	}
	
}
