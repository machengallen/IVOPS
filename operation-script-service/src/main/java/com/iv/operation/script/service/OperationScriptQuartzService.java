package com.iv.operation.script.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.iv.common.util.spring.JWTUtil;
import com.iv.operation.script.dao.impl.SingleTaskDaoImpl;
import com.iv.operation.script.dao.impl.SingleTaskScheduleDaoImpl;
import com.iv.operation.script.dto.ScheduleHostsDto;
import com.iv.operation.script.dto.TargetHostsDto;
import com.iv.operation.script.entity.SingleTaskScheduleEntity;
import com.iv.operation.script.quartz.job.SingleTaskQuartzJob;

/**
 * @author macheng 
 * 2018年6月13日 
 * operation-script-service 
 * 单脚本定时任务管理
 * 
 */
@Service
public class OperationScriptQuartzService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperationScriptQuartzService.class);

	@Autowired
	private SchedulerFactoryBean schedulerFactory;
	@Autowired
	private SingleTaskScheduleDaoImpl singleTaskScheduleDao;
	@Autowired
	private SingleTaskDaoImpl singleTaskDao;

	private void addSchedulerTask(Class<? extends Job> jobClass, String cronExpression, Map<String, Object> dataMap,
			String keyName) throws SchedulerException {
		Scheduler scheduler = schedulerFactory.getScheduler();
		if (!scheduler.isStarted()) {
			scheduler.start();// 启动调度引擎
		}
		String groupName = JWTUtil.getReqValue("curTenantId");// 以租户id进行分组
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(keyName, groupName).build();
		dataMap.put("groupName", groupName);// 存入租户标识符
		jobDetail.getJobDataMap().putAll(dataMap);// 放入运行中数据
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(keyName, groupName)
				.withSchedule(scheduleBuilder).build();

		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	public SingleTaskScheduleEntity singleTaskSchedule(int taskId, String cronExp) {
		SingleTaskScheduleEntity entity = new SingleTaskScheduleEntity();
		entity.setSingleTask(singleTaskDao.selectById(taskId));
		entity.setCronExp(cronExp);
		return singleTaskScheduleDao.save(entity);
	}
	
	public List<SingleTaskScheduleEntity> scheduleGet(int taskId){
		return singleTaskScheduleDao.selectByTaskId(taskId);
	}
	
	public SingleTaskScheduleEntity singleTaskScheduleMod(int scheduleId, String cronExp) {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao.selectById(scheduleId);
		scheduleEntity.setCronExp(cronExp);
		singleTaskScheduleDao.save(scheduleEntity);
		return scheduleEntity;
	}

	public void singleTaskExec(ScheduleHostsDto scheduleHostsDto) throws SchedulerException {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao
				.selectById(scheduleHostsDto.getTaskScheduleId());
		TargetHostsDto targetHostsDto = new TargetHostsDto();
		targetHostsDto.setSingleTaskId(scheduleEntity.getSingleTask().getId());
		targetHostsDto.setTargetHosts(scheduleHostsDto.getTargetHosts());
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("targetHosts", targetHostsDto);
		String keyName = SingleTaskQuartzJob.class.getSimpleName() + "-" + scheduleEntity.getSingleTask().getId() + "-"
				+ scheduleEntity.getId();// job类名-任务id-定时策略id
		addSchedulerTask(SingleTaskQuartzJob.class, scheduleEntity.getCronExp(), dataMap, keyName);
	}

}
