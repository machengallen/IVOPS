package com.iv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
public class AlarmAnalysisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmAnalysisServiceApplication.class, args);
	}
}
