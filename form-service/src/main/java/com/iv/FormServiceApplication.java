package com.iv;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients
//@EnableTransactionManagement
public class FormServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FormServiceApplication.class, args);
    }
}

