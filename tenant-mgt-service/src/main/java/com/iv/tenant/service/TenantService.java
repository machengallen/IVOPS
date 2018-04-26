package com.iv.tenant.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricProcessInstance;
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
import org.springframework.web.context.request.RequestContextHolder;

import com.iv.common.enumeration.WorkflowType;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.ConstantContainer;
import com.iv.common.util.spring.JWTUtil;
import com.iv.enter.dto.AccountDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.tenant.api.constant.ErrorMsg;
import com.iv.tenant.api.dto.EnterpriseInfoDto;
import com.iv.tenant.api.dto.EnterpriseInfoResp;
import com.iv.tenant.api.dto.HisProTenantApplyReq;
import com.iv.tenant.api.dto.ProcessDataDto;
import com.iv.tenant.api.dto.QueryEnterReq;
import com.iv.tenant.api.dto.SubEnterpriseInfoDto;
import com.iv.tenant.api.dto.TaskTenantApplyResp;
import com.iv.tenant.api.dto.TenantInfoDto;
import com.iv.tenant.dao.EnterpriseDaoImpl;
import com.iv.tenant.dao.SubEnterpriseDaoImpl;
import com.iv.tenant.entity.EnterpriseEntity;
import com.iv.tenant.entity.SubEnterpriseEntity;
import com.iv.tenant.feign.client.IMessageServiceClient;
import com.iv.tenant.feign.client.IUserServiceClient;

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
	private IMessageServiceClient messageServiceClient;
	@Autowired
	private IUserServiceClient userServiceClient;

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantService.class);

	/**
	 * 获取企业主体名列表
	 * 
	 * @return
	 */
	public List<EnterpriseEntity> getEnterprisesList() {
		return enterpriseDao.selectAll();
	}

	/**
	 * 获取租户信息
	 * 
	 * @param tenantId
	 * @return
	 */
	public List<EnterpriseEntity> getTenant(QueryEnterReq req) {
		return enterpriseDao.selectByCondition(req);
	}

	/*
	 * public List<UserInfoDto> getUserId(QueryUserId query) { String tenantId =
	 * ((LocalAuthDetails)
	 * SecurityContextHolder.getContext().getAuthentication().getPrincipal())
	 * .getCurTenantId(); List<UserInfoDto> users = new ArrayList<UserInfoDto>(); if
	 * (tenantId.startsWith("t")) { // 场景：添加用户至租户 List<LocalAuth> auths =
	 * USER_DAO.selectByCondition(query.getUserName(), query.getRealName(),
	 * query.getTel()); // 租户下已存在用户 List<HireUser> hireUsers =
	 * HIRE_USER_DAO.selectAll(tenantId); List<Integer> hireUserIds = new
	 * ArrayList<Integer>(); hireUsers.forEach(n -> hireUserIds.add(n.getUserId()));
	 * // 封装用户信息 for (LocalAuth auth : auths) { UserInfoDto userDto = new
	 * UserInfoDto(); userDto.setId(auth.getId());
	 * userDto.setBoundFlag(auth.getBoundFlag());
	 * userDto.setRealName(auth.getRealName()); userDto.setTel(auth.getTel());
	 * userDto.setUserName(auth.getUserName());
	 * userDto.setWechatInfo(auth.getWechatInfo()); if
	 * (hireUserIds.contains(auth.getId())) { userDto.setExist(true); }
	 * users.add(userDto); } return users; } else if (tenantId.startsWith("s")) { //
	 * 场景：添加用户至项目组 SubEnterpriseEntity subEnterpriseEntity =
	 * subEnterpriseDao.selectBySubTenantId(tenantId); List<LocalAuth> auths =
	 * USER_DAO.selectByCondition(query.getUserName(), query.getRealName(),
	 * query.getTel()); // 项目组下已存在的用户 List<HireUser> hireUsers =
	 * HIRE_USER_DAO.selectAll(tenantId); List<Integer> hireUserIds = new
	 * ArrayList<Integer>(); hireUsers.forEach(n -> hireUserIds.add(n.getUserId()));
	 * // 封装用户信息 for (LocalAuth localAuth : auths) { if
	 * (subEnterpriseEntity.getEnterprise().getLocalAuths().contains(localAuth)) {
	 * UserInfoDto userDto = new UserInfoDto(); userDto.setId(localAuth.getId());
	 * userDto.setBoundFlag(localAuth.getBoundFlag());
	 * userDto.setRealName(localAuth.getRealName());
	 * userDto.setTel(localAuth.getTel());
	 * userDto.setUserName(localAuth.getUserName());
	 * userDto.setWechatInfo(localAuth.getWechatInfo()); if
	 * (hireUserIds.contains(localAuth.getId())) { userDto.setExist(true); }
	 * users.add(userDto); } } return users; } else { return null; } }
	 */

	public EnterpriseInfoResp getSubTenant(String tenantId) {
		EnterpriseInfoResp infoResp = new EnterpriseInfoResp();
		EnterpriseEntity enterpriseEntity = null;
		enterpriseEntity = subEnterpriseDao.selectBySubTenantId(tenantId).getEnterprise();

		EnterpriseInfoDto enterpriseInfoDto = new EnterpriseInfoDto();
		BeanUtils.copyProperties(enterpriseEntity, enterpriseInfoDto);
		infoResp.setEnterprise(enterpriseInfoDto);
		List<SubEnterpriseInfoDto> dtos = new ArrayList<SubEnterpriseInfoDto>();
		for (SubEnterpriseEntity entity : subEnterpriseDao.selectByEnterprise(enterpriseEntity)) {
			SubEnterpriseInfoDto dto = new SubEnterpriseInfoDto();
			BeanUtils.copyProperties(entity, dto, "enterprise");
			dtos.add(dto);
		}
		infoResp.setSubEnterprises(dtos);
		return infoResp;
	}
	
	public SubEnterpriseInfoDto getSubTenantBySubId(String subTenant) {
		
		SubEnterpriseEntity entity = subEnterpriseDao.selectBySubTenantId(subTenant);
		SubEnterpriseInfoDto dto = new SubEnterpriseInfoDto();
		BeanUtils.copyProperties(entity, dto, "enterprise");
		return dto;
	}

	/**
	 * 申请创建租户
	 * 
	 * @param dto
	 */
	public ResponseDto applyNewTenant(int userId, TenantInfoDto dto) {
		if (null != enterpriseDao.selectByOrgCode(dto.getOrgCode())
				|| null != enterpriseDao.selectByName(dto.getName())) {
			return ResponseDto.builder(ErrorMsg.TENANT_EXIST);// 租户已存在
		}
		if (isTenantExist(dto.getOrgCode())) {
			return ResponseDto.builder(ErrorMsg.TENANT_APPLY_EXIST);// 租户申请已提交并在审批中
		}
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("tenantDto", dto);
		variables.put("userId", userId);
		identityService.setAuthenticatedUserId(String.valueOf(userId));
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(WorkflowType.APPLY_REGIS_TENANT.name(),
				variables);
		runtimeService.addUserIdentityLink(instance.getId(), String.valueOf(userId), IdentityLinkType.STARTER);
		return ResponseDto.builder(ErrorMsg.OK);
	}

	/**
	 * 审批申请任务
	 * 
	 * @param taskId
	 */
	public void taskApprove(String taskId, Boolean approved, String remark) {
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("approved", approved);
		taskVariables.put("remark", remark);
		taskService.complete(taskId, taskVariables);
	}

	/**
	 * 获取待审批租户创建任务
	 * 
	 * @param assignee
	 * @return
	 */
	public ProcessDataDto getUserTasksRegis(int userId, int first, int max) {
		long totalNum = taskService.createTaskQuery().processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name())
				.taskCandidateUser(String.valueOf(userId)).count();
		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name())
				.taskCandidateUser(String.valueOf(userId)).orderByTaskCreateTime().desc().listPage(first, max);
		List<TaskTenantApplyResp> list = new ArrayList<TaskTenantApplyResp>();
		for (Task task : tasks) {

			Map<String, Object> variables = taskService.getVariables(task.getId(),
					Arrays.asList("userId", "tenantDto"));
			int applicant = (int) variables.get("userId");
			TenantInfoDto tenantInfoDto = (TenantInfoDto) variables.get("tenantDto");
			TaskTenantApplyResp dto = new TaskTenantApplyResp(task.getId(), WorkflowType.APPLY_REGIS_TENANT, applicant,
					tenantInfoDto, task.getCreateTime().toLocaleString());
			list.add(dto);

		}

		ProcessDataDto dataDto = new ProcessDataDto();
		dataDto.setData(list);
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	/**
	 * 获取租户创建申请相关历史工作流
	 * 
	 * @param userId
	 * @param first
	 * @param max
	 */
	public ProcessDataDto getNewTenantHisPro(int userId, int first, int max) {
		List<HisProTenantApplyReq> dtos = new ArrayList<HisProTenantApplyReq>();
		long totalNum = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name()).involvedUser(String.valueOf(userId))
				.count();
		List<HistoricProcessInstance> histotricProcess = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_REGIS_TENANT.name()).involvedUser(String.valueOf(userId))
				/* .finished() */.orderByProcessInstanceEndTime().desc().listPage(first, max);
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
					hisProTenantApplyDto.setStartUser((int) historicVariableInstance.getValue());
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

		ProcessDataDto dataDto = new ProcessDataDto();
		dataDto.setData(dtos);
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	/**
	 * 获取租户申请的审批人，开启流程实例后由系统调用
	 * 
	 * @param execution
	 * @return
	 */
	public List<String> findCandidateUsersRegist(DelegateExecution execution) {
		// 当前版本只支持超级管理员进行审批
		LocalAuthDto admin = userServiceClient.selectLocalauthInfoByName("admin");

		// 制造用户消息
		TenantInfoDto tenantInfoDto = (TenantInfoDto) execution.getVariable("tenantDto");
		int userId = (int) execution.getVariable("userId");
		LocalAuthDto applicant = userServiceClient.selectLocalAuthById(userId);
		// 调用通知管理服务
		messageServiceClient.produceApproveMsg(applicant.getRealName() + "(" + applicant.getUserName() + ")",
				admin.getId(), tenantInfoDto.getName(), WorkflowType.APPLY_REGIS_TENANT);
		return Arrays.asList(String.valueOf(admin.getId()));
	}

	/**
	 * 审批通过后创建项目组并注册企业
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
			if (null != enterpriseDao.selectByOrgCode(dto.getOrgCode())) {
				return; // 租户已存在
			}
			LocalAuthDto user = userServiceClient.selectLocalAuthById(userId);
			// 注册企业信息
			EnterpriseEntity enterpriseEntity = registEnterprise(dto, user);
			// 初始化项目组空间
			SubEnterpriseEntity subEnterpriseEntity = initSubEnterprise(enterpriseEntity, user, enterpriseEntity.getIdentifier());
			// 初始化租户管理
			initTenMgt(enterpriseEntity.getId(), subEnterpriseEntity.getId(), userId);
		}
		// 制造消息通知
		messageServiceClient.produceApplyMsg(userId, isCreate, dto.getName(), remark, WorkflowType.APPLY_REGIS_TENANT);

	}

	/**
	 * 注册企业
	 * @param dto
	 * @param user
	 * @return
	 */
	private EnterpriseEntity registEnterprise(TenantInfoDto dto, LocalAuthDto user) {
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

		// 关联企业与创建者
		Set<Integer> userIds = new HashSet<Integer>(1);
		userIds.add(user.getId());
		entity.setUserIds(userIds);
		entity = enterpriseDao.save(entity);

		// TODO 初始化租户内角色/人员信息
		//RequestContextHolder.getRequestAttributes().removeAttribute("tenantMgt", 0);
		//RequestContextHolder.getRequestAttributes().setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID, 0);

		return entity;
	}

	/**
	 * 初始化项目组空间
	 * @param entity
	 * @param user
	 * @param subEnterpriseName
	 */
	private SubEnterpriseEntity initSubEnterprise(EnterpriseEntity enterpriseEntity, LocalAuthDto user, String subEnterpriseName) {
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
		
		// 更新用户信息
		AccountDto userModify = new AccountDto();
		userModify.setUserId(user.getId());
		if (ConstantContainer.TOURIST.equals(user.getCurTenantId())) {
			userModify.setCurTenantId(subEnterpriseEntity.getTenantId());
		}
		userServiceClient.saveOrUpdateUserAuth(userModify);
		
		return subEnterpriseEntity;
		
	}
	
	private void initTenMgt(int enterpriseId, int subEnterpriseId, int userId) {
		// TODO 初始化管理员角色
		/*
		 * RoleEntity admin = new RoleEntity(); admin.setName("ROLE_admin");
		 * admin.setTenantId(tenantId); roleRepository.save(admin); if (null !=
		 * user.getRoles()) { user.getRoles().add(admin); } else { Set<RoleEntity> roles
		 * = new HashSet<>(); roles.add(admin); user.setRoles(roles); }
		 * 
		 * USER_DAO.saveUserAuth(user);
		 */
	}

	/**
	 * 申请加入租户
	 * 
	 */
	public ResponseDto applyJoinTenant(int userId, String tenantId) {

		LocalAuthDto user = userServiceClient.selectLocalAuthById(userId);
		if (enterpriseDao.selectByTenantId(tenantId).getUserIds().contains(userId)) {
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
	 * 获取待审批的加入租户申请
	 * 
	 * @param userId
	 * @param first
	 * @param max
	 * @return
	 */
	public ProcessDataDto getUserTasksJoin(int userId, int first, int max) {
		long totalNum = taskService.createTaskQuery().processDefinitionKey(WorkflowType.APPLY_JOIN_TENANT.name())
				.active().taskCandidateUser(String.valueOf(userId)).count();
		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(WorkflowType.APPLY_JOIN_TENANT.name())
				.active().taskCandidateUser(String.valueOf(userId)).orderByTaskCreateTime().desc().listPage(first, max);
		List<TaskTenantApplyResp> list = new ArrayList<TaskTenantApplyResp>();
		for (Task task : tasks) {
			TaskTenantApplyResp tenantApplyResp = new TaskTenantApplyResp();
			// 流程第一个task，时间和process相同
			tenantApplyResp.setCreateTime(task.getCreateTime().toLocaleString());
			tenantApplyResp.setTaskId(task.getId());
			tenantApplyResp.setApplicant((int) taskService.getVariable(task.getId(), "userId"));
			tenantApplyResp.setType(WorkflowType.APPLY_JOIN_TENANT);
			TenantInfoDto tenantInfo = new TenantInfoDto();
			String tenantId = (String) taskService.getVariable(task.getId(), "tenantId");
			EnterpriseEntity enterpriseEntity = enterpriseDao.selectByTenantId(tenantId);
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

	/**
	 * 获取加入租户申请的历史工作流
	 * 
	 * @param userId
	 * @param first
	 * @param max
	 */
	public ProcessDataDto getJoinTenantHisPro(int userId, int first, int max) {

		long totalNum = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_JOIN_TENANT.name()).involvedUser(String.valueOf(userId))
				.count();
		List<HisProTenantApplyReq> dtos = new ArrayList<HisProTenantApplyReq>();
		List<HistoricProcessInstance> histotricProcess = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(WorkflowType.APPLY_JOIN_TENANT.name()).involvedUser(String.valueOf(userId))
				/* .finished() */.orderByProcessInstanceEndTime().desc().listPage(first, max);
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
					hisProTenantApplyDto.setStartUser((int) historicVariableInstance.getValue());
				} else if ("approved".equals(historicVariableInstance.getVariableName())) {
					hisProTenantApplyDto.setApproved((boolean) historicVariableInstance.getValue());
				} else if ("remark".equals(historicVariableInstance.getVariableName())) {
					hisProTenantApplyDto.setRemark((String) historicVariableInstance.getValue());
				} else if ("tenantId".equals(historicVariableInstance.getVariableName())) {
					EnterpriseEntity enterpriseEntity = enterpriseDao
							.selectByTenantId((String) historicVariableInstance.getValue());
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

		ProcessDataDto dataDto = new ProcessDataDto();
		dataDto.setData(dtos);
		dataDto.setTotalNum(totalNum);
		return dataDto;
	}

	/**
	 * 获取加入项目组的审批人
	 * 
	 * @param execution
	 * @return
	 */
	public Set<String> findCandidateUsersJoin(DelegateExecution execution) {
		//TODO 获取租户创建人
		//List<LocalAuth> admins = USER_DAO.selectUserByRole("ROLE_admin", (String) execution.getVariable("tenantId"));
		Set<String> list = new HashSet<String>();
		//admins.forEach(n -> list.add(String.valueOf(n.getId())));
		
		//TODO 获取租户管理员
		RequestContextHolder.getRequestAttributes().removeAttribute("tenantMgt", 0);
		/*List<Integer> creators = HIRE_USER_DAO.selectByRole("ROLE_creator",
				(String) execution.getVariable("tenantId"));
		RequestContextHolder.getRequestAttributes().setAttribute("tenantMgt", ConstantContainer.TENANT_SHARED_ID, 0);
		creators.forEach(n -> list.add(String.valueOf(n.getUserId())));*/

		// 制造用户消息
		String tenantId = (String) execution.getVariable("tenantId");
		EnterpriseEntity enterpriseEntity = enterpriseDao.selectByTenantId(tenantId);
		int userId = (int) execution.getVariable("userId");
		LocalAuthDto applicant = userServiceClient.selectLocalAuthById(userId);
		for (String approverId : list) {
			messageServiceClient.produceApproveMsg(applicant.getRealName() + "(" + applicant.getUserName() + ")",
					Integer.parseInt(approverId), enterpriseEntity.getName(), WorkflowType.APPLY_JOIN_TENANT);
		}

		return list;
	}

	/**
	 * 添加用户至租户
	 * 
	 * @param userId
	 */
	public void addUserToTenant(DelegateExecution execution) {
		boolean isApproved = (boolean) execution.getVariable("approved");
		int userId = (int) execution.getVariable("userId");
		String remark = (String) execution.getVariable("remark");
		String tenantId = (String) execution.getVariable("tenantId");
		EnterpriseEntity enterprise = enterpriseDao.selectByTenantId(tenantId);
		if (isApproved) {
			// 用户添加至租户
			LocalAuthDto user = userServiceClient.selectLocalAuthById(userId);
			if (enterprise.getUserIds().contains(user.getId())) {
				return; // 已加入该租户
			}
			enterprise.getUserIds().add(userId);
			enterprise = enterpriseDao.save(enterprise);
			// 用户关联租户
			if (ConstantContainer.TOURIST.equals(user.getCurTenantId())) {
				user.setCurTenantId(tenantId);
			}
		}

		// 制造消息通知
		messageServiceClient.produceApplyMsg(userId, isApproved, enterprise.getName(), remark,
				WorkflowType.APPLY_JOIN_TENANT);
	}

	/**
	 * 手动添加用户
	 * 
	 * @param userId
	 */
	public void manuallyAddUser(String tenantId, List<String> userIds) {

		for (String userId : userIds) {

			LocalAuthDto user = userServiceClient.selectLocalAuthById(Integer.parseInt(userId));
			if (tenantId.startsWith("t")) {
				// 当前租户id为企业
				EnterpriseEntity enterprise = enterpriseDao.selectByTenantId(tenantId);
				enterprise.getUserIds().add(Integer.parseInt(userId));
				enterprise = enterpriseDao.save(enterprise);
				
			} else if (tenantId.startsWith("s")) {
				// 当前租户id为项目组
				SubEnterpriseEntity subEnterpriseEntity = subEnterpriseDao.selectBySubTenantId(tenantId);
				subEnterpriseEntity.getUserIds().add(Integer.parseInt(userId));
				subEnterpriseDao.save(subEnterpriseEntity);
			}
			AccountDto userModify = new AccountDto();
			userModify.setUserId(user.getId());
			if (ConstantContainer.TOURIST.equals(user.getCurTenantId())) {
				userModify.setCurTenantId(tenantId);
			}
			// 保存用户
			userServiceClient.saveOrUpdateUserAuth(userModify);
		}

	}

	public ResponseDto switchTenant(String tenantId, HttpServletRequest request) {
		if (null == enterpriseDao.selectByTenantId(tenantId)
				&& null == subEnterpriseDao.selectBySubTenantId(tenantId)) {
			return ResponseDto.builder(ErrorMsg.TENANT_NOT_EXIST);
		}
		int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
		LocalAuthDto user = userServiceClient.selectLocalAuthById(userId);
		// 更新用户当前租户标识
		AccountDto userModify = new AccountDto();
		userModify.setUserId(userId);
		userModify.setCurTenantId(tenantId);
		userServiceClient.saveOrUpdateUserAuth(userModify);
		// TODO 刷新内存中的认证信息
		/*SecurityContextImpl contextImpl = (SecurityContextImpl) request.getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");
		LocalAuthDetails authDetails = (LocalAuthDetails) contextImpl.getAuthentication().getPrincipal();
		authDetails.setCurTenantId(tenantId);
		authDetails.getRoles().clear();
		Object passWord = contextImpl.getAuthentication().getCredentials();

		// 更新角色
		ArrayList<GrantedAuthority> temp = new ArrayList<GrantedAuthority>();
		Set<RoleEntity> roles = user.getRoles();
		authDetails.getRoles().addAll(roles);// 封装session中角色信息
		if (!CollectionUtils.isEmpty(roles)) {
			// 基本角色
			for (RoleEntity roleEntity : roles) {
				if (roleEntity.getTenantId().equals(authDetails.getCurTenantId())) {
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleEntity.getName());
					temp.add(authority);
				}
			}
		}
		HireUser hireUser = HIRE_USER_DAO.selectUserBytenantId(user.getId(), user.getCurTenantId());
		if (null != hireUser) {
			// 租户下角色
			int roleId = 100;
			for (HireRoleEntity hireRole : hireUser.getHireRoleEntities()) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(hireRole.getName());
				temp.add(authority);
				// 封装session中角色信息
				RoleEntity roleEntity = new RoleEntity();
				roleEntity.setId(roleId);
				roleId++;
				roleEntity.setName(hireRole.getName());
				roleEntity.setTenantId(user.getCurTenantId());
				authDetails.getRoles().add(roleEntity);
			}
		}
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authDetails,
				passWord, Collections.unmodifiableList(temp));
		authenticationToken.setDetails(contextImpl.getAuthentication().getDetails());
		contextImpl.setAuthentication(authenticationToken);

		// 更新内存中的session
		request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", contextImpl);*/
		return ResponseDto.builder(ErrorMsg.OK);
	}

	public ResponseDto createSubTenant(int creator, String name, List<Integer> userIds) {
		
		/*if (null != subEnterpriseDao.selectByName(name)) {
			return ResponseDto.builder(ErrorMsg.SUB_ENTERPRISE_EXIST);
		}*/
		LocalAuthDto authDetails = userServiceClient.selectLocalAuthById(creator);
		String tenantId = authDetails.getCurTenantId();
		EnterpriseEntity enterpriseEntity = enterpriseDao.selectByTenantId(tenantId);
		SubEnterpriseEntity subEnterpriseEntity = new SubEnterpriseEntity();
		subEnterpriseEntity.setName(name);
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
		userIds.add(authDetails.getId());
		addUserToSubEnter(subEnterpriseEntity, userIds);// 添加用户至项目组
		subEnterpriseDao.save(subEnterpriseEntity);

		// TODO 初始化项目组管理员角色
		/*RoleEntity admin = new RoleEntity();
		admin.setName("ROLE_admin");
		admin.setTenantId(subEnterpriseEntity.getSubTenantId());
		roleRepository.save(admin);
		LocalAuth user = USER_DAO.selectUserAuthById(authDetails.getId());
		if (null != user.getRoles()) {
			user.getRoles().add(admin);
		} else {
			Set<RoleEntity> roles = new HashSet<>();
			roles.add(admin);
			user.setRoles(roles);
		}
		USER_DAO.saveUserAuth(user);
		List<LocalAuth> tenantAdmins = USER_DAO.selectUserByRole("ROLE_admin", tenantId);
		for (LocalAuth localAuth : tenantAdmins) {
			localAuth.getRoles().add(admin);
			USER_DAO.saveUserAuth(localAuth);
		}*/

		
		
		
		
		// TODO 初始化项目组内角色信息
		/*HireRoleEntity manager = new HireRoleEntity();
		manager.setName("ROLE_manager");
		hireRoleDaoImpl.save(manager, subEnterpriseEntity.getSubTenantId());
		HireRoleEntity creator = new HireRoleEntity();
		creator.setName("ROLE_creator");
		hireRoleDaoImpl.save(creator, subEnterpriseEntity.getSubTenantId());
		HireRoleEntity adminHire = new HireRoleEntity();
		adminHire.setName("ROLE_admin");
		hireRoleDaoImpl.save(adminHire, subEnterpriseEntity.getSubTenantId());

		// 创建项目组管理员
		HireUser hireUser = new HireUser();
		hireUser.setUserId(user.getId());
		Set<HireRoleEntity> roles = new HashSet<HireRoleEntity>();
		roles.add(creator);
		roles.add(adminHire);
		hireUser.setHireRoleEntities(roles);
		HIRE_USER_DAO.save(hireUser, subEnterpriseEntity.getSubTenantId());*/

		return ResponseDto.builder(ErrorMsg.OK);
	}

	public void manuallyAddUserToSubTenant(String subTenantId, List<String> userIds) {
		List<Integer> idList = new ArrayList<Integer>();
		userIds.forEach(id -> idList.add(Integer.parseInt(id)));
		addUserToSubEnter(subEnterpriseDao.selectBySubTenantId(subTenantId), idList);
	}

	private void addUserToSubEnter(SubEnterpriseEntity subEnterpriseEntity, List<Integer> userIds) {
		
		if (!CollectionUtils.isEmpty(subEnterpriseEntity.getUserIds())) {
			subEnterpriseEntity.getUserIds().addAll(userIds);
		} else {
			subEnterpriseEntity.setUserIds(new HashSet<Integer>(userIds));
		}
		
	}

	public void deleteSubTenant(int id, HttpServletRequest request) {

		SubEnterpriseEntity entity = subEnterpriseDao.selectById(id);
		//RoleEntity roleEntity = roleRepository.findByTenantId(entity.getSubTenantId());
		for (Integer userId : entity.getUserIds()) {
			LocalAuthDto user = userServiceClient.selectLocalAuthById(userId);
			//TODO 删除对应用户角色
			//user.getRoles().remove(roleEntity);
			
			
			
			if (entity.getTenantId().equals(user.getCurTenantId())) {
				user.setCurTenantId(ConstantContainer.TOURIST);// 切换至游客
			}
		}
		// TODO 删除项目组相关角色信息表
		//roleRepository.delById(roleEntity.getId());
		// 删除项目组
		subEnterpriseDao.delSubTenantById(id);
		// 切换当前操作用户至租户下
		switchTenant(entity.getTenantId(), request);
	}

	public List<EnterpriseInfoDto> getEnterpriseAll() {
		List<EnterpriseEntity> enterpriseList = enterpriseDao.selectAll();
		List<EnterpriseInfoDto> dtos = new ArrayList<EnterpriseInfoDto>();
		for (EnterpriseEntity enterpriseEntity : enterpriseList) {
			EnterpriseInfoDto dto = new EnterpriseInfoDto();
			BeanUtils.copyProperties(enterpriseEntity, dto);
			dtos.add(dto);
		}
		return dtos;

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
