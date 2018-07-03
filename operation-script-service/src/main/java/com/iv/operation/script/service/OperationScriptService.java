package com.iv.operation.script.service;

import com.iv.common.dto.BatchResultDto;
import com.iv.common.dto.IdList;
import com.iv.common.util.spring.JWTUtil;
import com.iv.operation.script.constant.ErrorMsg;
import com.iv.operation.script.constant.OperatingSystemType;
import com.iv.operation.script.constant.ScriptSourceType;
import com.iv.operation.script.dao.impl.ImmediateTargetDaoImpl;
import com.iv.operation.script.dao.impl.SingleTaskDaoImpl;
import com.iv.operation.script.dao.impl.SingleTaskLifeDaoImpl;
import com.iv.operation.script.dto.*;
import com.iv.operation.script.entity.ImmediateTargetEntity;
import com.iv.operation.script.entity.SingleTaskEntity;
import com.iv.operation.script.entity.SingleTaskLifeEntity;
import com.iv.operation.script.feign.client.IScriptServiceClient;
import com.iv.operation.script.util.*;
import com.iv.script.api.dto.ScriptDto;
import com.iv.script.api.dto.TemporaryScriptDto;
import com.jcraft.jsch.Session;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;



/**
 * @author macheng
 * 2018年6月4日
 * operation-script-service
 * 单脚本任务管理服务
 */
@Service
public class OperationScriptService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperationScriptService.class);

	@Autowired
	private OperationScriptQuartzService quartzService;
	@Autowired
	private SingleTaskDaoImpl singleTaskDao;
	@Autowired
	private SingleTaskLifeDaoImpl singleTaskLifeDao;
	@Autowired
	private IScriptServiceClient scriptServiceClient;
	@Autowired
	private ImmediateTargetDaoImpl immediateTargetDao;

	/**
	 * 存储单脚本任务
	 * 
	 * @param dto
	 * @param scriptId
	 */
	private SingleTaskEntity taskSaveOrUpdate(SingleTaskDto dto, int scriptId) {
		SingleTaskEntity scriptEntity = null;
		SingleTaskLifeEntity lifeEntity = null;
		String user = JWTUtil.getReqValue("realName");
		long date = System.currentTimeMillis();
		if (null != dto.getTaskId()) {
			scriptEntity = singleTaskDao.selectById(dto.getTaskId());
			lifeEntity = scriptEntity.getTaskLife();
		} else {
			scriptEntity = new SingleTaskEntity();
			lifeEntity = new SingleTaskLifeEntity();
			lifeEntity.setCreaDate(date);
			lifeEntity.setCreator(user);
			scriptEntity.setTaskLife(lifeEntity);
		}
		if(null != dto.getTaskId()) {
			scriptEntity.setId(dto.getTaskId());
		}
		scriptEntity.setScriptId(scriptId);
		scriptEntity.setScriptSrc(dto.getScriptSrc());
		scriptEntity.setScriptArgs(dto.getScriptArgs());
		scriptEntity.setTaskDescription(dto.getTaskDescription());
		scriptEntity.setTaskName(dto.getTaskName());
		scriptEntity.setTimeout(dto.getTimeout());
		scriptEntity.setSystemType(dto.getSystemType());
		lifeEntity.setModDate(date);
		lifeEntity.setModifier(user);
		singleTaskDao.save(scriptEntity);

		return scriptEntity;
	}

	/**
	 * 任务创建：来源于脚本库
	 * 
	 * @param dto
	 * @param scriptId
	 */
	public SingleTaskEntity singleTaskCreate(SingleTaskDto dto, int scriptId) {
		return taskSaveOrUpdate(dto, scriptId);
	}

	/**
	 * 任务创建：来源于用户本地文件
	 * 
	 * @param dto
	 * @param file
	 * @throws IOException 
	 * @throws Exception
	 */
	public SingleTaskEntity singleTaskCreate(SingleTaskDto dto, MultipartFile file) throws IOException{
		/*InputStream fileStream;
		try {
			fileStream = file.getInputStream();
		} catch (IOException e) {
			LOGGER.error("文件流读取失败", e);
			return null;
		}*/
		String fileName = file.getOriginalFilename();
		String scriptType = fileName.substring(fileName.lastIndexOf(".") + 1);
		int scriptId = scriptServiceClient.tempWrite(fileName, scriptType, file.getBytes(), null);
		if(0 == scriptId) {
			throw new IOException("临时脚本存储失败");
		}
		return taskSaveOrUpdate(dto, scriptId);
	}

	/**
	 * 任务创建：来源于用户在线录入
	 * 
	 * @param dto
	 * @param context
	 */
	public SingleTaskEntity singleTaskCreate(SingleTaskDto dto, String context, String scriptType) {
		//InputStream fileStream = new ByteArrayInputStream(context.getBytes());
		int scriptId = scriptServiceClient.tempWrite(null, scriptType, context.getBytes(), null);
		return taskSaveOrUpdate(dto, scriptId);
	}
	
	/**
	 * 单脚本任务编辑
	 * 
	 * @param dto
	 * @param scriptId
	 * @param file
	 * @param context
	 * @param scriptType
	 * @return
	 * @throws IOException 
	 */
	public SingleTaskEntity singleTaskModify(SingleTaskDto dto, int taskId, int scriptId, MultipartFile file,
			String context, String scriptType) throws IOException {

		SingleTaskEntity taskEntity = singleTaskDao.selectById(dto.getTaskId());
		switch (dto.getScriptSrc()) {
		case SCRIPT_LIBRARY:
			if (taskEntity.getScriptSrc().name().equals(dto.getScriptSrc().name())) {
				if(taskEntity.getScriptId() == scriptId) {
					taskEntity = taskSaveOrUpdate(dto, scriptId);// 未修改脚本
				}
			}else {
				taskEntity = singleTaskCreate(dto, scriptId);
			}
			return taskEntity;
			
		case USER_LOCAL_LIBRARY:
			return singleTaskCreate(dto, file);
			
		case USER_ONLINE_EDIT:
			return singleTaskCreate(dto, context, scriptType);
					
		default:
			return null;
		}
	}
	
	public BatchResultDto<Integer> singleTaskDel(IdList<Integer> taskIds) throws SchedulerException {
		BatchResultDto<Integer> resultDto = new BatchResultDto<>();
		List<Integer> failedIds = new ArrayList<>();
		// 删除quartz定时作业
		for (Integer taskId : taskIds.getIds()) {
			try {
				quartzService.removeSchedulerTask(taskId);
				immediateTargetDao.delByTaskId(taskId);
				singleTaskDao.delById(taskId);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				failedIds.add(taskId);
			}
		}
		resultDto.setFailedMsg(failedIds);
		resultDto.setTotal(taskIds.getIds().size());
		resultDto.setFailed(failedIds.size());
		return resultDto;
	}
	
	/**
	 * 单次任务执行
	 * @param targetHostsDto
	 * @return
	 */
	public List<ImmediateTargetEntity> excute (ImmediateHostsDto targetHostsDto) {
		SingleTaskEntity taskEntity = singleTaskDao.selectById(targetHostsDto.getTaskId());
		List<ImmediateTargetEntity> taskTargetList = new ArrayList<ImmediateTargetEntity>();
		OperatingSystemType systemType = taskEntity.getSystemType();
		CompletionService<ImmediateTargetEntity> completionService;
		switch (systemType){
			case LINUX:
				completionService = doTask(taskEntity, targetHostsDto.getTargetHosts(), taskTargetList);
				break;
            case WINDOWS:
                completionService = doTaskWin(taskEntity, targetHostsDto.getTargetHosts(), taskTargetList);
                break;
            default:
                completionService = doTask(taskEntity, targetHostsDto.getTargetHosts(), taskTargetList);
                break;
		}


		// 获取任务结果集
		int count = targetHostsDto.getTargetHosts().size() - taskTargetList.size();
		for (int j = 1; j <= count; j++) {
			try {
				Future<ImmediateTargetEntity> future = completionService.take();// 阻塞等待第一个结果，返回后该结果从队列删除
				taskTargetList.add(future.get());// get不会阻塞
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage());
			} catch (ExecutionException e) {
				LOGGER.error(e.getMessage());
			}
		}
		immediateTargetDao.delByTaskId(targetHostsDto.getTaskId());// 删除上次执行任务结果
		immediateTargetDao.batchSave(taskTargetList);

		// 更新任务生命周期
		SingleTaskLifeEntity lifeEntity = taskEntity.getTaskLife();
		lifeEntity.execNumAdd();
		//lifeEntity.setExecDate(System.currentTimeMillis());
		//lifeEntity.setExecutor(JWTUtil.getReqValue("realName"));
		singleTaskLifeDao.save(lifeEntity);
		return taskTargetList;
	}
	
	private CompletionService<ImmediateTargetEntity> doTask(SingleTaskEntity taskEntity, List<HostDto> targetHosts, List<ImmediateTargetEntity> taskTargetList) {
		CompletionService<ImmediateTargetEntity> completionService = getExecutorService(targetHosts.size());// 线程提交服务
		for (HostDto host : targetHosts) {
			ResponseEntity<byte[]> fileStream = getFileStream(taskEntity.getScriptSrc(), taskEntity.getScriptId());// 获取执行脚本内容流
			if(null == fileStream) {
				// 没有该文件或脚本库服务未响应
				ImmediateTargetEntity targetEntity = new ImmediateTargetEntity(taskEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SCRIPT_NOT_EXIST.getMsg());
				taskTargetList.add(targetEntity);
				continue;
			}
			String fileName = getFileName(taskEntity.getScriptSrc(), taskEntity.getScriptId());
			if(StringUtils.isEmpty(fileStream)) {
				ImmediateTargetEntity targetEntity = new ImmediateTargetEntity(taskEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SCRIPT_NOT_EXIST.getMsg());
				taskTargetList.add(targetEntity);
				continue;
			}
			Session session = getSession(host.getHostIp(), host.getAccount(), host.getPassword(), host.getPort());
			if (null == session) {
				// 获取ssh连接失败
				ImmediateTargetEntity targetEntity = new ImmediateTargetEntity(taskEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SSH_CONNECT_FAILED.getMsg());
				taskTargetList.add(targetEntity);
				continue;
			}
			// 提交任务至线程池
			completionService.submit(new Callable<ImmediateTargetEntity>() {

				@Override
				public ImmediateTargetEntity call() throws Exception {
					// 上传脚本
					ImmediateTargetEntity targetEntity = new ImmediateTargetEntity(taskEntity, host.getHostIp(),
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
	
	private String getFileName(ScriptSourceType scriptType, int scriptId) {
		switch (scriptType) {
		case SCRIPT_LIBRARY:
			ScriptDto scriptDto = scriptServiceClient
			.scriptInfoById(scriptId);
			return scriptDto == null ? null : scriptDto.getName();
		case USER_LOCAL_LIBRARY:
		case USER_ONLINE_EDIT:
			TemporaryScriptDto temporaryScriptDto = scriptServiceClient
			.temporaryScriptInfoById(scriptId);
			return temporaryScriptDto == null ? null : temporaryScriptDto.getName();
		default:
			return null;
		}
	}
	
	private <T> CompletionService<T> getExecutorService(int elements){
		final BlockingDeque<Future<T>> blockingDeque = new LinkedBlockingDeque<Future<T>>(elements);
		final CompletionService<T> completionService = new ExecutorCompletionService<T>(
				ThreadPoolUtil.getInstance(), blockingDeque);
		return completionService;
	}
	
	public SingleTaskPageDto singleTaskGet(SingleTaskQueryDto queryDto) {

		return singleTaskDao.selectByCondition(queryDto);
	}

	public List<ImmediateTargetEntity> taskTargetGet(int taskId) {

		return immediateTargetDao.selectByTaskId(taskId);
	}


	/***
	 * windows执行命令
	 * @param taskEntity
	 * @param targetHosts
	 * @param taskTargetList
	 * @return
	 */
	private CompletionService<ImmediateTargetEntity> doTaskWin(SingleTaskEntity taskEntity, List<HostDto> targetHosts, List<ImmediateTargetEntity> taskTargetList) {
		CompletionService<ImmediateTargetEntity> completionService = getExecutorService(targetHosts.size());// 线程提交服务
		for (HostDto host : targetHosts) {
			ResponseEntity<byte[]> fileStream = getFileStream(taskEntity.getScriptSrc(), taskEntity.getScriptId());// 获取执行脚本内容流
			if(null == fileStream) {
				// 没有该文件或脚本库服务未响应
				ImmediateTargetEntity targetEntity = new ImmediateTargetEntity(taskEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SCRIPT_NOT_EXIST.getMsg());
				taskTargetList.add(targetEntity);
				continue;
			}
			TemporaryScriptDto temporaryScriptDto = scriptServiceClient.temporaryScriptInfoById(taskEntity.getScriptId());
			if(null == temporaryScriptDto) {
				ImmediateTargetEntity targetEntity = new ImmediateTargetEntity(taskEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SCRIPT_NOT_EXIST.getMsg());
				taskTargetList.add(targetEntity);
				continue;
			}
			String fileName = temporaryScriptDto.getName();

			//win telnet协议
			TelnetClientUtil instance = new TelnetClientUtil(host.getHostIp(),host.getPort(),host.getAccount(), host.getPassword(),taskEntity.getTimeout());
			try{
				 instance.connect();
			}catch (Exception e){
				// 连接失败
				ImmediateTargetEntity targetEntity = new ImmediateTargetEntity(taskEntity, host.getHostIp(),
						host.getPort(), host.getAccount(), host.getPassword(), Boolean.FALSE,
						ErrorMsg.SSH_CONNECT_FAILED.getMsg());
				taskTargetList.add(targetEntity);
				instance.disconnect();
				continue;
			}

			// 提交任务至线程池
			completionService.submit(new Callable<ImmediateTargetEntity>() {

				@Override
				public ImmediateTargetEntity call() throws Exception {

					ImmediateTargetEntity targetEntity = new ImmediateTargetEntity(taskEntity, host.getHostIp(),
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
