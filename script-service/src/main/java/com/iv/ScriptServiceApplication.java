package com.iv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients
public class ScriptServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScriptServiceApplication.class, args);
	}
}
