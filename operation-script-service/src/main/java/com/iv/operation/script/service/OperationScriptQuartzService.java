package com.iv.operation.script.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.iv.common.util.spring.JWTUtil;
import com.iv.operation.script.dao.impl.SingleTaskDaoImpl;
import com.iv.operation.script.dao.impl.SingleTaskScheduleDaoImpl;
import com.iv.operation.script.dto.ScheduleHostsDto;
import com.iv.operation.script.dto.ImmediateHostsDto;
import com.iv.operation.script.entity.SingleTaskScheduleEntity;
import com.iv.operation.script.quartz.job.SingleTaskQuartzJob;

/**
 * @author macheng 2018年6月13日 operation-script-service 单脚本定时任务管理
 * 
 */
@Service
public class OperationScriptQuartzService {

	//private static final Logger LOGGER = LoggerFactory.getLogger(OperationScriptQuartzService.class);

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
		SingleTaskScheduleEntity scheduleEntity = new SingleTaskScheduleEntity();
		scheduleEntity.setSingleTask(singleTaskDao.selectById(taskId));
		scheduleEntity.setCronExp(cronExp);
		singleTaskScheduleDao.save(scheduleEntity);
		return scheduleEntity;
	}

	public List<SingleTaskScheduleEntity> scheduleGet(int taskId) {
		return singleTaskScheduleDao.selectByTaskId(taskId);
	}

	public SingleTaskScheduleEntity singleTaskScheduleMod(int scheduleId, String cronExp) throws SchedulerException {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao.selectById(scheduleId);
		scheduleEntity.setCronExp(cronExp);
		singleTaskScheduleDao.save(scheduleEntity);
		// 刷新执行中的定时任务
		Scheduler scheduler = schedulerFactory.getScheduler();
		TriggerKey triggerKey = getTriggerKey(scheduleEntity);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExp);
		trigger = trigger.getTriggerBuilder().withSchedule(schedBuilder).build();
		scheduler.rescheduleJob(triggerKey, trigger);
		return scheduleEntity;
	}

	public void singleTaskExec(ScheduleHostsDto scheduleHostsDto) throws SchedulerException {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao
				.selectById(scheduleHostsDto.getTaskScheduleId());
		ImmediateHostsDto targetHostsDto = new ImmediateHostsDto();
		targetHostsDto.setScheduleId(scheduleEntity.getId());
		targetHostsDto.setTargetHosts(scheduleHostsDto.getTargetHosts());
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("targetHosts", targetHostsDto);
		String keyName = SingleTaskQuartzJob.class.getSimpleName() + "-" + scheduleEntity.getSingleTask().getId() + "-"
				+ scheduleEntity.getId();// job类名-任务id-定时策略id
		addSchedulerTask(SingleTaskQuartzJob.class, scheduleEntity.getCronExp(), dataMap, keyName);
	}
	
	public void schedulePause(int scheduleId) throws SchedulerException {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao.selectById(scheduleId);
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.pauseTrigger(getTriggerKey(scheduleEntity));
	}
	
	public void scheduleResume(int scheduleId) throws SchedulerException {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao.selectById(scheduleId);
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.resumeTrigger(getTriggerKey(scheduleEntity));
	}
	
	/**
	 * 删除指定的定时作业
	 * @param scheduleId
	 * @throws SchedulerException 
	 */
	public void scheduleDel(int scheduleId) throws SchedulerException {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao.selectById(scheduleId);
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.deleteJob(getJobKey(scheduleEntity));
		singleTaskScheduleDao.delById(scheduleId);
	}
	
	/**
	 * 删除指定任务的所有定时作业
	 * @param taskId
	 * @return 删除的定时作业数
	 * @throws SchedulerException
	 */
	public int removeSchedulerTask(int taskId) throws SchedulerException {
		// 移除任务下的所有定时作业
		for (SingleTaskScheduleEntity scheduleEntity : singleTaskScheduleDao.selectByTaskId(taskId)) {
			Scheduler scheduler = schedulerFactory.getScheduler();
			scheduler.deleteJob(getJobKey(scheduleEntity));
			//scheduler.unscheduleJob(triggerKey)
		}
		return singleTaskScheduleDao.delByTaskId(taskId);
	}
	
	/**
	 * 获取quartz jobkey
	 * @param scheduleEntity
	 * @return
	 */
	private JobKey getJobKey(SingleTaskScheduleEntity scheduleEntity) {
		String keyName = SingleTaskQuartzJob.class.getSimpleName() + "-" 
						+ scheduleEntity.getSingleTask().getId() + "-"
						+ scheduleEntity.getId();
		String groupName = JWTUtil.getReqValue("curTenantId");
		return JobKey.jobKey(keyName, groupName);
	}
	
	private TriggerKey getTriggerKey(SingleTaskScheduleEntity scheduleEntity) {
		String keyName = SingleTaskQuartzJob.class.getSimpleName() + "-" 
				+ scheduleEntity.getSingleTask().getId() + "-"
				+ scheduleEntity.getId();
		String groupName = JWTUtil.getReqValue("curTenantId");
		return TriggerKey.triggerKey(keyName, groupName);
	}

}
