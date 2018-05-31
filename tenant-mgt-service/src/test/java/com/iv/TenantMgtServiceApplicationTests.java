package com.iv;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

public class TenantMgtServiceApplicationTests {

	public static void main(String[] args) throws Exception {
		Pattern pattern = Pattern.compile("[0-9]*"); 
		Matcher isNum = pattern.matcher("801800012t");
		System.out.println(isNum.matches());
	}

}
