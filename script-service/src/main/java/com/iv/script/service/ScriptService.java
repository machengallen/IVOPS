package com.iv.script.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.iv.common.enumeration.ItemType;
import com.iv.common.enumeration.OpsType;
import com.iv.common.enumeration.WorkflowType;
import com.iv.common.enumeration.YesOrNo;
import com.iv.common.response.ResponseDto;
import com.iv.message.api.constant.MsgType;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.script.api.constant.ErrorMsg;
import com.iv.script.api.dto.ProcessDataDto;
import com.iv.script.api.dto.ScriptDto;
import com.iv.script.api.dto.ScriptQueryDto;
import com.iv.script.api.dto.ScriptTypeCountDto;
import com.iv.script.binding.BinderConfiguration;
import com.iv.script.dao.AuthorDaoImpl;
import com.iv.script.dao.ScriptDaoImpl;
import com.iv.script.dto.HisProScriptApplyReq;
import com.iv.script.dto.ScriptDetailInfoDto;
import com.iv.script.dto.TaskScriptStoreApplyResp;
import com.iv.script.entity.AuthorEntity;
import com.iv.script.entity.ScriptEntity;
import com.iv.script.entity.ScriptLogEntity;
import com.iv.script.entity.ScriptPagingWrap;
import com.iv.script.feign.client.IUserServiceClient;
import com.iv.script.util.FileReaderUtil;

@Service
public class ScriptService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptService.class);
	@Autowired
	private ScriptDaoImpl docScriptDaoImpl;
	@Autowired
	private IUserServiceClient userServiceClient;
	@Autowired
	private AuthorDaoImpl authorDaoImpl;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private BinderConfiguration msgSender;

	@Value("${iv.script.repositoryPath}")
	private String scriptRepositoryPath;

	@Transactional
	public ResponseDto fileUpload(MultipartFile multipartFile, int userId, String alias, ItemType itemType,
			String remark) {

		if (!multipartFile.isEmpty()) {
			String originalFilename = multipartFile.getOriginalFilename();
			int index = originalFilename.lastIndexOf(".");
			String type = originalFilename.substring(index + 1);
			// 封装作者信息
			LocalAuthDto authDto = userServiceClient.selectLocalAuthById(userId);
			AuthorEntity author = authorDaoImpl.selectById(userId);
			if (null == author) {
				author = new AuthorEntity();
				author.setUserId(userId);
				author.setRealName(authDto.getRealName());
				author.setEmail(authDto.getEmail());
				author.setTel(authDto.getTel());
			}
			// 存储文件信息至db
			ScriptEntity scriptInfo = upload2db(author, itemType, type, alias, remark);
			// 存储文件至文件系统
			try {
				multipartFile.transferTo(new File(scriptRepositoryPath + itemType + "\\" + scriptInfo.getName()));
			} catch (IllegalStateException | IOException e) {
				// 回滚数据库文件信息
				LOGGER.error("文件保存失败", e);
				docScriptDaoImpl.delByName(scriptInfo.getName());
				return ResponseDto.builder(ErrorMsg.UPLOAD_FAILED);
			}
			return scriptApply(userId, scriptInfo.getId());
			// return ResponseDto.builder(ErrorMsg.OK);

		} else {
			return ResponseDto.builder(ErrorMsg.SCRIPT_NOT_EMPTY);
		}
	}

	@Transactional
	public ResponseDto textUpload(String text, String type, String alias, int userId, ItemType itemType,
			String remark) {

		if (!StringUtils.isEmpty(text)) {
			// 封装作者信息
			LocalAuthDto authDto = userServiceClient.selectLocalAuthById(userId);
			AuthorEntity author = authorDaoImpl.selectById(userId);
			if (null == author) {
				author = new AuthorEntity();
				author.setUserId(userId);
				author.setRealName(authDto.getRealName());
				author.setEmail(authDto.getEmail());
				author.setTel(authDto.getTel());
			}
			// 存储文件信息至db
			ScriptEntity scriptInfo = upload2db(author, itemType, type, alias, remark);
			// 存储文件至文件系统
			FileWriter fileWriter = null;
			try {
				File file = new File(scriptRepositoryPath + itemType + "\\" + scriptInfo.getName());
				file.createNewFile();
				fileWriter = new FileWriter(file, true);
				fileWriter.write(text);
				fileWriter.flush();
			} catch (IllegalStateException | IOException e) {
				// 回滚数据库文件信息
				LOGGER.error("文件保存失败", e);
				docScriptDaoImpl.delByName(scriptInfo.getName());
				return ResponseDto.builder(ErrorMsg.UPLOAD_FAILED);
			} finally {
				try {
					fileWriter.close();
				} catch (IOException e) {
					LOGGER.error("文件流关闭失败", e);
				}
			}

			// return ResponseDto.builder(ErrorMsg.OK);
			return scriptApply(userId, scriptInfo.getId());
		} else {
			return ResponseDto.builder(ErrorMsg.SCRIPT_NOT_EMPTY);
		}
	}

	@Transactional
	public ResponseDto textUpdate(int scriptId, String text, String type, String alias, int modifierId,
			ItemType itemType, String remark) {
		if (StringUtils.isEmpty(text)) {
			// 文本内容为空
			return ResponseDto.builder(ErrorMsg.SCRIPT_NOT_EMPTY);
		}

		ScriptEntity scriptEntity = docScriptDaoImpl.selectById(scriptId);
		if (null == scriptEntity) {
			// 当前编辑脚本不存在
			return ResponseDto.builder(ErrorMsg.SCRIPT_NOT_EXIST);
		}
		// 封装作者信息
		LocalAuthDto authDto = userServiceClient.selectLocalAuthById(modifierId);
		AuthorEntity modifier = authorDaoImpl.selectById(modifierId);
		if (null == modifier) {
			modifier = new AuthorEntity();
			modifier.setUserId(modifierId);
			modifier.setRealName(authDto.getRealName());
			modifier.setEmail(authDto.getEmail());
			modifier.setTel(authDto.getTel());
		}
		// 更新文件信息至db
		scriptEntity.setAlias(alias);
		scriptEntity.setItemType(itemType);
		scriptEntity.setModDate(System.currentTimeMillis());
		scriptEntity.setModifier(modifier);
		scriptEntity.setType(type);
		scriptEntity.setIfReviewed(YesOrNo.NO);// 是否审批通过
		scriptEntity.setRemark(remark);// 脚本说明
		// 更新日志信息
		Set<ScriptLogEntity> logs = scriptEntity.getScriptLog();
		ScriptLogEntity log = new ScriptLogEntity();
		long time = System.currentTimeMillis();
		log.setOpsDate(time);
		log.setOpsType(OpsType.EDIT);
		log.setOpsUser(modifier.getRealName());
		logs.add(log);
		scriptEntity.setScriptLog(logs);
		docScriptDaoImpl.save(scriptEntity);
		// 存储文件至文件系统
		FileWriter fileWriter = null;
		try {
			File file = new File(scriptRepositoryPath + itemType + "\\" + scriptEntity.getName());
			if (file.exists()) {
				// file.delete();
				fileWriter = new FileWriter(file, false);
				fileWriter.write(text);
				fileWriter.flush();
			}
		} catch (IllegalStateException | IOException e) {
			// 回滚数据库文件信息
			LOGGER.error("文件保存失败", e);
			docScriptDaoImpl.delByName(scriptEntity.getName());
			return ResponseDto.builder(ErrorMsg.UPDATE_FAILED);
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				LOGGER.error("文件流关闭失败", e);
			}
		}

		return ResponseDto.builder(ErrorMsg.OK);
	}

	private ScriptEntity upload2db(AuthorEntity author, ItemType itemType, String type, String alias, String remark) {
		File file = new File(scriptRepositoryPath + itemType + "\\");
		if (!file.exists()) {
			file.mkdirs();
		}

		long creDate = System.currentTimeMillis();
		StringBuilder nameBuilder = new StringBuilder();
		nameBuilder.append(itemType.name());
		nameBuilder.append("-");
		nameBuilder.append(creDate);
		nameBuilder.append(".");
		nameBuilder.append(type);
		String name = nameBuilder.toString();
		// 文件名重复校验
		/*
		 * if (!cover) { if (null != docScriptDaoImpl.selectByName(alias, type,
		 * tenantId)) { return ResponseDto.builder(ErrorMsg.FILE_EXIST); } }
		 */

		// 存储脚本文件信息
		ScriptEntity docInfoEntity = new ScriptEntity();
		docInfoEntity.setAlias(alias);
		docInfoEntity.setItemType(itemType);
		docInfoEntity.setName(name);
		docInfoEntity.setType(type);
		docInfoEntity.setCreDate(creDate);
		docInfoEntity.setCreator(author);
		docInfoEntity.setIfReviewed(YesOrNo.NO);// 是否审批通过
		docInfoEntity.setRemark(remark);// 脚本说明
		// 日志存储
		Set<ScriptLogEntity> logs = new TreeSet<ScriptLogEntity>();
		ScriptLogEntity log = new ScriptLogEntity();
		long time = System.currentTimeMillis();
		log.setOpsDate(time);
		log.setOpsType(OpsType.CREATE);
		log.setOpsUser(author.getRealName());
		logs.add(log);
		docInfoEntity.setScriptLog(logs);
		ScriptEntity scriptEntity = docScriptDaoImpl.save(docInfoEntity);

		return scriptEntity;

	}

	public ScriptPagingWrap fileList(ScriptQueryDto query) {

		// 脚本文件库信息保存在租户db
		List<AuthorEntity> auths = null;
		if (!StringUtils.isEmpty(query.getCreator())) {
			auths = authorDaoImpl.selectByRealName(query.getCreator());
			if (CollectionUtils.isEmpty(auths)) {
				ScriptPagingWrap dto = new ScriptPagingWrap();
				dto.setTotalCount(0);
				dto.setEntities(new ArrayList<>());
				return dto;
			}
		}
		return docScriptDaoImpl.selectByCondition(query, auths);
	}

	public ResponseDto download(int id, HttpServletResponse response) {

		ScriptEntity docInfoEntity = docScriptDaoImpl.selectById(id);
		File file = new File(scriptRepositoryPath + docInfoEntity.getItemType() + "\\" + docInfoEntity.getName());
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setHeader("Content-Disposition", "attachment;filename=" + docInfoEntity.getName());
		response.setCharacterEncoding("UTF-8");
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(file));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (IOException e) {
			LOGGER.error("IO异常", e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					LOGGER.error("IO异常", e);
				}
			}
		}
		return null;
	}

	public ResponseEntity<byte[]> editor(int id) throws IOException {

		ScriptEntity docInfoEntity = docScriptDaoImpl.selectById(id);
		File file = new File(scriptRepositoryPath + docInfoEntity.getItemType() + "\\" + docInfoEntity.getName());
		byte[] body = null;
		InputStream is = new FileInputStream(file);
		body = new byte[is.available()];
		is.read(body);
		is.close();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Content-Disposition", "attchement;filename=" + docInfoEntity.getName());
		HttpStatus statusCode = HttpStatus.OK;
		ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
		return entity;
	}

	public ResponseDto fileDelete(List<Integer> ids) {

		List<Integer> failedIds = new ArrayList<Integer>();
		for (Integer id : ids) {

			ScriptEntity docInfoEntity = docScriptDaoImpl.selectById(id);
			if (null == docInfoEntity) {
				failedIds.add(id);
				continue;
			}

			if (FileReaderUtil.deleteFile(scriptRepositoryPath + docInfoEntity.getName())) {
				docScriptDaoImpl.delById(id);
			} else {
				failedIds.add(id);
			}
		}

		if (!CollectionUtils.isEmpty(failedIds)) {
			ResponseDto responseDto = new ResponseDto();
			responseDto.setErrorMsg(ErrorMsg.SCRIPT_DEL_PARTIAL_FAILED);
			responseDto.setData(failedIds);
			return responseDto;
		} else {
			return ResponseDto.builder(ErrorMsg.OK);
		}

	}

	/**
	 * 脚本库文件信息查询
	 * 
	 * @param scriptId
	 * @return
	 */
	public ScriptDto scriptInfoById(int scriptId) {
		ScriptEntity script = docScriptDaoImpl.selectById(scriptId);
		ScriptDto scriptDto = new ScriptDto();
		BeanCopier copy = BeanCopier.create(ScriptEntity.class, ScriptDto.class, false);
		copy.copy(script, scriptDto, null);
		return scriptDto;
	}

	/**
	 * 领域脚本量统计
	 * 
	 * @return
	 */
	public List<ScriptTypeCountDto> itemTypeCount() {
		List<Object[]> objects = docScriptDaoImpl.itemTypeCount();
		List<ScriptTypeCountDto> dtos = new ArrayList<ScriptTypeCountDto>();
		ItemType[] itemTypes = ItemType.values();
		for (ItemType itemType : itemTypes) {
			ScriptTypeCountDto scriptTypeCountDto = new ScriptTypeCountDto();
			scriptTypeCountDto.setItemType(itemType);
			for (Object[] object : objects) {
				if (itemType.equals((ItemType) object[1])) {
					scriptTypeCountDto.setCount((long) object[0]);
				}
			}
			dtos.add(scriptTypeCountDto);
		}

		return dtos;
	}

	/**
	 * 个人贡献量统计
	 * 
	 * @param userId
	 * @return
	 */
	public long personalScriptCount(int userId) {
		return docScriptDaoImpl.personalScriptCount(userId);
	}

	/**
	 * 提交脚本审批
	 * 
	 * @param userId
	 * @param scriptId
	 * @return
	 */
	public ResponseDto scriptApply(int userId, int scriptId) {
		Map<String, Object> variables = new HashMap<String, Object>();
		ScriptEntity scriptInfo = docScriptDaoImpl.selectById(scriptId);
		variables.put("scriptInfo", scriptInfo);
		variables.put("userId", userId);
		identityService.setAuthenticatedUserId(String.valueOf(userId));
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(WorkflowType.SCRIPT_APPLY.name(),
				variables);
		runtimeService.addUserIdentityLink(instance.getId(), String.valueOf(userId), IdentityLinkType.STARTER);
		return ResponseDto.builder(ErrorMsg.OK);
	}

	/**
	 * 获取企业脚本入库审批人，开启流程实例后由系统调用
	 * 
	 * @param execution
	 * @return
	 */
	public List<String> findScriptApprover(DelegateExecution execution) {
		List<String> approvers = new ArrayList<String>();
		LocalAuthDto localAuthDto = userServiceClient.selectLocalauthInfoByName("admin");
		approvers.add(String.valueOf(localAuthDto.getId()));
		ScriptEntity scriptEntity = (ScriptEntity) execution.getVariable("scriptInfo");
		int applicantId = (int) execution.getVariable("userId");
		LocalAuthDto applicant = userServiceClient.selectLocalAuthById(applicantId);
		// 发送消息通知
		msgSender.pendingMsgSend(Arrays.asList(localAuthDto.getId()),
				applicant.getRealName() + "(" + applicant.getUserName(), scriptEntity.getId(), scriptEntity.getName(),
				MsgType.SCRIPT_NEW_PENDING);
		return approvers;
	}

	/**
	 * 审批脚本申请任务
	 * 
	 * @param taskId
	 */
	public void taskApprove(int userId, String taskId, Boolean approved, String remark) {
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("approved", approved);
		taskVariables.put("remark", remark);
		taskVariables.put("approver", userId);
		taskService.addUserIdentityLink(taskId, String.valueOf(userId), IdentityLinkType.ASSIGNEE);
		taskService.complete(taskId, taskVariables);
	}

	/**
	 * 审批通过后改变脚本入库状态,activit调用
	 * 
	 * @param execution
	 */
	public void editScriptInfo(DelegateExecution execution) {
		Boolean isApprove = (Boolean) execution.getVariable("approved");
		ScriptEntity dto = (ScriptEntity) execution.getVariable("scriptInfo");
		int userId = (int) execution.getVariable("userId");
		String remark = (String) execution.getVariable("remark");
		/*
		 * int userId = (int) execution.getVariable("userId");//申请脚本审批的人 String remark =
		 * (String) execution.getVariable("remark");
		 */
		if (isApprove) {
			dto.setIfReviewed(YesOrNo.YES);
		} else {
			dto.setIfReviewed(YesOrNo.NO);
		}
		docScriptDaoImpl.save(dto);
		// 发送消息通知
		msgSender.resultMsgSend(Arrays.asList(userId), dto.getId(), dto.getName(), isApprove, remark,
				MsgType.SCRIPT_NEW_RESULT);
	}

	/**
	 * 获取待审批脚本入库任务
	 * 
	 * @param assignee
	 * @return
	 * @throws IOException
	 */
	public ProcessDataDto getUserTasksScriptStore(int userId, int first, int max) throws IOException {
		long totalNum = taskService.createTaskQuery().processDefinitionKey(WorkflowType.SCRIPT_APPLY.name())
				.taskCandidateUser(String.valueOf(userId)).count();
		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(WorkflowType.SCRIPT_APPLY.name())
				.taskCandidateUser(String.valueOf(userId)).orderByTaskCreateTime().desc().listPage(first, max);
		List<TaskScriptStoreApplyResp> list = new ArrayList<TaskScriptStoreApplyResp>();
		for (Task task : tasks) {

			Map<String, Object> variables = taskService.getVariables(task.getId(),
					Arrays.asList("userId", "scriptInfo"));
			int applicantId = (int) variables.get("userId");
			LocalAuthDto applicantDto = userServiceClient.selectLocalAuthById(applicantId);
			ScriptEntity tenantInfoDto = (ScriptEntity) variables.get("scriptInfo");
			ScriptDetailInfoDto scriptDetailInfoDto = new ScriptDetailInfoDto(tenantInfoDto,
					getScriptInfoByte(tenantInfoDto));
			TaskScriptStoreApplyResp dto = new TaskScriptStoreApplyResp(task.getId(), WorkflowType.SCRIPT_APPLY,
					applicantDto.getRealName(), applicantDto.getEmail(), applicantDto.getTel(), scriptDetailInfoDto,
					task.getCreateTime().toLocaleString());
			list.add(dto);

		}

		ProcessDataDto dataDto = new ProcessDataDto();
		dataDto.setData(list);
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	/**
	 * 我发起的脚本申请
	 * 
	 * @param userId
	 * @param first
	 * @param max
	 * @throws IOException
	 */
	public ProcessDataDto getMyScriptApply(int userId, int first, int max) throws IOException {
		long totalNum = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.SCRIPT_APPLY.name()).startedBy(String.valueOf(userId)).count();
		List<HistoricProcessInstance> histotricProcess = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.SCRIPT_APPLY.name()).startedBy(String.valueOf(userId))
				.orderByProcessInstanceStartTime().desc().listPage(first, max);
		List<HisProScriptApplyReq> dtos = getHistoryData(histotricProcess);
		ProcessDataDto dataDto = new ProcessDataDto();
		dataDto.setData(dtos);
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	/**
	 * 获取审批人为当前用户的节点历史
	 * 
	 * @param userId
	 * @return
	 */
	private Set<String> getHisUserTaskActivityInstanceList(int userId) {
		List<HistoricActivityInstance> hisActivityInstanceList = ((HistoricActivityInstanceQuery) historyService
				.createHistoricActivityInstanceQuery().processDefinitionId("SCRIPT_APPLY:1:4")
				/* .processInstanceId(processInstanceId) */.activityName("ApprovalTask")
				.taskAssignee(String.valueOf(userId)).finished().orderByHistoricActivityInstanceEndTime().desc())
						.list();
		Set<String> ids = new HashSet<String>();
		if (!CollectionUtils.isEmpty(hisActivityInstanceList)) {
			// 存在当前用户审批的任务列表
			for (HistoricActivityInstance historicActivityInstance : hisActivityInstanceList) {
				ids.add(historicActivityInstance.getProcessInstanceId());
			}
		}
		return ids;
	}
	/*
	*//**
		 * 获取候选人为当前用户的节点历史
		 * 
		 * @param userId
		 * @return
		 *//*
			 * private Set<String> getHisCandidateActivityInstanceList(int userId) {
			 * //待用户审批的脚本 List<Task> tasks = taskService.createTaskQuery()
			 * .processDefinitionKey(WorkflowType.SCRIPT_APPLY.name())
			 * .taskCandidateUser(String.valueOf(userId)).list(); Set<String> ids = new
			 * HashSet<String>(); if(!CollectionUtils.isEmpty(tasks)) { //存在当前用户审批的任务列表 for
			 * (Task task : tasks) { ids.add(task.getProcessInstanceId()); } } return ids; }
			 */

	/**
	 * 我审批的脚本申请
	 * 
	 * @param userId
	 * @param first
	 * @param max
	 * @throws IOException
	 */
	public ProcessDataDto getMyApprovedScript(int userId, int first, int max) throws IOException {
		// 用户审批过的脚本
		Set<String> ids = getHisUserTaskActivityInstanceList(userId);
		/*
		 * Set<String> ids1 = getHisCandidateActivityInstanceList(userId); Set<String>
		 * idsAll = new HashSet<String>(); idsAll.addAll(ids); idsAll.addAll(ids1);
		 */
		ProcessDataDto dataDto = new ProcessDataDto();
		List<HistoricProcessInstance> histotricProcess = new ArrayList<HistoricProcessInstance>();
		// 无当前用户审批的任务列表
		if (ids.size() == 0) {
			dataDto.setData(histotricProcess);
			dataDto.setTotalNum(0);
			return dataDto;
		}

		long totalNum = ids.size();
		histotricProcess = historyService.createHistoricProcessInstanceQuery().processInstanceIds(ids)
				.orderByProcessInstanceStartTime().desc().listPage(first, max);
		List<HisProScriptApplyReq> dtos = getHistoryData(histotricProcess);
		dataDto.setData(dtos);
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	/**
	 * 将历史工作流封装成历史数据
	 * 
	 * @param histotricProcess
	 * @return
	 * @throws IOException
	 */
	private List<HisProScriptApplyReq> getHistoryData(List<HistoricProcessInstance> histotricProcess)
			throws IOException {
		List<HisProScriptApplyReq> dtos = new ArrayList<HisProScriptApplyReq>();
		if (null == histotricProcess || CollectionUtils.isEmpty(histotricProcess)) {
			return dtos;
		}
		for (HistoricProcessInstance historicProcessInstance : histotricProcess) {
			HisProScriptApplyReq hisProTenantApplyDto = new HisProScriptApplyReq();
			hisProTenantApplyDto.setId(historicProcessInstance.getId());
			hisProTenantApplyDto.setEndTime(historicProcessInstance.getEndTime() == null ? null
					: historicProcessInstance.getEndTime().toLocaleString());
			hisProTenantApplyDto.setStartTime(historicProcessInstance.getStartTime().toLocaleString());
			hisProTenantApplyDto.setDuration(historicProcessInstance.getDurationInMillis() == null ? null
					: String.valueOf(historicProcessInstance.getDurationInMillis() / 1000 / 60));
			hisProTenantApplyDto.setType(historicProcessInstance.getProcessDefinitionKey());
			// historicProcessInstance.getStartUserId();

			List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
					.executionId(historicProcessInstance.getId()).list();
			for (HistoricVariableInstance historicVariableInstance : variables) {
				if ("userId".equals(historicVariableInstance.getVariableName())) {
					LocalAuthDto applicant = userServiceClient
							.selectLocalAuthById((int) historicVariableInstance.getValue());
					hisProTenantApplyDto.setApplicant(applicant.getRealName());
					hisProTenantApplyDto.setEmail(applicant.getEmail());
					hisProTenantApplyDto.setTel(applicant.getTel());
				} else if ("approver".equals(historicVariableInstance.getVariableName())) {
					LocalAuthDto approver = userServiceClient
							.selectLocalAuthById((int) historicVariableInstance.getValue());
					hisProTenantApplyDto.setApprover(approver.getRealName());
				} else if ("scriptInfo".equals(historicVariableInstance.getVariableName())) {
					ScriptEntity scriptEntity = (ScriptEntity) historicVariableInstance.getValue();
					ScriptDetailInfoDto scriptDetailInfoDto = new ScriptDetailInfoDto(scriptEntity,
							getScriptInfoByte(scriptEntity));
					hisProTenantApplyDto.setScriptInfo(scriptDetailInfoDto);
				} else if ("approved".equals(historicVariableInstance.getVariableName())) {
					hisProTenantApplyDto.setApproved((boolean) historicVariableInstance.getValue());
				} else if ("remark".equals(historicVariableInstance.getVariableName())) {
					hisProTenantApplyDto.setRemark((String) historicVariableInstance.getValue());
				}
			}
			/*
			 * Task task =
			 * taskService.createTaskQuery().processInstanceId(historicProcessInstance.getId
			 * ()).singleResult(); if(null != task) {
			 * hisProTenantApplyDto.setTaskId(task.getId()); }
			 */
			dtos.add(hisProTenantApplyDto);
		}
		return dtos;
	}

	/**
	 * 获取脚本快速执行链接
	 * 
	 * @param scriptId
	 * @throws IOException
	 */
	public ScriptDetailInfoDto getScriptDetailInfo(int scriptId) throws IOException {
		ScriptDetailInfoDto scriptDetailInfo = new ScriptDetailInfoDto();
		ScriptEntity docInfoEntity = docScriptDaoImpl.selectById(scriptId);
		// 更新点击量
		docInfoEntity.setClickNum(docInfoEntity.getClickNum() + 1);
		docScriptDaoImpl.save(docInfoEntity);
		File file = new File(scriptRepositoryPath + docInfoEntity.getItemType() + "\\" + docInfoEntity.getName());
		byte[] body = null;
		InputStream is = new FileInputStream(file);
		body = new byte[is.available()];
		is.read(body);
		is.close();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Content-Disposition", "attchement;filename=" + docInfoEntity.getName());
		HttpStatus statusCode = HttpStatus.OK;
		ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
		scriptDetailInfo.setScriptEntity(docInfoEntity);
		scriptDetailInfo.setBytes(entity);
		return scriptDetailInfo;
	}

	/**
	 * 根据文件实体类获取文件字节码
	 * 
	 * @return
	 * @throws IOException
	 */
	private ResponseEntity<byte[]> getScriptInfoByte(ScriptEntity docInfoEntity) throws IOException {
		File file = new File(scriptRepositoryPath + docInfoEntity.getItemType() + "\\" + docInfoEntity.getName());
		byte[] body = null;
		InputStream is = new FileInputStream(file);
		body = new byte[is.available()];
		is.read(body);
		is.close();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Content-Disposition", "attchement;filename=" + docInfoEntity.getName());
		HttpStatus statusCode = HttpStatus.OK;
		ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
		return entity;
	}

	/**
	 * 获取正式文件流
	 * 
	 * @param scriptId
	 * @return
	 * @throws IOException
	 */
	public ResponseEntity<byte[]> officialRead(int scriptId) throws IOException {
		ScriptEntity scriptInfo = docScriptDaoImpl.selectById(scriptId);
		File file = new File(scriptRepositoryPath + scriptInfo.getItemType() + "\\" + scriptInfo.getName());
		byte[] body = null;
		InputStream is = new FileInputStream(file);
		body = new byte[is.available()];
		is.read(body);
		is.close();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Content-Disposition", "attchement;filename=" + scriptInfo.getName());
		HttpStatus statusCode = HttpStatus.OK;
		ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
		return entity;
	}
}
