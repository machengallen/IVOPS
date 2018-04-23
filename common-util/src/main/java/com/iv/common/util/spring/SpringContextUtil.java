package com.iv.common.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring ioc工具类
 * @author macheng
 * 2018年4月3日
 * alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		SpringContextUtil.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	public static Object getBean(String beanName){
		return applicationContext.getBean(beanName);
	}
	
	public static <T>T getBean(String beanName , Class<T>clazz) {  
        return applicationContext.getBean(beanName , clazz);  
    } 
	public static <T>T getBean(Class<T>clazz) {
        return applicationContext.getBean(clazz);  
    } 

}
