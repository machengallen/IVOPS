package com.iv.operation.script.controller;

import com.iv.common.dto.IdList;
import com.iv.common.response.ResponseDto;
import com.iv.operation.script.constant.ErrorMsg;
import com.iv.operation.script.constant.OperatingSystemType;
import com.iv.operation.script.constant.ScriptSourceType;
import com.iv.operation.script.dto.*;
import com.iv.operation.script.entity.SingleTaskEntity;
import com.iv.operation.script.service.OperationScriptQuartzService;
import com.iv.operation.script.service.OperationScriptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 脚本作业API
 * 
 * @author macheng 
 * 2018年5月25日 
 * operation-script-service
 * 
 */
@RestController
@Api(description = "linux系统作业相关接口")
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
	@ApiOperation(value = "单脚本任务新增", notes = "90200")
	@ApiImplicitParams({ @ApiImplicitParam(name = "scriptId", value = "脚本库文件id", dataType = "int", paramType = "query") })
	@PostMapping("/create/single")
	public ResponseDto singleTaskCreate(@RequestParam(required = false) MultipartFile file,
			@RequestParam String taskName, @RequestParam String taskDescription,
			@RequestParam ScriptSourceType scriptSrc, @RequestParam(required = false) String scriptType,
			@RequestParam(required = false) String scriptContext, @RequestParam(required = false) Integer scriptId,
			@RequestParam(required = false) List<String> scriptArgs, @RequestParam int timeout,
										@RequestParam OperatingSystemType systemType) {


		SingleTaskDto scriptDto = new SingleTaskDto(null, taskName, taskDescription, scriptSrc, scriptArgs,
				timeout,systemType);
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
	@ApiOperation(value = "单脚本任务列表查询", notes = "90201")
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
	@ApiOperation(value = "单脚本任务编辑", notes = "90202")
	@ApiImplicitParams({ @ApiImplicitParam(name = "scriptId", value = "脚本库文件id", dataType = "int", paramType = "query") })
	@PostMapping("/modify/single")
	public ResponseDto singleTaskModify(@RequestParam int taskId, @RequestParam(required = false) MultipartFile file,
			@RequestParam String taskName, @RequestParam String taskDescription,
			@RequestParam ScriptSourceType scriptSrc, @RequestParam(required = false) String scriptType,
			@RequestParam(required = false) String scriptContext, @RequestParam Integer scriptId,
			@RequestParam(required = false) List<String> scriptArgs, @RequestParam int timeout,@RequestParam OperatingSystemType systemType) {

		SingleTaskDto scriptDto = new SingleTaskDto(taskId, taskName, taskDescription, scriptSrc, scriptArgs, timeout,systemType);
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
	 * 单脚本任务删除
	 * @param scheduleId
	 * @return
	 */
	@ApiOperation(value = "单脚本任务删除", notes = "90209")
	@PostMapping("/del/single")
	public ResponseDto singleTaskDel(@RequestBody IdList<Integer> taskIds) {

		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(service.singleTaskDel(taskIds));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("任务删除失败", e);
			return ResponseDto.builder(ErrorMsg.DEL_TASK_FAILED);
		}
	}

	/**
	 * 执行单脚本任务
	 * 
	 * @param targetHostsDto
	 * @return
	 */
	@ApiOperation(value = "单次任务执行（立即执行）", notes = "90203")
	@PostMapping("/exec/immediate")
	public ResponseDto immediateExec(@RequestBody ImmediateHostsDto targetHostsDto) {

		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(service.excute(targetHostsDto));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("任务执行失败", e);
			return ResponseDto.builder(ErrorMsg.EXEC_TASK_FAILED);
		}
	}
	
	/**
	 * 获取单次任务执行结果
	 * 
	 * @param taskId
	 * @return
	 */
	@ApiOperation(value = "单次任务最近执行结果查询", notes = "90204")
	@GetMapping("/get/immediate/results")
	public ResponseDto taskTargetGet(@RequestParam int taskId) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(service.taskTargetGet(taskId));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("获取任务执行对象失败", e);
			return ResponseDto.builder(ErrorMsg.GET_TASKS_TARGET_FAILED);
		}
	}
	
	/**
	 * 获取定时作业执行结果
	 * 
	 * @param taskId
	 * @return
	 */
	@ApiOperation(value = "定时作业最近执行结果查询", notes = "90213")
	@GetMapping("/get/schedule/results")
	public ResponseDto scheduleTargetGet(@RequestParam int scheduleId) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(quartzService.scheduleTargetGet(scheduleId));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("获取任务执行结果失败", e);
			return ResponseDto.builder(ErrorMsg.GET_TASKS_TARGET_FAILED);
		}
	}
	
	/**
	 * 定时任务执行策略创建
	 * @param taskId
	 * @param cronExp
	 * @return
	 */
	@ApiOperation(value = "定时作业规则新增", notes = "90205")
	@GetMapping("/create/schedule")
	public ResponseDto scheduleCreate(@RequestParam int taskId, @RequestParam String cronExp, @RequestParam String name) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(quartzService.singleTaskSchedule(taskId, cronExp, name));
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
	@ApiOperation(value = "指定脚本任务的所有定时作业查询", notes = "90206")
	@GetMapping("/get/single/schedules")
	public ResponseDto scheduleGetByTask(@RequestParam int taskId) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(quartzService.scheduleGetByTask(taskId));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("获取定时策略失败", e);
			return ResponseDto.builder(ErrorMsg.GET_DATA_FAILED);
		}
	}
	
	/**
	 * 查询脚本任务的定时策略
	 * @param taskId
	 * @return
	 */
	@ApiOperation(value = "定时作业列表查询", notes = "90213")
	@PostMapping("/get/schedule")
	public ResponseDto scheduleGetPage(@RequestBody ScheduleQueryDto queryDto) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(quartzService.scheduleGetPage(queryDto));
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
	@ApiOperation(value = "定时作业规则编辑", notes = "90207")
	@GetMapping("/modify/schedule")
	public ResponseDto scheduleModify(@RequestParam int scheduleId, @RequestParam String cronExp, @RequestParam String name) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(quartzService.singleTaskScheduleMod(scheduleId, cronExp, name));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("定时任务创建失败", e);
			return ResponseDto.builder(ErrorMsg.MOD_TASK_SCHEDULE_FAILED);
		}
	}
	
	/**
	 * 定时规则删除
	 * @param scheduleId
	 * @return
	 */
	@ApiOperation(value = "定时作业删除", notes = "90210")
	@PostMapping("/del/schedule")
	public ResponseDto scheduleDel(@RequestBody IdList<Integer> scheduleIds) {
		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			responseDto.setData(quartzService.scheduleDel(scheduleIds));
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("定时作业删除失败", e);
			return ResponseDto.builder(ErrorMsg.DEL_SCHEDULE_FAILED);
		}
	}
	
	/**
	 * 定时作业执行
	 * @param targetHostsDto
	 * @return
	 */
	@ApiOperation(value = "定时作业执行", notes = "90208")
	@PostMapping("/exec/schedule")
	public ResponseDto scheduleExec(@RequestBody ScheduleHostsDto scheduleHostsDto) {

		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			quartzService.scheduleExec(scheduleHostsDto);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("任务执行失败", e);
			return ResponseDto.builder(ErrorMsg.EXEC_TASK_FAILED);
		}
	}
	
	/**
	 * 定时作业暂停
	 * @param targetHostsDto
	 * @return
	 */
	@ApiOperation(value = "定时作业暂停", notes = "90211")
	@GetMapping("/pause/schedule")
	public ResponseDto schedulePause(@RequestParam int scheduleId) {

		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			quartzService.schedulePause(scheduleId);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("定时作业暂停失败", e);
			return ResponseDto.builder(ErrorMsg.PAU_SCHEDULE_FAILED);
		}
	}
	
	/**
	 * 定时作业重启
	 * @param targetHostsDto
	 * @return
	 */
	@ApiOperation(value = "定时作业重启", notes = "90212")
	@GetMapping("/resume/schedule")
	public ResponseDto scheduleResume(@RequestParam int scheduleId) {

		try {
			ResponseDto responseDto = ResponseDto.builder(ErrorMsg.OK);
			quartzService.scheduleResume(scheduleId);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("定时作业重启失败", e);
			return ResponseDto.builder(ErrorMsg.RES_SCHEDULE_FAILED);
		}
	}
	
}
