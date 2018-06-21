package com.iv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;


@SpringCloudApplication
@EnableFeignClients
@SpringBootApplication
public class AlarmReportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmReportServiceApplication.class, args);
	}
}
