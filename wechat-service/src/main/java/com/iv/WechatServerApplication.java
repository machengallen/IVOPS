package com.iv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
@PropertySource(value={"classpath:test.properties"}, encoding="utf-8")
@SpringBootApplication
public class WechatServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatServerApplication.class, args);
	}
}
