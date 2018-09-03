package com.iv.tenant.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iv.common.enumeration.WorkflowType;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.common.util.spring.Constants;
import com.iv.common.util.spring.JWTUtil;
import com.iv.enter.dto.AccountDto;
import com.iv.enter.dto.UsersQueryDto;
import com.iv.message.api.constant.MsgType;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.tenant.api.constant.ErrorMsg;
import com.iv.tenant.api.dto.HisProTenantApplyReq;
import com.iv.tenant.api.dto.ProcessDataDto;
import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.api.dto.SubEnterpriseInfoDto;
import com.iv.tenant.api.dto.TaskEnterpriseApplyResp;
import com.iv.tenant.api.dto.TenantInfoDto;
import com.iv.tenant.api.dto.UserListDto;
import com.iv.tenant.binding.BinderConfiguration;
import com.iv.tenant.dao.EnterpriseDaoImpl;
import com.iv.tenant.dao.SubEnterpriseDaoImpl;
import com.iv.tenant.entity.EnterpriseEntity;
import com.iv.tenant.entity.SubEnterpriseEntity;
import com.iv.tenant.feign.client.IAuthenticationServiceClient;
import com.iv.tenant.feign.client.IPermissionServiceClient;
import com.iv.tenant.feign.client.ISubTenantPermissionServiceClient;
import com.iv.tenant.feign.client.IUserServiceClient;
import com.iv.tenant.util.Constant;

/**
 * 租户管理服务
 * 
 * @author macheng 2018年4月4日 alarm-aggregation-service-1.0.0-SNAPSHOT
 * 
 */
@Service
public class TenantService {

	@Autowired
	private EnterpriseDaoImpl enterpriseDao;
	@Autowired
	private SubEnterpriseDaoImpl subEnterpriseDao;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private IUserServiceClient userServiceClient;
	@Autowired
	private IPermissionServiceClient permissionServiceClient;
	@Autowired
	private ISubTenantPermissionServiceClient subTenantPermissionServiceClient;
	@Autowired
	private IAuthenticationServiceClient authenticationServiceClient;
	@Autowired
	private BinderConfiguration msgSender;

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantService.class);

	/**
	 * 获取企业主体名列表
	 * 
	 * @return
	 */
	public List<EnterpriseEntity> getEnterprisesAll() {
		return enterpriseDao.selectAll();
	}

	public List<SubEnterpriseInfoDto> getSubEnterpriseAll() {
		List<SubEnterpriseEntity> enterpriseList = subEnterpriseDao.selectAll();
		List<SubEnterpriseInfoDto> dtos = new ArrayList<SubEnterpriseInfoDto>();
		for (SubEnterpriseEntity enterpriseEntity : enterpriseList) {
			SubEnterpriseInfoDto dto = new SubEnterpriseInfoDto();
			BeanUtils.copyProperties(enterpriseEntity, dto, "enterprise");
			dtos.add(dto);
		}
		return dtos;

	}

	/**
	 * 获取租户信息
	 * 
	 * @param tenantId
	 * @return
	 */
	public List<EnterpriseEntity> getEnterprisesCondition(QueryEnterReq req) {
		return enterpriseDao.selectByCondition(req);
	}

	/*
	 * public EnterpriseWithTenant getEnterpriseWithTenant(String name) {
	 * EnterpriseWithTenant enterpriseWithTenant = new EnterpriseWithTenant();
	 * EnterpriseEntity enterpriseEntity = enterpriseDao.selectByName(name);
	 * enterpriseWithTenant.setEnterpriseEntity(enterpriseEntity);
	 * enterpriseWithTenant.setSubEnterpriseEntities(subEnterpriseDao.
	 * selectByEnterprise(enterpriseEntity)); return enterpriseWithTenant; }
	 */

	public List<SubEnterpriseEntity> getSubEnterpriseCondition(String condition) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(condition);
		if (!isNum.matches()) {
			// 优先根据项目组标识符查询
			SubEnterpriseEntity tenant = subEnterpriseDao.selectByIdentifier(condition);
			if (null != tenant) {
				return Arrays.asList(tenant);
			}
		}
		// 根据项目组名称查询
		return subEnterpriseDao.selectByName(condition);
	}

	/*
	 * public EnterpriseInfoResp getEnterpriseByTenantId(String tenantId) {
	 * EnterpriseInfoResp infoResp = new EnterpriseInfoResp(); EnterpriseEntity
	 * enterpriseEntity = null; enterpriseEntity =
	 * subEnterpriseDao.selectByTenantId(tenantId).getEnterprise();
	 * 
	 * EnterpriseInfoDto enterpriseInfoDto = new EnterpriseInfoDto();
	 * BeanUtils.copyProperties(enterpriseEntity, enterpriseInfoDto);
	 * infoResp.setEnterprise(enterpriseInfoDto); List<SubEnterpriseInfoDto> dtos =
	 * new ArrayList<SubEnterpriseInfoDto>(); for (SubEnterpriseEntity entity :
	 * subEnterpriseDao.selectByEnterprise(enterpriseEntity)) { SubEnterpriseInfoDto
	 * dto = new SubEnterpriseInfoDto(); BeanUtils.copyProperties(entity, dto,
	 * "enterprise"); dtos.add(dto); } infoResp.setSubEnterprises(dtos); return
	 * infoResp; }
	 */

	public SubEnterpriseInfoDto getTenantByTenantId(String tenantId) {

		SubEnterpriseEntity entity = subEnterpriseDao.selectByTenantId(tenantId);
		if (null == entity) {
			return null;
		}
		SubEnterpriseInfoDto dto = new SubEnterpriseInfoDto();
		BeanUtils.copyProperties(entity, dto, "enterprise");
		return dto;
	}

	public UserListDto getUsersByTenantId(String tenantId, int page, int items) {
		return subEnterpriseDao.selectUsersByTenantId(tenantId, page, items);
	}

	public List<SubEnterpriseEntity> getTenantsJoined(int userId) {
		return subEnterpriseDao.selectByUserId(userId);

	}

	/**
	 * 申请创建租户
	 * 
	 * @param dto
	 */
	/*
	 * public ResponseDto applyNewTenant(int userId, int enterpriseId, String name)
	 * {
	 * 
	 * if (null != enterpriseDao.selectByOrgCode(dto.getOrgCode()) || null !=
	 * enterpriseDao.selectByName(dto.getName())) { return
	 * ResponseDto.builder(ErrorMsg.TENANT_EXIST);// 租户已存在 } if
	 * (isTenantExist(dto.getOrgCode())) { return
	 * ResponseDto.builder(ErrorMsg.TENANT_APPLY_EXIST);// 租户申请已提交并在审批中 }
	 * 
	 * Map<String, Object> variables = new HashMap<String, Object>();
	 * variables.put("enterpriseId", enterpriseId); variables.put("tenantName",
	 * name); variables.put("userId", userId);
	 * identityService.setAuthenticatedUserId(String.valueOf(userId));
	 * ProcessInstance instance =
	 * runtimeService.startProcessInstanceByKey(WorkflowType.APPLY_REGIS_TENANT.name
	 * (), variables); runtimeService.addUserIdentityLink(instance.getId(),
	 * String.valueOf(userId), IdentityLinkType.STARTER); return
	 * ResponseDto.builder(ErrorMsg.OK); }
	 */

	/**
	 * 申请创建项目组
	 * 
	 * @param userId
	 * @param dto
	 * @return
	 */
	public ResponseDto applyNewTenant(int userId, TenantInfoDto dto) {
		/*
		 * if (null != enterpriseDao.selectByOrgCode(dto.getOrgCode()) || null !=
		 * enterpriseDao.selectByName(dto.getName())) { return
		 * ResponseDto.builder(ErrorMsg.TENANT_EXIST);// 租户已存在 } if
		 * (isTenantExist(dto.getOrgCode())) { return
		 * ResponseDto.builder(ErrorMsg.TENANT_APPLY_EXIST);// 租户申请已提交并在审批中 }
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("tenantDto", dto);
		variables.put("userId", userId);
		identityService.setAuthenticatedUserId(String.valueOf(userId));
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(WorkflowType.APPLY_REGIS_TENANT.name(),
				variables);
		runtimeService.addUserIdentityLink(instance.getId(), String.valueOf(userId), IdentityLinkType.STARTER);
		return ResponseDto.builder(ErrorMsg.OK);
	}

	/*
	 * public ProcessDataDto getUserTasksTenantRegis(int userId, int first, int max)
	 * { long totalNum =
	 * taskService.createTaskQuery().processDefinitionKey(WorkflowType.
	 * APPLY_REGIS_TENANT.name())
	 * .taskCandidateUser(String.valueOf(userId)).count(); List<Task> tasks =
	 * taskService.createTaskQuery().processDefinitionKey(WorkflowType.
	 * APPLY_REGIS_TENANT.name())
	 * .taskCandidateUser(String.valueOf(userId)).orderByTaskCreateTime().desc().
	 * listPage(first, max); List<TaskEnterpriseApplyResp> list = new
	 * ArrayList<TaskEnterpriseApplyResp>(); for (Task task : tasks) {
	 * 
	 * Map<String, Object> variables = taskService.getVariables(task.getId(),
	 * Arrays.asList("userId", "enterpriseId", "tenantName")); int applicant = (int)
	 * variables.get("userId"); int enterpriseId = (int)
	 * variables.get("enterpriseId"); String tenantName = (String)
	 * variables.get("tenantName"); EnterpriseEntity enterpriseEntity =
	 * enterpriseDao.selectById(enterpriseId); TenantInfoDto tenantInfoDto = new
	 * TenantInfoDto(); BeanUtils.copyProperties(enterpriseEntity, tenantInfoDto,
	 * "id"); tenantInfoDto.setSubEnterpriseName(tenantName);
	 * TaskEnterpriseApplyResp dto = new TaskEnterpriseApplyResp(task.getId(),
	 * WorkflowType.APPLY_REGIS_TENANT, applicant, tenantInfoDto,
	 * task.getCreateTime().toLocaleString()); list.add(dto);
	 * 
	 * }
	 * 
	 * ProcessDataDto dataDto = new ProcessDataDto(); dataDto.setData(list);
	 * dataDto.setTotalNum(totalNum); return dataDto; }
	 */

	/**
	 * 审批申请任务
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
	 * 获取待审批租户创建任务
	 * 
	 * @param assignee
	 * @return
	 */
	public ProcessDataDto getMyApprovingRegis(int userId, int first, int max) {
		long totalNum = taskService.createTaskQuery().processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name())
				.taskCandidateUser(String.valueOf(userId)).count();
		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name())
				.taskCandidateUser(String.valueOf(userId)).orderByTaskCreateTime().desc().listPage(first, max);
		List<TaskEnterpriseApplyResp> list = new ArrayList<TaskEnterpriseApplyResp>();
		for (Task task : tasks) {

			Map<String, Object> variables = taskService.getVariables(task.getId(),
					Arrays.asList("userId", "tenantDto"));
			int applicantId = (int) variables.get("userId");
			LocalAuthDto applicantDto = userServiceClient.selectLocalAuthById(applicantId);
			TenantInfoDto tenantInfoDto = (TenantInfoDto) variables.get("tenantDto");
			TaskEnterpriseApplyResp dto = new TaskEnterpriseApplyResp(task.getId(), WorkflowType.APPLY_REGIS_TENANT,
					applicantDto.getRealName(), applicantDto.getEmail(), applicantDto.getTel(), tenantInfoDto,
					task.getCreateTime().getTime());
			list.add(dto);

		}

		ProcessDataDto dataDto = new ProcessDataDto();
		dataDto.setData(list);
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	/**
	 * 获取待审批的加入项目组申请
	 * 
	 * @param userId
	 * @param first
	 * @param max
	 * @return
	 */
	public ProcessDataDto getMyApprovingJoin(int userId, int first, int max) {
		long totalNum = taskService.createTaskQuery().processDefinitionKey(WorkflowType.APPLY_JOIN_TENANT.name())
				.active().taskCandidateUser(String.valueOf(userId)).count();
		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(WorkflowType.APPLY_JOIN_TENANT.name())
				.active().taskCandidateUser(String.valueOf(userId)).orderByTaskCreateTime().desc().listPage(first, max);
		List<TaskEnterpriseApplyResp> list = new ArrayList<TaskEnterpriseApplyResp>();
		for (Task task : tasks) {
			int applicantId = (int) taskService.getVariable(task.getId(), "userId");
			LocalAuthDto applicantDto = userServiceClient.selectLocalAuthById(applicantId);
			TaskEnterpriseApplyResp tenantApplyResp = new TaskEnterpriseApplyResp();
			// 流程第一个task，时间和process相同
			tenantApplyResp.setCreateTime(task.getCreateTime().getTime());
			tenantApplyResp.setTaskId(task.getId());
			tenantApplyResp.setApplicant(applicantDto.getRealName());
			tenantApplyResp.setEmail(applicantDto.getEmail());
			tenantApplyResp.setTel(applicantDto.getTel());
			tenantApplyResp.setType(WorkflowType.APPLY_JOIN_TENANT);
			TenantInfoDto tenantInfo = new TenantInfoDto();
			String tenantId = (String) taskService.getVariable(task.getId(), "tenantId");
			SubEnterpriseEntity subEnterpriseEntity = subEnterpriseDao.selectByTenantId(tenantId);
			EnterpriseEntity enterpriseEntity = subEnterpriseEntity.getEnterprise();
			tenantInfo.setName(enterpriseEntity.getName());
			tenantInfo.setOrgCode(enterpriseEntity.getOrgCode());
			tenantApplyResp.setTenantInfo(tenantInfo);
			list.add(tenantApplyResp);
		}

		ProcessDataDto dataDto = new ProcessDataDto();
		dataDto.setData(list);
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	public ProcessDataDto getMyApprovedRegis(int userId, int first, int max) {
		long totalNum = historyService.createHistoricTaskInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name()).taskAssignee(String.valueOf(userId))
				.count();

		if (0 == totalNum) {
			return new ProcessDataDto(Collections.emptyList(), 0);// 不存在当前用户审批的任务列表
		}
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name()).taskAssignee(String.valueOf(userId))
				.orderByHistoricTaskInstanceEndTime().desc().listPage(first, max);
		Set<String> ids = new HashSet<String>();
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			ids.add(historicTaskInstance.getProcessInstanceId());
		}
		List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
				.processInstanceIds(ids).orderByProcessInstanceStartTime().desc().list();

		ProcessDataDto dataDto = new ProcessDataDto();
		dataDto.setData(historyRegisConvert(historicProcessInstances));
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	public ProcessDataDto getMyApprovedJoin(int userId, int first, int max) {

		long totalNum = historyService.createHistoricTaskInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_JOIN_TENANT.name()).taskAssignee(String.valueOf(userId))
				.count();
		if (0 == totalNum) {
			return new ProcessDataDto(Collections.emptyList(), 0);// 不存在当前用户审批的任务列表
		}
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_JOIN_TENANT.name()).taskAssignee(String.valueOf(userId))
				.orderByHistoricTaskInstanceEndTime().desc().listPage(first, max);
		Set<String> ids = new HashSet<String>();
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			ids.add(historicTaskInstance.getProcessInstanceId());
		}
		List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
				.processInstanceIds(ids).orderByProcessInstanceStartTime().desc().list();

		ProcessDataDto dataDto = new ProcessDataDto();
		dataDto.setData(historyJoinConvert(historicProcessInstances));
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	/**
	 * 获取项目组创建申请相关历史工作流
	 * 
	 * @param userId
	 * @param first
	 * @param max
	 */
	public ProcessDataDto getMyApplyRegis(int userId, int first, int max) {

		long totalNum = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name()).startedBy(String.valueOf(userId)).count();
		List<HistoricProcessInstance> histotricProcess = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name()).startedBy(String.valueOf(userId))
				/* .finished() */.orderByProcessInstanceEndTime().desc().listPage(first, max);

		ProcessDataDto dataDto = new ProcessDataDto();
		dataDto.setData(historyRegisConvert(histotricProcess));
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	/**
	 * 获取加入租户申请的历史工作流
	 * 
	 * @param userId
	 * @param first
	 * @param max
	 */
	public ProcessDataDto getMyApplyJoin(int userId, int first, int max) {

		long totalNum = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_JOIN_TENANT.name()).startedBy(String.valueOf(userId)).count();

		List<HistoricProcessInstance> histotricProcess = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_JOIN_TENANT.name()).startedBy(String.valueOf(userId))
				/* .finished() */.orderByProcessInstanceEndTime().desc().listPage(first, max);

		ProcessDataDto dataDto = new ProcessDataDto();
		dataDto.setData(historyJoinConvert(histotricProcess));
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	private List<HisProTenantApplyReq> historyRegisConvert(List<HistoricProcessInstance> histotricProcess) {
		List<HisProTenantApplyReq> dtos = new ArrayList<HisProTenantApplyReq>();
		for (HistoricProcessInstance historicProcessInstance : histotricProcess) {
			HisProTenantApplyReq hisProTenantApplyDto = new HisProTenantApplyReq();
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
				} else if ("tenantDto".equals(historicVariableInstance.getVariableName())) {
					hisProTenantApplyDto.setTenantInfo((TenantInfoDto) historicVariableInstance.getValue());
				} else if ("approved".equals(historicVariableInstance.getVariableName())) {
					hisProTenantApplyDto.setApproved((boolean) historicVariableInstance.getValue());
				} else if ("remark".equals(historicVariableInstance.getVariableName())) {
					hisProTenantApplyDto.setRemark((String) historicVariableInstance.getValue());
				}
			}

			dtos.add(hisProTenantApplyDto);
		}
		return dtos;

	}

	private List<HisProTenantApplyReq> historyJoinConvert(List<HistoricProcessInstance> histotricProcess) {
		List<HisProTenantApplyReq> dtos = new ArrayList<HisProTenantApplyReq>();
		for (HistoricProcessInstance historicProcessInstance : histotricProcess) {
			HisProTenantApplyReq hisProTenantApplyDto = new HisProTenantApplyReq();
			hisProTenantApplyDto.setId(historicProcessInstance.getId());
			hisProTenantApplyDto.setEndTime(historicProcessInstance.getEndTime() == null ? null
					: historicProcessInstance.getEndTime().toLocaleString());
			hisProTenantApplyDto.setStartTime(historicProcessInstance.getStartTime().toLocaleString());
			hisProTenantApplyDto.setDuration(String.valueOf(historicProcessInstance.getDurationInMillis() == null ? null
					: historicProcessInstance.getDurationInMillis() / 1000 / 60));
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
				} else if ("approved".equals(historicVariableInstance.getVariableName())) {
					hisProTenantApplyDto.setApproved((boolean) historicVariableInstance.getValue());
				} else if ("remark".equals(historicVariableInstance.getVariableName())) {
					hisProTenantApplyDto.setRemark((String) historicVariableInstance.getValue());
				} else if ("tenantId".equals(historicVariableInstance.getVariableName())) {
					SubEnterpriseEntity subEnterpriseEntity = subEnterpriseDao
							.selectByTenantId((String) historicVariableInstance.getValue());
					EnterpriseEntity enterpriseEntity = subEnterpriseEntity.getEnterprise();
					if (null == enterpriseEntity) {
						continue;
					}
					TenantInfoDto tenantInfo = new TenantInfoDto();
					tenantInfo.setName(enterpriseEntity.getName());
					tenantInfo.setIdentifier(enterpriseEntity.getIdentifier());
					tenantInfo.setOrgCode(enterpriseEntity.getOrgCode());
					hisProTenantApplyDto.setTenantInfo(tenantInfo);
				}
			}

			dtos.add(hisProTenantApplyDto);
		}
		return dtos;
	}

	/*
	 * public ProcessDataDto getHistoryTenantRegis(int userId, int first, int max) {
	 * List<HisProTenantApplyReq> dtos = new ArrayList<HisProTenantApplyReq>(); long
	 * totalNum = historyService.createHistoricProcessInstanceQuery()
	 * .processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name()).involvedUser(
	 * String.valueOf(userId)) .count(); List<HistoricProcessInstance>
	 * histotricProcess = historyService.createHistoricProcessInstanceQuery()
	 * .processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name()).involvedUser(
	 * String.valueOf(userId)) .finished()
	 * .orderByProcessInstanceEndTime().desc().listPage(first, max); for
	 * (HistoricProcessInstance historicProcessInstance : histotricProcess) {
	 * HisProTenantApplyReq hisProTenantApplyDto = new HisProTenantApplyReq();
	 * hisProTenantApplyDto.setId(historicProcessInstance.getId());
	 * hisProTenantApplyDto.setEndTime(historicProcessInstance.getEndTime() == null
	 * ? null : historicProcessInstance.getEndTime().toLocaleString());
	 * hisProTenantApplyDto.setStartTime(historicProcessInstance.getStartTime().
	 * toLocaleString());
	 * hisProTenantApplyDto.setDuration(historicProcessInstance.getDurationInMillis(
	 * ) == null ? null :
	 * String.valueOf(historicProcessInstance.getDurationInMillis() / 1000 / 60));
	 * hisProTenantApplyDto.setType(historicProcessInstance.getProcessDefinitionKey(
	 * )); // historicProcessInstance.getStartUserId();
	 * 
	 * List<HistoricVariableInstance> variables =
	 * historyService.createHistoricVariableInstanceQuery()
	 * .executionId(historicProcessInstance.getId()).list(); String tenantName =
	 * null; for (HistoricVariableInstance historicVariableInstance : variables) {
	 * if ("userId".equals(historicVariableInstance.getVariableName())) {
	 * hisProTenantApplyDto.setStartUser((int) historicVariableInstance.getValue());
	 * } else if ("tenantName".equals(historicVariableInstance.getVariableName())) {
	 * tenantName = (String) historicVariableInstance.getValue(); } else if
	 * ("enterpriseId".equals(historicVariableInstance.getVariableName())) { int
	 * enterpriseId = (int) historicVariableInstance.getValue(); EnterpriseEntity
	 * enterpriseEntity = enterpriseDao.selectById(enterpriseId); TenantInfoDto
	 * tenantInfoDto = new TenantInfoDto();
	 * BeanUtils.copyProperties(enterpriseEntity, tenantInfoDto, "id");
	 * hisProTenantApplyDto.setTenantInfo(tenantInfoDto); } else if
	 * ("approved".equals(historicVariableInstance.getVariableName())) {
	 * hisProTenantApplyDto.setApproved((boolean)
	 * historicVariableInstance.getValue()); } else if
	 * ("remark".equals(historicVariableInstance.getVariableName())) {
	 * hisProTenantApplyDto.setRemark((String) historicVariableInstance.getValue());
	 * } } hisProTenantApplyDto.getTenantInfo().setSubEnterpriseName(tenantName);
	 * dtos.add(hisProTenantApplyDto); }
	 * 
	 * ProcessDataDto dataDto = new ProcessDataDto(); dataDto.setData(dtos);
	 * dataDto.setTotalNum(totalNum); return dataDto; }
	 */

	/**
	 * 获取企业注册申请的审批人，开启流程实例后由系统调用
	 * 
	 * @param execution
	 * @return
	 */
	public List<String> findCandidateUsersRegistTenant(DelegateExecution execution) {
		// 运营运维管理员进行审批
		List<String> approvers = new ArrayList<String>(1);
		Set<LocalAuthDto> authDtos = permissionServiceClient.getApprovalPersons(Constant.TENANT_REGIS_CODE);
		if (CollectionUtils.isEmpty(authDtos)) {
			return approvers;
		}
		authDtos.forEach(n -> approvers.add(String.valueOf(n.getId())));
		// 制造用户消息
		TenantInfoDto tenantInfoDto = (TenantInfoDto) execution.getVariable("tenantDto");
		int userId = (int) execution.getVariable("userId");
		LocalAuthDto applicant = userServiceClient.selectLocalAuthById(userId);
		// 调用通知管理服务
		List<Integer> approverIds = new ArrayList<Integer>(1);
		approvers.forEach(n -> approverIds.add(Integer.parseInt(n)));
		msgSender.pendingMsgSend(approverIds, applicant.getRealName() + "(" + applicant.getUserName(),
				tenantInfoDto.getName(), tenantInfoDto.getSubEnterpriseName(), MsgType.TNT_NEW_PENDING);
		return approvers;
	}

	/**
	 * 获取项目组申请的审批人，开启流程实例后由系统调用
	 * 
	 * @param execution
	 * @return
	 */
	/*
	 * public List<String> findCandidateUsersRegistTenant(DelegateExecution
	 * execution) { int enterpriseId = (int) execution.getVariable("enterpriseId");
	 * EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(enterpriseId);
	 * int applicant = (int) execution.getVariable("userId"); String tenantName =
	 * (String) execution.getVariable("tenantName"); LocalAuthDto localAuthDto =
	 * permissionServiceClient.getEnterpriseAdmin(enterpriseId); // 调用消息服务
	 * messageServiceClient.produceApproveMsg(String.valueOf(applicant),
	 * localAuthDto.getId(), tenantName, enterpriseEntity.getName(),
	 * WorkflowType.APPLY_REGIS_TENANT); return
	 * Arrays.asList(String.valueOf(localAuthDto.getId())); }
	 */

	/**
	 * 审批通过后创建项目组,activit调用
	 * 
	 * @param execution
	 */
	/*
	 * public void createTenant(DelegateExecution execution) {
	 * 
	 * Boolean isCreate = (Boolean) execution.getVariable("approved"); String
	 * tenantName = (String) execution.getVariable("tenantName"); int enterpriseId =
	 * (int) execution.getVariable("enterpriseId"); int userId = (int)
	 * execution.getVariable("userId"); String remark = (String)
	 * execution.getVariable("remark"); EnterpriseEntity enterpriseEntity =
	 * enterpriseDao.selectById(enterpriseId); if (isCreate) { LocalAuthDto user =
	 * userServiceClient.selectLocalAuthById(userId); // 初始化项目组
	 * initSubEnterprise(enterpriseEntity, user, enterpriseEntity.getIdentifier());
	 * 
	 * } // 制造消息通知 messageServiceClient.produceApplyMsg(userId, isCreate,
	 * tenantName, enterpriseEntity.getName(), remark,
	 * WorkflowType.APPLY_REGIS_TENANT);
	 * 
	 * }
	 */

	/**
	 * 审批通过后创建项目组并注册企业,activit调用
	 * 
	 * @param execution
	 */
	public void createTenant(DelegateExecution execution) {

		Boolean isCreate = (Boolean) execution.getVariable("approved");
		TenantInfoDto dto = (TenantInfoDto) execution.getVariable("tenantDto");
		int userId = (int) execution.getVariable("userId");
		String remark = (String) execution.getVariable("remark");

		if (isCreate) {
			// 审批通过，创建租户
			EnterpriseEntity enterpriseEntity = enterpriseDao.selectByOrgCode(dto.getOrgCode());
			LocalAuthDto user = userServiceClient.selectLocalAuthById(userId);
			if (null == enterpriseEntity) {
				// 企业第一次创建项目组，注册企业信息
				enterpriseEntity = initEnterprise(dto, user);
			}
			// 初始化项目组空间
			initSubEnterprise(enterpriseEntity, user, dto.getSubEnterpriseName());
		}
		// 制造消息通知
		msgSender.resultMsgSend(Arrays.asList(userId), isCreate, dto.getName(), dto.getSubEnterpriseName(), remark,
				MsgType.TNT_NEW_RESULT);

	}

	/**
	 * 注册企业
	 * 
	 * @param dto
	 * @param user
	 * @return
	 */
	private EnterpriseEntity initEnterprise(TenantInfoDto dto, LocalAuthDto user) {
		EnterpriseEntity entity = new EnterpriseEntity();
		entity.setName(dto.getName());
		entity.setOrgCode(dto.getOrgCode());
		entity.setAddress(dto.getAddress());
		entity.setBusinessInvolves(dto.getBusinessInvolves());
		entity.setIndustry(dto.getIndustry());
		entity.setStaffSize(dto.getStaffSize());
		String identifier = Calendar.getInstance().get(Calendar.YEAR) + 6000
				+ (String.format("%04d", enterpriseDao.countAll() + 1));
		entity.setIdentifier(identifier);
		entity = enterpriseDao.save(entity);

		// 初始化企业创建者
		// permissionServiceClient.createEnterpriseAdmin(entity.getId(), user.getId());
		return entity;
	}

	/**
	 * 初始化项目组空间
	 * 
	 * @param entity
	 * @param user
	 * @param subEnterpriseName
	 */
	private SubEnterpriseEntity initSubEnterprise(EnterpriseEntity enterpriseEntity, LocalAuthDto user,
			String subEnterpriseName) {
		// 创建项目组
		SubEnterpriseEntity subEnterpriseEntity = new SubEnterpriseEntity();
		subEnterpriseEntity.setName(subEnterpriseName);
		subEnterpriseEntity.setEnterprise(enterpriseEntity);
		SubEnterpriseEntity previousEntity = subEnterpriseDao.countWithEnterprise(enterpriseEntity);
		String identifier = null;
		if (null != previousEntity) {
			// 已存在项目组
			identifier = String.valueOf(Integer.parseInt(previousEntity.getSubIdentifier()) + 1);
		} else {
			// 第一个项目组
			identifier = enterpriseEntity.getIdentifier() + 1;
		}
		subEnterpriseEntity.setSubIdentifier(identifier);
		subEnterpriseEntity.setTenantId("t" + identifier);

		// 添加用户至项目组
		Set<Integer> userIds = new HashSet<Integer>(1);
		userIds.add(user.getId());
		subEnterpriseEntity.setUserIds(userIds);
		subEnterpriseDao.save(subEnterpriseEntity);

		// 初始化项目组管理员
		subTenantPermissionServiceClient.createSubEnterpriseAdmin(subEnterpriseEntity.getTenantId(), user.getId());

		// 更新用户信息
		AccountDto userModify = new AccountDto();
		userModify.setUserId(user.getId());
		if (ConstantContainer.TOURIST.equals(user.getCurTenantId())) {
			userModify.setCurTenantId(subEnterpriseEntity.getTenantId());
		}
		userServiceClient.saveOrUpdateUserAuth(userModify);

		return subEnterpriseEntity;

	}

	/**
	 * 申请加入项目组
	 * 
	 */
	public ResponseDto applyJoinTenant(int userId, String tenantId) {

		// LocalAuthDto user = userServiceClient.selectLocalAuthById(userId);
		if (subEnterpriseDao.selectByTenantId(tenantId).getUserIds().contains(userId)) {
			// 已加入该租户
			return ResponseDto.builder(ErrorMsg.ALREADY_IN_TENANT);
		}
		if (isUserJoinExist(userId, tenantId)) {
			// 未关闭的申请流已存在
			return ResponseDto.builder(ErrorMsg.TENANT_APPLY_EXIST);
		}
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userId", userId);
		variables.put("tenantId", tenantId);
		identityService.setAuthenticatedUserId(String.valueOf(userId));
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(WorkflowType.APPLY_JOIN_TENANT.name(),
				variables);
		runtimeService.addUserIdentityLink(instance.getId(), String.valueOf(userId), IdentityLinkType.STARTER);
		return ResponseDto.builder(ErrorMsg.OK);
	}

	/**
	 * 获取加入项目组的审批人
	 * 
	 * @param execution
	 * @return
	 */
	public List<String> findCandidateUsersJoin(DelegateExecution execution) {
		String tenantId = (String) execution.getVariable("tenantId");
		SubEnterpriseEntity subEnterpriseEntity = subEnterpriseDao.selectByTenantId(tenantId);
		int userId = (int) execution.getVariable("userId");
		List<String> approvers = new ArrayList<String>(1);
		// 获取租户创建人
		/*
		 * LocalAuthDto authDto =
		 * permissionServiceClient.getSubEnterpriseAdmin(subEnterpriseEntity.getId());
		 * approvers.add(String.valueOf(authDto.getId()));
		 */
		// 获取租户管理员
		Set<LocalAuthDto> authDtos = subTenantPermissionServiceClient.getSubEnterpriseApprovalPersons(tenantId,
				Constant.JOIN_TASKT_CODE);
		if (CollectionUtils.isEmpty(authDtos)) {
			return approvers;
		}
		authDtos.forEach(n -> approvers.add(String.valueOf(n.getId())));
		// 制造用户消息
		LocalAuthDto applicant = userServiceClient.selectLocalAuthById(userId);
		List<Integer> approverIds = new ArrayList<>();
		approvers.forEach(n -> approverIds.add(Integer.parseInt(n)));
		msgSender.pendingMsgSend(approverIds, applicant.getRealName() + "(" + applicant.getUserName() + ")",
				subEnterpriseEntity.getEnterprise().getName(), subEnterpriseEntity.getName(), MsgType.TNT_JOIN_PENDING);

		return approvers;
	}

	/**
	 * 添加用户至项目组
	 * 
	 * @param userId
	 */
	public void addUserToTenant(DelegateExecution execution) {
		boolean isApproved = (boolean) execution.getVariable("approved");
		int userId = (int) execution.getVariable("userId");
		String remark = (String) execution.getVariable("remark");
		String tenantId = (String) execution.getVariable("tenantId");
		SubEnterpriseEntity subEnterpriseEntity = subEnterpriseDao.selectByTenantId(tenantId);
		if (isApproved) {
			// 用户添加至租户
			if (subEnterpriseEntity.getUserIds().contains(userId)) {
				return; // 已加入该租户
			}
			subEnterpriseEntity.getUserIds().add(userId);
			subEnterpriseDao.save(subEnterpriseEntity);
			// 用户关联租户
			LocalAuthDto user = userServiceClient.selectLocalAuthById(userId);
			if (ConstantContainer.TOURIST.equals(user.getCurTenantId())) {
				user.setCurTenantId(tenantId);
			}
		}
		// 制造消息通知
		msgSender.resultMsgSend(Arrays.asList(userId), isApproved, subEnterpriseEntity.getEnterprise().getName(),
				subEnterpriseEntity.getName(), remark, MsgType.TNT_JOIN_RESULT);
	}

	/**
	 * 手动添加用户
	 * 
	 * @param userId
	 */
	public ResponseDto manuallyAddUser(String tenantId, List<String> userIds) {

		List<Integer> idList = new ArrayList<Integer>();
		userIds.forEach(id -> idList.add(Integer.parseInt(id)));
		SubEnterpriseEntity subEnterpriseEntity = subEnterpriseDao.selectByTenantId(tenantId);
		subEnterpriseEntity.getUserIds().addAll(idList);
		subEnterpriseDao.save(subEnterpriseEntity);
		UsersQueryDto queryDto = new UsersQueryDto();
		queryDto.setUserIds(idList);
		List<LocalAuthDto> authDtos = userServiceClient.selectUserInfos(queryDto);
		if (null == authDtos) {
			return ResponseDto.builder(ErrorMsg.TENANT_USERADD_FAILED);
		}
		for (LocalAuthDto user : authDtos) {

			AccountDto userModify = new AccountDto();
			userModify.setUserId(user.getId());
			if (ConstantContainer.TOURIST.equals(user.getCurTenantId())) {
				userModify.setCurTenantId(tenantId);
			}
			// 保存用户
			userServiceClient.saveOrUpdateUserAuth(userModify);
		}
		return ResponseDto.builder(ErrorMsg.OK);

	}

	public ResponseDto switchTenant(String tenantId, String refreshToken, String token) {
		if (null == subEnterpriseDao.selectByTenantId(tenantId)) {
			return ResponseDto.builder(ErrorMsg.TENANT_NOT_EXIST);
		}
		int userId = JWTUtil.getJWtJson(token).getInt("userId");
		// 更新用户当前租户标识
		AccountDto userModify = new AccountDto();
		userModify.setUserId(userId);
		userModify.setCurTenantId(tenantId);
		userServiceClient.saveOrUpdateUserAuth(userModify);
		// 刷新token认证信息
		Object accessToken = authenticationServiceClient.refreshToken(Constants.OAUTH2_CLIENT_BASIC, "refresh_token",
				refreshToken);
		ResponseDto dto = ResponseDto.builder(ErrorMsg.OK);
		dto.setData(accessToken);
		return dto;
	}

	public SubEnterpriseInfoDto getSubEnterpriseByTenantIdBack(String tenantId) {
		SubEnterpriseEntity enterpriseEntity = subEnterpriseDao.selectByTenantId(tenantId);
		SubEnterpriseInfoDto enterpriseInfoDto = new SubEnterpriseInfoDto();
		BeanUtils.copyProperties(enterpriseEntity, enterpriseInfoDto, "enterprise");
		return enterpriseInfoDto;
	}
	
	/**
	 * 验证该租户申请（active状态）是否存在
	 * 
	 * @param orgCode
	 * @return true：租户已存在
	 */
	private boolean isTenantExist(String orgCode) {
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name()).active().list();
		for (ProcessInstance processInstance : processInstances) {
			Map<String, Object> map = runtimeService.getVariables(processInstance.getId());
			TenantInfoDto tenantInfo = (TenantInfoDto) map.get("tenantDto");
			if (orgCode.equals(tenantInfo.getOrgCode())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证该用户加入租户的申请（active状态）是否存在
	 * 
	 * @param userId
	 * @param tenantId
	 * @return
	 */
	private boolean isUserJoinExist(int userId, String tenantId) {

		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_JOIN_TENANT.name()).startedBy(String.valueOf(userId))
				.unfinished().list();
		for (HistoricProcessInstance historicProcessInstance : list) {
			List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
					.executionId(historicProcessInstance.getId()).list();
			for (HistoricVariableInstance historicVariableInstance : variables) {
				if ("tenantId".equals(historicVariableInstance.getVariableName())) {
					String tenIden = (String) historicVariableInstance.getValue();
					if (tenantId.equals(tenIden)) {
						// 已存在该申请
						// if (historicProcessInstance.getEndTime() != null) {
						return true;
						// }
					}
				}
			}
		}
		return false;
	}

}
