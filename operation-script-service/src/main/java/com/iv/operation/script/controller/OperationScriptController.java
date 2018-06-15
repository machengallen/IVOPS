package com.iv.operation.script.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iv.common.response.ResponseDto;
import com.iv.operation.script.dto.ScheduleHostsDto;
import com.iv.operation.script.dto.SingleTaskDto;
import com.iv.operation.script.dto.SingleTaskQueryDto;
import com.iv.operation.script.dto.TargetHostsDto;
import com.iv.operation.script.entity.SingleTaskEntity;
import com.iv.operation.script.service.OperationScriptQuartzService;
import com.iv.operation.script.service.OperationScriptService;
import com.iv.operation.script.util.ErrorMsg;
import com.iv.operation.script.util.ScriptSourceType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * 脚本作业API
 * 
 * @author macheng 2018年5月25日 operation-script-service
 * 
 */
@RestController
@Api(description = "脚本作业相关接口")
public class OperationScriptController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperationScriptController.class);
	@Autowired
	private OperationScriptService service;
	@Autowired
	private OperationScriptQuartzService quartzService;

	/**
	 * 创建单脚本任务
	 * 
	 * @param file
	 * @param taskName
	 * @param taskDescription
	 * @param scriptSrc
	 * @param scriptType
	 * @param scriptContext
	 * @param scriptId
	 * @param scriptArgs
	 * @param timeout
	 * @return
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "scriptId", value = "脚本库文件id", dataType = "int", paramType = "query") })
	@PostMapping("/create/single")
	public ResponseDto singleTaskCreate(@RequestParam(required = false) MultipartFile file,
			@RequestParam String taskName, @RequestParam String taskDescription,
			@RequestParam ScriptSourceType scriptSrc, @RequestParam(required = false) String scriptType,
			@RequestParam(required = false) String scriptContext, @RequestParam(required = false) Integer scriptId,
			@RequestParam(required = false) List<String> scriptArgs, @RequestParam int timeout) {

		SingleTaskDto scriptDto = new SingleTaskDto(null, taskName, taskDescription, scriptSrc, scriptArgs,
				timeout);
		try {
			SingleTaskEntity taskEntity = null;
			switch (scriptSrc) {
			case SCRIPT_LIBRARY:
				taskEntity = service.singleTaskCreate(scriptDto, scriptId);
				break;
			case USER_LOCAL_LIBRARY:
				taskEntity = service.singleTaskCreate(scriptDto, file);
				break;
			case USER_ONLINE_EDIT:
				taskEntity = service.singleTaskCreate(scriptDto, scriptContext, scriptType);
				break;
			default:
				return ResponseDto.builder(ErrorMsg.SCRIPT_SRCTYPE_UNKNOW);
			}

			if(null == taskEntity) {
				return ResponseDto.builder(ErrorMsg.CREATE_TASK_FAILED);
			}
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(taskEntity);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("任务创建失败", e);
			return ResponseDto.builder(ErrorMsg.CREATE_TASK_FAILED);
		}
	}
	
	/**
	 * 查询单脚本任务列表
	 * 
	 * @param queryDto
	 * @return
	 */
	@PostMapping("/get/single")
	public ResponseDto singleTaskGet(@RequestBody SingleTaskQueryDto queryDto) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(service.singleTaskGet(queryDto));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("获取任务列表失败", e);
			return ResponseDto.builder(ErrorMsg.GET_DATA_FAILED);
		}
	}
	
	/**
	 * 任务编辑
	 * 
	 * @return
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "scriptId", value = "脚本库文件id", dataType = "int", paramType = "query") })
	@PostMapping("/modify/single")
	public ResponseDto singleTaskModify(@RequestParam int taskId, @RequestParam(required = false) MultipartFile file,
			@RequestParam String taskName, @RequestParam String taskDescription,
			@RequestParam ScriptSourceType scriptSrc, @RequestParam(required = false) String scriptType,
			@RequestParam(required = false) String scriptContext, @RequestParam Integer scriptId,
			@RequestParam(required = false) List<String> scriptArgs, @RequestParam int timeout) {

		SingleTaskDto scriptDto = new SingleTaskDto(taskId, taskName, taskDescription, scriptSrc, scriptArgs, timeout);
		try {
			SingleTaskEntity taskEntity = service.singleTaskModify(scriptDto, taskId, scriptId, file, scriptContext, scriptType);
			if(null == taskEntity) {
				return ResponseDto.builder(ErrorMsg.MOD_TASK_FAILED);
			}
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(taskEntity);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("任务编辑失败", e);
			return ResponseDto.builder(ErrorMsg.MOD_TASK_FAILED);
		}
	}

	/**
	 * 执行单脚本任务
	 * 
	 * @param targetHostsDto
	 * @return
	 */
	@PostMapping("/exec/immediate")
	public ResponseDto singleTaskExec(@RequestBody TargetHostsDto targetHostsDto) {

		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(service.singleTaskExec(targetHostsDto));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("任务执行失败", e);
			return ResponseDto.builder(ErrorMsg.EXEC_TASK_FAILED);
		}
	}
	
	/**
	 * 获取任务执行对象列表
	 * 
	 * @param taskId
	 * @return
	 */
	@GetMapping("/get/single/targets")
	public ResponseDto singleTaskTargetGet(@RequestParam int taskId) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(service.singleTaskTargetGet(taskId));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("获取任务执行对象失败", e);
			return ResponseDto.builder(ErrorMsg.GET_TASKS_TARGET_FAILED);
		}
	}
	
	/**
	 * 定时任务执行策略创建
	 * @param taskId
	 * @param cronExp
	 * @return
	 */
	@GetMapping("/create/schedule")
	public ResponseDto scheduleCreate(@RequestParam int taskId, @RequestParam String cronExp) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(quartzService.singleTaskSchedule(taskId, cronExp));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("定时任务创建失败", e);
			return ResponseDto.builder(ErrorMsg.CREATE_TASK_SCHEDULE_FAILED);
		}
	}
	
	/**
	 * 查询脚本任务的定时策略
	 * @param taskId
	 * @return
	 */
	@GetMapping("/get/schedule")
	public ResponseDto scheduleGet(@RequestParam int taskId) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(quartzService.scheduleGet(taskId));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("获取定时策略失败", e);
			return ResponseDto.builder(ErrorMsg.GET_DATA_FAILED);
		}
	}
	
	/**
	 * 定时任务规则修改
	 * @param taskId
	 * @param cronExp
	 * @return
	 */
	@GetMapping("/modify/schedule")
	public ResponseDto scheduleModify(@RequestParam int scheduleId, @RequestParam String cronExp) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(quartzService.singleTaskSchedule(scheduleId, cronExp));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("定时任务创建失败", e);
			return ResponseDto.builder(ErrorMsg.MOD_TASK_SCHEDULE_FAILED);
		}
	}
	
	/**
	 * 定时任务执行
	 * @param targetHostsDto
	 * @return
	 */
	@PostMapping("/exec/schedule")
	public ResponseDto scheduleExec(@RequestBody ScheduleHostsDto scheduleHostsDto) {

		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			quartzService.singleTaskExec(scheduleHostsDto);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("任务执行失败", e);
			return ResponseDto.builder(ErrorMsg.EXEC_TASK_FAILED);
		}
	}
	
}
