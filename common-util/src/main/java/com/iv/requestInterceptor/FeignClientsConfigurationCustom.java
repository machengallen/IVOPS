package com.iv.requestInterceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign远程调用服务 header头部信息丢失问题解决
 * @author zhangying
 * 2018年4月20日
 * aggregation-1.4.0-SNAPSHOT
 */
public class FeignClientsConfigurationCustom implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		// TODO Auto-generated method stub
		 RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();    
		    if (requestAttributes == null) {    
		      return;    
		    }   
		  
		    HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();    
		    Enumeration<String> headerNames = request.getHeaderNames();    
		    if (headerNames != null) {    
		      while (headerNames.hasMoreElements()) {    
		        String name = headerNames.nextElement();    
		        Enumeration<String> values = request.getHeaders(name);    
		        while (values.hasMoreElements()) {    
		          String value = values.nextElement();    
		          template.header(name, value);    
		        }    
		      }    
		    }    
	}
	
	@Bean  
	public FeignHystrixConcurrencyStrategy feignHystrixConcurrencyStrategy() {  
	  return new FeignHystrixConcurrencyStrategy();  
	} 

}
