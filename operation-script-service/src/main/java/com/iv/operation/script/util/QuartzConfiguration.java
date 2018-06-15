package com.iv.operation.script.util;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.Scheduler;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfiguration {

	@Bean  
    public SchedulerFactoryBean schedulerFactory(DataSource dataSource) throws IOException{  
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setOverwriteExistingJobs(true);
        //schedulerFactory.setAutoStartup(true);
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setQuartzProperties(quartzProperties());
        return schedulerFactory;  
    } 
	
	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}
	
	/*@Bean(name="quartzScheduler")
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws IOException {
        return schedulerFactoryBean.getScheduler();
    }*/
}
