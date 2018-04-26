package com.iv;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
@SpringCloudApplication
@EnableFeignClients
public class TenantMgtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TenantMgtServiceApplication.class, args);
	}
}
