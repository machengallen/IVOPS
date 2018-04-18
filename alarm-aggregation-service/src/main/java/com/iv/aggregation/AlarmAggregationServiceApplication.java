package com.iv.aggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@SpringCloudApplication
@EnableFeignClients
@PropertySource(value={"classpath:config.properties"}, encoding="utf-8")
public class AlarmAggregationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmAggregationServiceApplication.class, args);
	}
}
