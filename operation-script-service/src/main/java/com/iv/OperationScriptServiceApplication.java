package com.iv;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients
public class OperationScriptServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperationScriptServiceApplication.class, args);
	}
}
