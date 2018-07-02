package com.iv.operation.script.service;

import com.iv.common.dto.BatchResultDto;
import com.iv.common.dto.IdList;
import com.iv.common.dto.ObjectPageDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.operation.script.constant.ErrorMsg;
import com.iv.operation.script.constant.OperatingSystemType;
import com.iv.operation.script.constant.ScriptSourceType;
import com.iv.operation.script.dao.impl.ScheduleTargetDaoImpl;
import com.iv.operation.script.dao.impl.SingleTaskDaoImpl;
import com.iv.operation.script.dao.impl.SingleTaskLifeDaoImpl;
import com.iv.operation.script.dao.impl.SingleTaskScheduleDaoImpl;
import com.iv.operation.script.dto.*;
import com.iv.operation.script.entity.ScheduleTargetEntity;
import com.iv.operation.script.entity.SingleTaskEntity;
import com.iv.operation.script.entity.SingleTaskLifeEntity;
import com.iv.operation.script.entity.SingleTaskScheduleEntity;
import com.iv.operation.script.feign.client.IScriptServiceClient;
import com.iv.operation.script.quartz.job.SingleTaskQuartzJob;
import com.iv.operation.script.util.*;
import com.iv.script.api.dto.TemporaryScriptDto;
import com.jcraft.jsch.Session;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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
	@Autowired
	private ScheduleTargetDaoImpl scheduleTargetDao;
	@Autowired
	private SingleTaskLifeDaoImpl singleTaskLifeDao;
	@Autowired
	private IScriptServiceClient scriptServiceClient;

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

	public SingleTaskScheduleEntity singleTaskSchedule(int taskId, String cronExp, String name) {
		SingleTaskScheduleEntity scheduleEntity = new SingleTaskScheduleEntity();
		scheduleEntity.setSingleTask(singleTaskDao.selectById(taskId));
		scheduleEntity.setCronExp(cronExp);
		scheduleEntity.setName(name);
		long time = System.currentTimeMillis();
		scheduleEntity.setCreator(JWTUtil.getReqValue("realName"));
		scheduleEntity.setCreaDate(time);
		scheduleEntity.setModifier(JWTUtil.getReqValue("realName"));
		scheduleEntity.setModDate(time);
		singleTaskScheduleDao.save(scheduleEntity);
		return scheduleEntity;
	}

	public List<ScheduleTargetEntity> scheduleTargetGet(int scheduleId) {
		return scheduleTargetDao.selectByScheduleId(scheduleId);
	}

	public List<SingleTaskScheduleEntity> scheduleGetByTask(int taskId) {
		return singleTaskScheduleDao.selectByTaskId(taskId);
	}

	public ObjectPageDto<SingleTaskScheduleDto> scheduleGetPage(ScheduleQueryDto queryDto) throws SchedulerException {
		Scheduler scheduler = schedulerFactory.getScheduler();
		ObjectPageDto<SingleTaskScheduleDto> dtos = singleTaskScheduleDao.selectPage(queryDto);
		for (SingleTaskScheduleDto dto : dtos.getData()) {
			TriggerKey triggerKey = getTriggerKey(dto.getId(), dto.getTaskId());
			dto.setState(scheduler.getTriggerState(triggerKey));
			Trigger trigger = scheduler.getTrigger(triggerKey);
			if (null != trigger) {
				dto.setNextFireTime(trigger.getNextFireTime() == null ? null : trigger.getNextFireTime().getTime());
				dto.setPreviousFireTime(
						trigger.getPreviousFireTime() == null ? null : trigger.getPreviousFireTime().getTime());
				dto.setStartTime(trigger.getStartTime().getTime());
				dto.setEndTime(trigger.getEndTime() == null ? null : trigger.getEndTime().getTime());
			}
		}
		return dtos;
	}

	public SingleTaskScheduleEntity singleTaskScheduleMod(int scheduleId, String cronExp, String name) throws SchedulerException {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao.selectById(scheduleId);
		scheduleEntity.setCronExp(cronExp);
		scheduleEntity.setName(name);
		scheduleEntity.setModifier(JWTUtil.getReqValue("realName"));
		scheduleEntity.setModDate(System.currentTimeMillis());
		singleTaskScheduleDao.save(scheduleEntity);
		// 刷新执行中的定时任务
		Scheduler scheduler = schedulerFactory.getScheduler();
		TriggerKey triggerKey = getTriggerKey(scheduleEntity.getId(), scheduleEntity.getSingleTask().getId());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if(null == trigger) {// 未提交quartz的任务,暂忽略
			return null;
		}
		CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExp);
		trigger = trigger.getTriggerBuilder().withSchedule(schedBuilder).build();
		scheduler.rescheduleJob(triggerKey, trigger);
		return scheduleEntity;
	}

	public void scheduleExec(ScheduleHostsDto scheduleHostsDto) throws SchedulerException {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao.selectById(scheduleHostsDto.getScheduleId());
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("targetHosts", scheduleHostsDto);
		String keyName = SingleTaskQuartzJob.class.getSimpleName() + "-" + scheduleEntity.getSingleTask().getId() + "-"
				+ scheduleEntity.getId();// job类名-任务id-定时策略id
		addSchedulerTask(SingleTaskQuartzJob.class, scheduleEntity.getCronExp(), dataMap, keyName);
	}

	public void schedulePause(int scheduleId) throws SchedulerException {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao.selectById(scheduleId);
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.pauseTrigger(getTriggerKey(scheduleEntity.getId(), scheduleEntity.getSingleTask().getId()));
	}

	public void scheduleResume(int scheduleId) throws SchedulerException {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao.selectById(scheduleId);
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.resumeTrigger(getTriggerKey(scheduleEntity.getId(), scheduleEntity.getSingleTask().getId()));
	}

	/**
	 * 删除指定的定时作业
	 *
	 * @param scheduleId
	 * @throws SchedulerException
	 */
	public BatchResultDto<Integer> scheduleDel(IdList<Integer> scheduleIds) {
		BatchResultDto<Integer> resultDto = new BatchResultDto<>();
		List<Integer> failedIds = new ArrayList<>();
		for (Integer scheduleId : scheduleIds.getIds()) {
			try {
				SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao.selectById(scheduleId);
				Scheduler scheduler = schedulerFactory.getScheduler();
				scheduler.deleteJob(getJobKey(scheduleEntity.getId(), scheduleEntity.getSingleTask().getId()));
				scheduleTargetDao.delByScheduleId(scheduleId);
				singleTaskScheduleDao.delById(scheduleId);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				failedIds.add(scheduleId);
			}
		}
		resultDto.setFailedMsg(failedIds);
		resultDto.setTotal(scheduleIds.getIds().size());
		resultDto.setFailed(failedIds.size());
		return resultDto;
	}

	/**
	 * 删除指定任务的所有定时作业
	 *
	 * @param taskId
	 * @return 删除的定时作业数
	 * @throws SchedulerException
	 */
	public int removeSchedulerTask(int taskId) throws SchedulerException {
		// 移除任务下的所有定时作业
		for (SingleTaskScheduleEntity scheduleEntity : singleTaskScheduleDao.selectByTaskId(taskId)) {
			Scheduler scheduler = schedulerFactory.getScheduler();
			scheduler.deleteJob(getJobKey(scheduleEntity.getId(), scheduleEntity.getSingleTask().getId()));
			// scheduler.unscheduleJob(triggerKey)
		}
		return singleTaskScheduleDao.delByTaskId(taskId);
	}

	/**
	 * 定时任务执行
	 * 
	 * @param targetHostsDto
	 * @return
	 */
	public List<ScheduleTargetEntity> excute(ScheduleHostsDto targetHostsDto) {
		SingleTaskScheduleEntity scheduleEntity = singleTaskScheduleDao.selectById(targetHostsDto.getScheduleId());
		List<ScheduleTargetEntity> taskTargetList = new ArrayList<ScheduleTargetEntity>();

		SingleTaskEntity singleTask = scheduleEntity.getSingleTask();
		OperatingSystemType systemType = singleTask.getSystemType();
		CompletionService<ScheduleTargetEntity> completionService;

		switch (systemType){
			case LINUX:
				completionService = doTask(scheduleEntity, targetHostsDto.getTargetHosts(), taskTargetList);
				break;
			case WINDOWS:
				completionService = doTaskWin(scheduleEntity, targetHostsDto.getTargetHosts(), taskTargetList);
				break;
			default:
				completionService = doTask(scheduleEntity, targetHostsDto.getTargetHosts(), taskTargetList);
				break;
		}


		// 获取任务结果集
		int count = targetHostsDto.getTargetHosts().size() - taskTargetList.size();
		for (int j = 1; j <= count; j++) {
			try {
				Future<ScheduleTargetEntity> future = completionService.take();// 阻塞等待第一个结果，返回后该结果从队列删除
				taskTargetList.add(future.get());// get不会阻塞
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage());
			} catch (ExecutionException e) {
				LOGGER.error(e.getMessage());
			}
		}
		scheduleTargetDao.delByScheduleId(scheduleEntity.getId());// 删除上次执行任务结果
		scheduleTargetDao.batchSave(taskTargetList);

		// 更新任务生命周期
		SingleTaskLifeEntity lifeEntity = scheduleEntity.getSingleTask().getTaskLife();
		lifeEntity.execNumAdd();
		// lifeEntity.setExecDate(System.currentTimeMillis());
		// lifeEntity.setExecutor(JWTUtil.getReqValue("realName"));
		singleTaskLifeDao.save(lifeEntity);

		// TODO 调用微信服务发送模板消息
		// TODO 调用邮箱服务发送邮件通知消息
		return taskTargetList;

	}

	private CompletionService<ScheduleTargetEntity> doTask(SingleTaskScheduleEntity scheduleEntity,
			List<HostDto> targetHosts, List<ScheduleTargetEntity> taskTargetList) {
		SingleTaskEntity taskEntity = scheduleEntity.getSingleTask();
		CompletionService<ScheduleTargetEntity> completionService = getExecutorService(targetHosts.size());// 线程提交服务
		for (HostDto host : targetHosts) {
			ResponseEntity<byte[]> fileStream = getFileStream(taskEntity.getScriptSrc(), taskEntity.getScriptId());// 获取执行脚本内容流
			if (null == fileStream) {
				// 没有该文件或脚本库服务未响应
				ScheduleTargetEntity targetEntity = new ScheduleTargetEntity(scheduleEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SCRIPT_NOT_EXIST.getMsg());
				taskTargetList.add(targetEntity);
				continue;
			}
			TemporaryScriptDto temporaryScriptDto = scriptServiceClient
					.temporaryScriptInfoById(taskEntity.getScriptId());
			if (null == temporaryScriptDto) {
				ScheduleTargetEntity targetEntity = new ScheduleTargetEntity(scheduleEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SCRIPT_NOT_EXIST.getMsg());
				taskTargetList.add(targetEntity);
				continue;
			}
			String fileName = temporaryScriptDto.getName();
			Session session = getSession(host.getHostIp(), host.getAccount(), host.getPassword(), host.getPort());
			if (null == session) {
				// 获取ssh连接失败
				ScheduleTargetEntity targetEntity = new ScheduleTargetEntity(scheduleEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SSH_CONNECT_FAILED.getMsg());
				taskTargetList.add(targetEntity);
				continue;
			}
			// 提交任务至线程池
			completionService.submit(new Callable<ScheduleTargetEntity>() {

				@Override
				public ScheduleTargetEntity call() throws Exception {
					// 上传脚本
					ScheduleTargetEntity targetEntity = new ScheduleTargetEntity(scheduleEntity, host.getHostIp(),
							host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE, null);
					OptResultDto sftp = SSHUtil.sftpUpload(session, fileName, fileStream.getBody());
					if (!sftp.isSuccess()) {
						targetEntity.setResult(sftp.getResult());
						return targetEntity;
					}
					OptResultDto cmd = SSHUtil.sshCmd(session, "./" + fileName);// 执行脚本
					targetEntity.setSuccess(Boolean.TRUE);
					targetEntity.setResult(cmd.getResult());
					SSHUtil.sftpDelete(session, fileName);// 删除脚本
					return targetEntity;
				}
			});
			session.disconnect();
		}
		return completionService;
	}

	private Session getSession(String ip, String userName, String password, Integer port) {
		SSHAccount sshAccount = new SSHAccount(userName, password, ip);// 准备ssh连接
		if (null != port && port != 22) {
			sshAccount.setPort(port);
		}
		return SSHSessionFactory.getSession(sshAccount);
	}

	private ResponseEntity<byte[]> getFileStream(ScriptSourceType scriptType, int scriptId) {

		ResponseEntity<byte[]> fileStream;
		if(scriptType.name().equals(ScriptSourceType.SCRIPT_LIBRARY.name())) {
			fileStream = scriptServiceClient.officialRead(scriptId);
		} else {
			fileStream = scriptServiceClient.tempRead(scriptId);
		}
		return fileStream;
	}

	private <T> CompletionService<T> getExecutorService(int elements) {
		final BlockingDeque<Future<T>> blockingDeque = new LinkedBlockingDeque<Future<T>>(elements);
		final CompletionService<T> completionService = new ExecutorCompletionService<T>(ThreadPoolUtil.getInstance(),
				blockingDeque);
		return completionService;
	}

	/**
	 * 获取quartz jobkey
	 *
	 * @param scheduleEntity
	 * @return
	 */
	private JobKey getJobKey(int scheduleId, int taskId) {
		String keyName = SingleTaskQuartzJob.class.getSimpleName() + "-" + taskId + "-" + scheduleId;
		String groupName = JWTUtil.getReqValue("curTenantId");
		return JobKey.jobKey(keyName, groupName);
	}

	private TriggerKey getTriggerKey(int scheduleId, int taskId) {
		String keyName = SingleTaskQuartzJob.class.getSimpleName() + "-" + taskId + "-" + scheduleId;
		String groupName = JWTUtil.getReqValue("curTenantId");
		return TriggerKey.triggerKey(keyName, groupName);
	}

	/**
	 * windows 定时任务
	 * @param scheduleEntity
	 * @param targetHosts
	 * @param taskTargetList
	 * @return
	 */
	private CompletionService<ScheduleTargetEntity> doTaskWin(SingleTaskScheduleEntity scheduleEntity, List<HostDto> targetHosts, List<ScheduleTargetEntity> taskTargetList) {
		SingleTaskEntity taskEntity = scheduleEntity.getSingleTask();
		CompletionService<ScheduleTargetEntity> completionService = getExecutorService(targetHosts.size());// 线程提交服务
		for (HostDto host : targetHosts) {
			ResponseEntity<byte[]> fileStream = getFileStream(taskEntity.getScriptSrc(), taskEntity.getScriptId());// 获取执行脚本内容流
			if(null == fileStream) {
				// 没有该文件或脚本库服务未响应
				ScheduleTargetEntity targetEntity = new ScheduleTargetEntity(scheduleEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SCRIPT_NOT_EXIST.getMsg());
				taskTargetList.add(targetEntity);
				continue;
			}
			TemporaryScriptDto temporaryScriptDto = scriptServiceClient.temporaryScriptInfoById(taskEntity.getScriptId());
			if(null == temporaryScriptDto) {
				ScheduleTargetEntity targetEntity = new ScheduleTargetEntity(scheduleEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SCRIPT_NOT_EXIST.getMsg());
				taskTargetList.add(targetEntity);
				continue;
			}

			//win telnet协议
			TelnetClientUtil instance = new TelnetClientUtil(host.getHostIp(),host.getPort(),host.getAccount(), host.getPassword(),taskEntity.getTimeout());
			try{
				instance.connect();
			}catch (Exception e){
				// 连接失败
				ScheduleTargetEntity targetEntity = new ScheduleTargetEntity(scheduleEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SSH_CONNECT_FAILED.getMsg());
				taskTargetList.add(targetEntity);
				instance.disconnect();
				continue;
			}
			// 提交任务至线程池
			completionService.submit(new Callable<ScheduleTargetEntity>() {

				@Override
				public ScheduleTargetEntity call() throws Exception {
					// 上传脚本
					ScheduleTargetEntity targetEntity = new ScheduleTargetEntity(scheduleEntity, host.getHostIp(),
							host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE, null);
					//执行脚本
					String command = instance.sendCommand(new String(fileStream.getBody()));
					instance.disconnect();

					targetEntity.setSuccess(Boolean.TRUE);
					targetEntity.setResult(command);
					return targetEntity;
				}
			});

		}
		return completionService;
	}

}
