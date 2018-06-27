package com.iv.form.service;

import com.alibaba.fastjson.JSONArray;
import com.iv.common.enumeration.CycleType;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.constant.BusException;
import com.iv.constant.Constant;
import com.iv.constant.ErrorMsg;
import com.iv.dto.*;
import com.iv.enter.dto.GroupQuery;
import com.iv.enumeration.FormState;
import com.iv.form.dao.IFormDao;
import com.iv.form.dao.IFormOptDao;
import com.iv.form.dao.impl.IFormDaoImpl;
import com.iv.form.dao.impl.IFormOptDaoImpl;
import com.iv.form.dto.ConditionInfoDto;
import com.iv.form.dto.FormPullDownDto;
import com.iv.form.entity.*;
import com.iv.form.feign.clients.GroupServiceClient;
import com.iv.form.feign.clients.SubTenantPermissionServiceClient;
import com.iv.form.feign.clients.UserServiceClient;
import com.iv.form.util.CycleUtil;
import com.iv.form.util.ExcelUtil;
import com.iv.outer.dto.LocalAuthDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.Normalizer;
import java.util.*;


/**
 * @author liangk
 * 2018年5月7日
 * form-service-api-1.0.0-SNAPSHOT
 */
@Service
//@Transactional
public class FormService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormService.class);

    private static final IFormDao FORM_DAO = IFormDaoImpl.getInstance();
    private static final IFormOptDao FORM_OPT_DAO = IFormOptDaoImpl.getInstance();

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private GroupServiceClient groupServiceClient;

    @Autowired
    private SubTenantPermissionServiceClient subTenantPermissionServiceClient;

    @Autowired
    private EmailService emailService;
    @Autowired
    private WechatService wechatService;






    /**
     * 新增修改工单信息
     * @param formMsgDto 工单信息实体
     * @param userId 用户id
     * @return
     */
    //@Transactional
    public FormInfoEntity saveOrUpdateFormInfo(FormMsgDto formMsgDto, int userId) throws BusException{
        //校验参数
        if(StringUtils.isEmpty(userId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }
        //校验
        if(StringUtils.isEmpty(formMsgDto.getDemandTypeCode())
                ||StringUtils.isEmpty(formMsgDto.getFormApplyTime())||StringUtils.isEmpty(formMsgDto.getFormExpectEndTime())||StringUtils.isEmpty(formMsgDto.getFormOwnerId())
        ||StringUtils.isEmpty(formMsgDto.getFormOwnerPhone())||StringUtils.isEmpty(formMsgDto.getHandlerId())||StringUtils.isEmpty(formMsgDto.getUnitCode())||StringUtils.isEmpty(formMsgDto.getPriority())){
            throw new BusException(ErrorMsg.FORM_NULL_FAILED);
        }

        //用户信息
        saveOrUpdateUser(userId);
        saveOrUpdateUser(formMsgDto.getHandlerId());

        FormInfoEntity formInfoEntity = new FormInfoEntity();

        if (StringUtils.isEmpty(formMsgDto.getId())){//新增

            //formInfoEntity.setId(formMsgDto.getId());
            formInfoEntity.setUnitCode(formMsgDto.getUnitCode());
            formInfoEntity.setApplicantId(userId);
            formInfoEntity.setDemandTypeCode(formMsgDto.getDemandTypeCode());
            formInfoEntity.setFormApplyTime(formMsgDto.getFormApplyTime());
            formInfoEntity.setFormExpectEndTime(formMsgDto.getFormExpectEndTime());
            formInfoEntity.setFormOwnerId(formMsgDto.getFormOwnerId());
            formInfoEntity.setFormOwnerPhone(formMsgDto.getFormOwnerPhone());
            formInfoEntity.setGroupId(formMsgDto.getGroupId());
            formInfoEntity.setHandlerId(formMsgDto.getHandlerId());
            formInfoEntity.setFormState(Constant.FORM_STATE_INIT);
            formInfoEntity.setPriority(formMsgDto.getPriority());
            formInfoEntity.setDelFlag(Constant.DEL_FLAG_FALSE);
            formInfoEntity.setSourceType(Constant.FORM_SOURCE_CUSTOMER_SERVICE);
            formInfoEntity.setRelationFormId(formMsgDto.getRelationFormId());
            formInfoEntity.setDemandContent(formMsgDto.getDemandContent());
            formInfoEntity.setCreateBy(userId);
            formInfoEntity.setCreateDate(System.currentTimeMillis());



        }else {//修改
            formInfoEntity=FORM_DAO.selectFormById(formMsgDto.getId());
            formInfoEntity.setUnitCode(formMsgDto.getUnitCode());
            formInfoEntity.setApplicantId(userId);
            formInfoEntity.setDemandTypeCode(formMsgDto.getDemandTypeCode());
            formInfoEntity.setFormApplyTime(formMsgDto.getFormApplyTime());
            formInfoEntity.setFormExpectEndTime(formMsgDto.getFormExpectEndTime());
            formInfoEntity.setFormOwnerId(formMsgDto.getFormOwnerId());
            formInfoEntity.setFormOwnerPhone(formMsgDto.getFormOwnerPhone());
            formInfoEntity.setGroupId(formMsgDto.getGroupId());
            formInfoEntity.setHandlerId(formMsgDto.getHandlerId());
            formInfoEntity.setFormState(Constant.FORM_STATE_INIT);
            formInfoEntity.setPriority(formMsgDto.getPriority());
            formInfoEntity.setDelFlag(Constant.DEL_FLAG_FALSE);
            formInfoEntity.setSourceType(Constant.FORM_SOURCE_CUSTOMER_SERVICE);
            formInfoEntity.setRelationFormId(formMsgDto.getRelationFormId());
            formInfoEntity.setDemandContent(formMsgDto.getDemandContent());
            formInfoEntity.setUpdateBy(userId);
            formInfoEntity.setUpdateDate(System.currentTimeMillis());
        }

        //保存文件
        if (formMsgDto.getFileIds()!=null){
            formInfoEntity.getFiles().clear();
            HashSet<FormFileEntity> fileEntities = new HashSet<>();
            for(Integer fileId:formMsgDto.getFileIds()){
                FormFileEntity formFileEntity = FORM_OPT_DAO.selectFormFile(fileId);
                fileEntities.add(formFileEntity);
            }
            formInfoEntity.setFiles(fileEntities);
        }

        FORM_DAO.saveOrUpdateForm(formInfoEntity);


        if (StringUtils.isEmpty(formMsgDto.getId())){//新增

            //插入流转记录表
            FormChangeLogsEntity formChangeLogsEntity = new FormChangeLogsEntity();
            formChangeLogsEntity.setFormId(formInfoEntity.getId());
            formChangeLogsEntity.setFormState(Constant.FORM_STATE_INIT);
            formChangeLogsEntity.setChangeContent(Constant.FORM_STATE_CONTENT_INIT);
            formChangeLogsEntity.setCreateBy(userId);
            formChangeLogsEntity.setCreateDate(System.currentTimeMillis());
            FORM_DAO.saveOrUpdateFormChanges(formChangeLogsEntity);

        }else {//修改

        }

        //发邮件，以及微信
        emailService.sendEmail(formInfoEntity);
        String code="";
        wechatService.sendWechat(formInfoEntity,code);

        return formInfoEntity;
    }

    /**
     * 查询工单信息
     * @param formId
     * @return
     */
    public FormAllMsgDto selectFormById(String formId)throws BusException{
        //校验参数
        if(StringUtils.isEmpty(formId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //查询信息
        Map formInfo = FORM_DAO.selectFormMapById(formId);
        List<Map> files= FORM_DAO.selectFileByFormId(formId);
//        Map formEvaluate = FORM_DAO.selectFormEvaluateMapById(formId);
       /* List<Map> formChangeLogsList = FORM_DAO.selectFormChangeMapById(formId);
        List<Map> formOperateLogsList = FORM_DAO.selectFormOperateMapById(formId);
        List<Map> formUpgradeLogsList= FORM_DAO.selectFormUpgradeMapById(formId);
        List<Map> formAuditList = FORM_DAO.selectFormAuditMapById(formId);*/


        FormAllMsgDto formAllMsgDto = new FormAllMsgDto();
        formAllMsgDto.setFormInfo(formInfo);
        formAllMsgDto.setFiles(files);
//        formAllMsgDto.setFormEvaluate(formEvaluate);
        /*formAllMsgDto.setFormChangeLogsList(formChangeLogsList);
        formAllMsgDto.setFormUpgradeLogsList(formUpgradeLogsList);
        formAllMsgDto.setFormOperateLogsList(formOperateLogsList);
        formAllMsgDto.setFormAuditList(formAuditList);*/


        return formAllMsgDto;
    }

    /**
     * 查询工单信息
     * @param formId
     * @return
     */
    public Map selectFormByCallBack(String formId)throws BusException{
        //校验参数
        if(StringUtils.isEmpty(formId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //查询信息
        Map formInfo = FORM_DAO.selectFormMapByCallBack(formId);


        return formInfo;
    }


    /**
     *查询我的工单列表
     * @param userId
     * @param formConditionDto
     * @return
     * @throws BusException
     */
    public FormInfoPage selectMyFormListPage(HttpServletRequest request,int userId, FormConditionDto formConditionDto)throws BusException {
        //校验参数
        if(StringUtils.isEmpty(userId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //设置默认当前页和间距
        formConditionDto.setCurPage(formConditionDto.getCurPage()==0?1:formConditionDto.getCurPage());
        formConditionDto.setItems(formConditionDto.getItems()==0?10:formConditionDto.getItems());

        List<Object> childrenIds = new ArrayList<>();
        //判断服务目录
        if(!StringUtils.isEmpty(formConditionDto.getDemandTypeCode())){
            FormDemandEntity entity = FORM_OPT_DAO.selectDemandAndChildrenById(formConditionDto.getDemandTypeCode());
            childrenIds = getChildren( entity, new ArrayList<>());
        }
        String demandIds = StringUtils.collectionToCommaDelimitedString(childrenIds);



        FormInfoPage  formInfoPage=FORM_DAO.selectMyFormListPage(userId,formConditionDto,demandIds);



        //添加工单记录
        List<LinkedHashMap> data = groupServiceClient.groupsUsersInfo()==null?null:(List<LinkedHashMap>)groupServiceClient.groupsUsersInfo().getData();

        //添加工单记录

        for(int i=0;i< formInfoPage.getFormInfoList().size();i++){

            String id = formInfoPage.getFormInfoList().get(i).get("id").toString();
            String groupId = formInfoPage.getFormInfoList().get(i).get("groupId").toString();

            List<Map> changeList = FORM_DAO.selectFormChangeMapById(id);
            List<Map> operateList = FORM_DAO.selectFormOperateMapById(id);
            List<Map> upgradeList = FORM_DAO.selectFormUpgradeMapById(id);
            //组名
            formInfoPage.getFormInfoList().get(i).put("groupName",getGroupName(data,groupId));
            //流转记录
            formInfoPage.getFormInfoList().get(i).put("changeList",changeList);
            //操作记录
            formInfoPage.getFormInfoList().get(i).put("operateList",operateList);
            //升级记录
            formInfoPage.getFormInfoList().get(i).put("upgradeList",upgradeList);

        }

        return formInfoPage;
    }

    /**
     * 查询回收站
     * @param userId
     * @param formConditionDto
     * @return
     * @throws BusException
     */
    public FormInfoPage selectDelFormListPage(HttpServletRequest request,int userId, FormConditionDto formConditionDto)throws BusException {
        //校验参数
        if(StringUtils.isEmpty(userId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //设置默认当前页和间距
        formConditionDto.setCurPage(formConditionDto.getCurPage()==0?1:formConditionDto.getCurPage());
        formConditionDto.setItems(formConditionDto.getItems()==0?10:formConditionDto.getItems());


        List<Object> childrenIds = new ArrayList<>();
        //判断服务目录
        if(!StringUtils.isEmpty(formConditionDto.getDemandTypeCode())){
            FormDemandEntity entity = FORM_OPT_DAO.selectDemandAndChildrenById(formConditionDto.getDemandTypeCode());
            childrenIds = getChildren( entity, new ArrayList<>());
        }
        String demandIds = StringUtils.collectionToCommaDelimitedString(childrenIds);

        FormInfoPage  formInfoPage=FORM_DAO.selectDelFormListPage(userId,formConditionDto,demandIds);

        //添加工单记录

        List<LinkedHashMap> data = groupServiceClient.groupsUsersInfo()==null?null:(List<LinkedHashMap>)groupServiceClient.groupsUsersInfo().getData();

        //添加工单记录

        for(int i=0;i< formInfoPage.getFormInfoList().size();i++){

            String id = formInfoPage.getFormInfoList().get(i).get("id").toString();
            String groupId = formInfoPage.getFormInfoList().get(i).get("groupId").toString();

            List<Map> changeList = FORM_DAO.selectFormChangeMapById(id);
            List<Map> operateList = FORM_DAO.selectFormOperateMapById(id);
            List<Map> upgradeList = FORM_DAO.selectFormUpgradeMapById(id);
            //组名
            formInfoPage.getFormInfoList().get(i).put("groupName",getGroupName(data,groupId));
            //流转记录
            formInfoPage.getFormInfoList().get(i).put("changeList",changeList);
            //操作记录
            formInfoPage.getFormInfoList().get(i).put("operateList",operateList);
            //升级记录
            formInfoPage.getFormInfoList().get(i).put("upgradeList",upgradeList);

        }

        return formInfoPage;

    }

    /**
     * 查询星标工单
     * @param userId
     * @param formConditionDto
     * @return
     * @throws BusException
     */
    public FormInfoPage selectMarkFormListPage(HttpServletRequest request,int userId, FormConditionDto formConditionDto)throws BusException {
        //校验参数
        if(StringUtils.isEmpty(userId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //设置默认当前页和间距
        formConditionDto.setCurPage(formConditionDto.getCurPage()==0?1:formConditionDto.getCurPage());
        formConditionDto.setItems(formConditionDto.getItems()==0?10:formConditionDto.getItems());

        List<Object> childrenIds = new ArrayList<>();
        //判断服务目录
        if(!StringUtils.isEmpty(formConditionDto.getDemandTypeCode())){
            FormDemandEntity entity = FORM_OPT_DAO.selectDemandAndChildrenById(formConditionDto.getDemandTypeCode());
            childrenIds = getChildren( entity, new ArrayList<>());
        }
        String demandIds = StringUtils.collectionToCommaDelimitedString(childrenIds);
        FormInfoPage  formInfoPage=FORM_DAO.selectMarkFormListPage(userId,formConditionDto,demandIds);
        //添加工单记录

        List<LinkedHashMap> data = groupServiceClient.groupsUsersInfo()==null?null:(List<LinkedHashMap>)groupServiceClient.groupsUsersInfo().getData();

        //添加工单记录

        for(int i=0;i< formInfoPage.getFormInfoList().size();i++){

            String id = formInfoPage.getFormInfoList().get(i).get("id").toString();
            String groupId = formInfoPage.getFormInfoList().get(i).get("groupId").toString();

            List<Map> changeList = FORM_DAO.selectFormChangeMapById(id);
            List<Map> operateList = FORM_DAO.selectFormOperateMapById(id);
            List<Map> upgradeList = FORM_DAO.selectFormUpgradeMapById(id);
            //组名
            formInfoPage.getFormInfoList().get(i).put("groupName",getGroupName(data,groupId));
            //流转记录
            formInfoPage.getFormInfoList().get(i).put("changeList",changeList);
            //操作记录
            formInfoPage.getFormInfoList().get(i).put("operateList",operateList);
            //升级记录
            formInfoPage.getFormInfoList().get(i).put("upgradeList",upgradeList);

        }
        return formInfoPage;
    }

    /**
     * 逻辑删除工单
     * @param userId
     */
    //@Transactional
    public void delFormByLogic(int userId,String formId) throws BusException{
        //校验参数
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(formId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //TODO 查询工单是否能删除
        //LocalAuthDto localAuthDto = userServiceClient.selectLocalAuthById(userId);

        FormInfoEntity formInfoEntity = FORM_DAO.selectFormById(formId);

        formInfoEntity.setUpdateBy(userId);
        //formInfoEntity.setUpdateName(localAuthDto.getUserName());
        formInfoEntity.setDelFlag(Constant.DEL_FLAG_TRUE);

        //逻辑删除工单
        FORM_DAO.delFormByLogic(formInfoEntity);

        //插入流转记录表
        FormChangeLogsEntity formChangeLogsEntity = new FormChangeLogsEntity();
        formChangeLogsEntity.setFormId(formId);
        formChangeLogsEntity.setFormState(Constant.FORM_STATE_DEL);
        formChangeLogsEntity.setChangeContent(Constant.FORM_STATE_CONTENT_DEL);
        formChangeLogsEntity.setCreateBy(userId);
        formChangeLogsEntity.setCreateDate(System.currentTimeMillis());
        //formChangeLogsEntity.setCreateName(localAuthDto.getUserName());
        FORM_DAO.saveOrUpdateFormChanges(formChangeLogsEntity);

        //发邮件，以及微信
        emailService.sendEmail(formInfoEntity);
        String code="";
        wechatService.sendWechat(formInfoEntity,code);

    }

    /**
     * 标星工单
     * @param userId
     * @param formId
     */
    public void saveMarkForm(int userId, String formId)throws BusException {
        //校验参数
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(formId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        FormMarksEntity formMarksEntity = new FormMarksEntity();
        formMarksEntity.setFormId(formId);
        formMarksEntity.setCreateBy(userId);
        formMarksEntity.setCreateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateMarkForm(formMarksEntity);

    }

    /**
     * 删除标星
     * @param userId
     * @param formId
     */
    public void delMarkForm(int userId, String formId) throws BusException{
        //校验参数
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(formId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }
        FORM_DAO.delMarkForm(userId,formId);
    }

    /**
     * 工单类型饼状图
     * @return
     */
    public List<Map> selectPieChartByKind(Long date) {

        //public Set<GroupBySeverityDto> getSevProportion(Long date) {
            List<Map> list = FORM_DAO.selectPieChartByKind(date);

            return list;
    }

    /**
     * 员工柱状图
     * @return
     */
//    public List selectBarGraphByGroup() {
//        return FORM_DAO.selectBarGraphByGroup();
//    }


    /**
     * 查询：工单汇总数据 （处理中，已完结，满意度）
     * @return
     */
    public Map selectDataCollection(Long date) {
        return FORM_DAO.selectDataCollection(date);
    }

    /**
     * 工单列表
     * @param formConditionDto
     * @return
     */

    public FormInfoPage selectFormListPage(HttpServletRequest request,FormConditionDto formConditionDto,Integer userId) {
        //设置默认当前页和间距
        formConditionDto.setCurPage(formConditionDto.getCurPage()==0?1:formConditionDto.getCurPage());
        formConditionDto.setItems(formConditionDto.getItems()==0?10:formConditionDto.getItems());


        List<Object> childrenIds = new ArrayList<>();
        //判断服务目录
        if(!StringUtils.isEmpty(formConditionDto.getDemandTypeCode())){
            FormDemandEntity entity = FORM_OPT_DAO.selectDemandAndChildrenById(formConditionDto.getDemandTypeCode());
            childrenIds = getChildren( entity, new ArrayList<>());
        }
        String demandIds = StringUtils.collectionToCommaDelimitedString(childrenIds);


        FormInfoPage  formInfoPage=FORM_DAO.selectFormListPage(formConditionDto,userId,demandIds);


        List<LinkedHashMap> data = groupServiceClient.groupsUsersInfo()==null?null:(List<LinkedHashMap>)groupServiceClient.groupsUsersInfo().getData();


        //添加工单记录

        for(int i=0;i< formInfoPage.getFormInfoList().size();i++){

            String id = formInfoPage.getFormInfoList().get(i).get("id").toString();
            String groupId = formInfoPage.getFormInfoList().get(i).get("groupId").toString();

            List<Map> changeList = FORM_DAO.selectFormChangeMapById(id);
            List<Map> operateList = FORM_DAO.selectFormOperateMapById(id);
            List<Map> upgradeList = FORM_DAO.selectFormUpgradeMapById(id);
            //组名
            formInfoPage.getFormInfoList().get(i).put("groupName",getGroupName(data,groupId));
            //流转记录
            formInfoPage.getFormInfoList().get(i).put("changeList",changeList);
            //操作记录
            formInfoPage.getFormInfoList().get(i).put("operateList",operateList);
            //升级记录
            formInfoPage.getFormInfoList().get(i).put("upgradeList",upgradeList);

        }

        return formInfoPage;
    }

    public String getGroupName(List<LinkedHashMap> data,String groupId){
        for(LinkedHashMap map :data){
            if (groupId.equals(map.get("groupId").toString())){
                return map.get("groupName").toString();
            }
        }

        return null;
    }


    /**
     * 查询负责人以及运维组成员
     * @return
     */
    public FormGroupDto selectGroupAndClient(HttpServletRequest request,Integer id) {
        FormGroupDto formGroupDto = new FormGroupDto();
        formGroupDto.setClients(FORM_DAO.selectCompanyClients(id));
        return formGroupDto;
    }

    /**
     * 查询所有公司信息
     * @return
     */
    public List<Map> selectCompanyList() {
        return FORM_DAO.selectCompanyList();
    }

    /**
     * 查询列表条件
     * @return
     */
    public ConditionInfoDto selectConditionInfoList(HttpServletRequest request) {
        ConditionInfoDto conditionInfoDto = new ConditionInfoDto();
        //工单状态
        List<Map> formStateDtos =FORM_DAO.selectFormStateList();
        //申请人列表
        List<Map> applicantList =FORM_DAO.selectApplicantList();
        //公司信息
        List<Map> companyList =FORM_DAO.selectCompanyList();
        //需求列表
        List<FormDemandEntity> formDemandEntities = FORM_OPT_DAO.selectDemandAll();

        //处理人列表

        ResponseDto responseDto = groupServiceClient.groupsUsersInfo();
        //优先级
        List<Map> priorityList = FORM_DAO.selectPriorityList();
        Object data = responseDto==null?null:responseDto.getData();
        conditionInfoDto.setFormHandlerDtos(data);
        conditionInfoDto.setFormApplicantDtos(applicantList);
        conditionInfoDto.setFormCompanyDtos(companyList);
        conditionInfoDto.setFormDemandDtos(formDemandEntities);
        conditionInfoDto.setFormStateDtos(formStateDtos);
        conditionInfoDto.setFormPriorityDtos(priorityList);
        return conditionInfoDto;
    }


    /**
     * 开始处理
     * @param formId
     * @param userId
     * @throws BusException
     */
    //@Transactional
    public void executeTakeOver(String formId, int userId)throws BusException {
        //校验参数
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(formId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //更新数据库
        FormUserEntity user = saveOrUpdateUser(userId);
        //更改工单状态
        FormInfoEntity formInfoEntity=FORM_DAO.selectFormById(formId);
        formInfoEntity.setUpdateBy(userId);
        formInfoEntity.setUpdateDate(System.currentTimeMillis());
        formInfoEntity.setFormState(Constant.FORM_STATE_IN_HAND);
        FORM_DAO.saveOrUpdateForm(formInfoEntity);

        //记录流转表
        FormChangeLogsEntity formChangeLogsEntity = new FormChangeLogsEntity();
        formChangeLogsEntity.setFormId(formId);
        formChangeLogsEntity.setFormState(Constant.FORM_STATE_IN_HAND);
        formChangeLogsEntity.setChangeContent(Constant.FORM_STATE_CONTENT_IN_HAND);
        formChangeLogsEntity.setCreateBy(userId);
        formChangeLogsEntity.setCreateDate(System.currentTimeMillis());

        FORM_DAO.saveOrUpdateFormChanges(formChangeLogsEntity);
        //开始流程
//        Map<String, Object> variables = new HashMap<String, Object>();
//        variables.put("userId", userId);
//        variables.put("formInfoEntity", formInfoEntity);
//
//        String taskId = formActService.formActService(variables, userId, user.getRealName());

        //发邮件，以及微信
        emailService.sendEmail(formInfoEntity);
        String code="";
        wechatService.sendWechat(formInfoEntity,code);

    }

    /**
     * 工单处理完成
     * @param formOperateLogsDto
     * @param userId
     */
    //@Transactional
    public void executeDealWithEnd(FormOperateLogsDto formOperateLogsDto, int userId,String curTenantId)throws BusException {
        //校验参数
        String formId = formOperateLogsDto.getFormId();
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(formId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }



        //更新数据库
        FormUserEntity user = saveOrUpdateUser(userId);

        //更新工单状态
        FormInfoEntity formInfoEntity = FORM_DAO.selectFormById(formId);

        //查询审核人员
        Set<LocalAuthDto> localAuthDtos = subTenantPermissionServiceClient.approveFormPerson("90120", curTenantId, formInfoEntity.getGroupId().shortValue());
        if (localAuthDtos==null||localAuthDtos.size()==0){
            throw new BusException(ErrorMsg.FORM_NO_AUDIT);
        }
        for(LocalAuthDto localAuthDto:localAuthDtos){
            FormAuditPersonEntity formAuditPersonEntity = new FormAuditPersonEntity();
            formAuditPersonEntity.setFormId(formId);
            formAuditPersonEntity.setGroupId(formInfoEntity.getGroupId().shortValue());
            formAuditPersonEntity.setUserId(localAuthDto.getId());
            FORM_DAO.saveOrUpdateFormAuditPerson(formAuditPersonEntity);
        }

        formInfoEntity.setFormState(Constant.FORM_STATE_RESOLVED);
        formInfoEntity.setUpdateBy(userId);
        formInfoEntity.setUpdateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateForm(formInfoEntity);
        //保存操所记录
        FormOperateLogsEntity formOperateLogsEntity = new FormOperateLogsEntity();
        formOperateLogsEntity.setFormId(formId);
        formOperateLogsEntity.setDemandTypeCode(formOperateLogsDto.getDemandTypeCode());
        formOperateLogsEntity.setHandlerId(userId);
        formOperateLogsEntity.setCreateDate(System.currentTimeMillis());
        formOperateLogsEntity.setOperateDetails(formOperateLogsDto.getOperateDetails());
        formOperateLogsEntity.setWorkload(formOperateLogsDto.getWorkload());
        //formOperateLogsEntity.setSecondDemandTypeCode(formOperateLogsDto.getSecondDemandTypeCode());
        formOperateLogsEntity.setStartTime(formOperateLogsDto.getStartTime());
        formOperateLogsEntity.setEndTime(formOperateLogsDto.getEndTime());
        FORM_DAO.saveOrUpdateFormOperate(formOperateLogsEntity);



        //保存流转记录
        FormChangeLogsEntity formChangeLogsEntity = new FormChangeLogsEntity();
        formChangeLogsEntity.setFormId(formId);
        formChangeLogsEntity.setFormState(Constant.FORM_STATE_AUDIT);
        formChangeLogsEntity.setChangeContent(Constant.FORM_STATE_CONTENT_AUDIT);
        formChangeLogsEntity.setCreateBy(userId);
        formChangeLogsEntity.setCreateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateFormChanges(formChangeLogsEntity);

        //结束当前任务

        //发邮件，以及微信
        emailService.sendEmail(formInfoEntity);
        String code="";
        wechatService.sendWechat(formInfoEntity,code);

    }

    /**
     * 工单升级
     * @param userId
     * @param formId
     * @param handlerId 转派人员Id
     */
    public void executeUpgrade(int userId, String formId,Integer handlerGroupId ,Integer handlerId,String reason)throws BusException {
        //校验参数

        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(formId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //更新数据库
        FormUserEntity user = saveOrUpdateUser(userId);

        //更新工单状态
        FormInfoEntity formInfoEntity = FORM_DAO.selectFormById(formId);
        formInfoEntity.setGroupId(handlerGroupId);
        formInfoEntity.setHandlerId(handlerId);
        formInfoEntity.setFormState(Constant.FORM_STATE_UPGRADE);
        formInfoEntity.setUpdateBy(userId);
        formInfoEntity.setUpdateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateForm(formInfoEntity);

        //保存流转记录
        FormChangeLogsEntity formChangeLogsEntity = new FormChangeLogsEntity();
        formChangeLogsEntity.setFormId(formId);
        formChangeLogsEntity.setFormState(Constant.FORM_STATE_UPGRADE);
        formChangeLogsEntity.setChangeContent(Constant.FORM_STATE_CONTENT_UPGRADE);
        formChangeLogsEntity.setCreateBy(userId);
        formChangeLogsEntity.setCreateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateFormChanges(formChangeLogsEntity);


        FormUpgradeLogsEntity formUpgradeLogsEntity = new FormUpgradeLogsEntity();
        formUpgradeLogsEntity.setFormId(formId);
        formUpgradeLogsEntity.setUpgradeReason(reason);
        formUpgradeLogsEntity.setCreateBy(userId);
        formUpgradeLogsEntity.setCreateDate(System.currentTimeMillis());
        formUpgradeLogsEntity.setHandlerId(handlerId);
        FORM_DAO.saveOrUpdateUpgrade(formUpgradeLogsEntity);

        //TODO 升级流程

        //发邮件，以及微信
        emailService.sendEmail(formInfoEntity);
        String code="";
        wechatService.sendWechat(formInfoEntity,code);
    }


    /**
     * 审核通过
     * @param userId
     * @param formAuditDto
     */
    public void executeAuditPass(int userId, FormAuditDto formAuditDto)throws BusException {
        //校验参数
        String formId = formAuditDto.getFormId();
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(formId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //更新数据库
        FormUserEntity user = saveOrUpdateUser(userId);

        //更新工单状态
        FormInfoEntity formInfoEntity = FORM_DAO.selectFormById(formId);
        formInfoEntity.setFormState(Constant.FORM_STATE_EVALUATE);
        formInfoEntity.setUpdateBy(userId);
        formInfoEntity.setUpdateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateForm(formInfoEntity);

        //保存流转记录
        FormChangeLogsEntity formChangeLogsEntity = new FormChangeLogsEntity();
        formChangeLogsEntity.setFormId(formId);
        formChangeLogsEntity.setFormState(Constant.FORM_STATE_EVALUATE);
        formChangeLogsEntity.setChangeContent(Constant.FORM_STATE_CONTENT_EVALUATE);
        formChangeLogsEntity.setCreateBy(userId);
        formChangeLogsEntity.setCreateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateFormChanges(formChangeLogsEntity);

        //审核记录
        FormAuditEntity formAuditEntity = new FormAuditEntity();
        formAuditEntity.setFormId(formId);
        formAuditEntity.setGrade(formAuditDto.getGrade());
        formAuditEntity.setContent(formAuditDto.getContent());
        formAuditEntity.setPass(true);
        formAuditEntity.setCreateBy(userId);
        formAuditEntity.setCreateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateAudit(formAuditEntity);


        //TODO 开始流程

        //发邮件，以及微信
        emailService.sendEmail(formInfoEntity);
        String code="";
        wechatService.sendWechat(formInfoEntity,code);

    }

    /**
     * 驳回
     * @param userId
     * @param formAuditDto
     */
    public void executeAuditReject(int userId, FormAuditDto formAuditDto)throws BusException {
        //校验参数
        String formId = formAuditDto.getFormId();
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(formId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //更新数据库
        FormUserEntity user = saveOrUpdateUser(userId);

        //更新工单状态
        FormInfoEntity formInfoEntity = FORM_DAO.selectFormById(formId);
        formInfoEntity.setFormState(Constant.FORM_STATE_BACK);
        formInfoEntity.setUpdateBy(userId);
        formInfoEntity.setUpdateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateForm(formInfoEntity);

        //保存流转记录
        FormChangeLogsEntity formChangeLogsEntity = new FormChangeLogsEntity();
        formChangeLogsEntity.setFormId(formId);
        formChangeLogsEntity.setFormState(Constant.FORM_STATE_BACK);
        formChangeLogsEntity.setChangeContent(Constant.FORM_STATE_CONTENT_BACK);
        formChangeLogsEntity.setCreateBy(userId);
        formChangeLogsEntity.setCreateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateFormChanges(formChangeLogsEntity);


        //审核记录
        FormAuditEntity formAuditEntity = new FormAuditEntity();
        formAuditEntity.setFormId(formId);
        formAuditEntity.setGrade(formAuditDto.getGrade());
        formAuditEntity.setContent(formAuditDto.getContent());
        formAuditEntity.setPass(true);
        formAuditEntity.setCreateBy(userId);
        formAuditEntity.setCreateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateAudit(formAuditEntity);
        //TODO 开始流程


        //发邮件，以及微信
        emailService.sendEmail(formInfoEntity);
        String code="";
        wechatService.sendWechat(formInfoEntity,code);
    }

    /**
     * 工单评价
     * @param userId
     * @param formEvaluateDto
     */
    public void executeEvaluate(int userId, FormEvaluateDto formEvaluateDto) throws BusException{
        //校验参数
        String formId = formEvaluateDto.getFormId();
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(formId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //更新数据库
        FormUserEntity user = saveOrUpdateUser(userId);

        //更新工单状态
        FormInfoEntity formInfoEntity = FORM_DAO.selectFormById(formId);
        formInfoEntity.setFormState(Constant.FORM_STATE_END);
        formInfoEntity.setUpdateBy(userId);
        formInfoEntity.setUpdateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateForm(formInfoEntity);

        //保存评价记录
        FormEvaluateEntity formEvaluateEntity = new FormEvaluateEntity();
        formEvaluateEntity.setFormId(formId);
        formEvaluateEntity.setEvaluateLevel(formEvaluateDto.getEvaluateLevel());
        formEvaluateEntity.setClientOpinion(formEvaluateDto.getClientOpinion());
        formEvaluateEntity.setEndTime(System.currentTimeMillis());
        formEvaluateEntity.setOperateType(0);
        FORM_DAO.saveOrUpdateFormEvaluate(formEvaluateEntity);


        //保存流转记录
        FormChangeLogsEntity formChangeLogsEntity = new FormChangeLogsEntity();
        formChangeLogsEntity.setFormId(formId);
        formChangeLogsEntity.setFormState(Constant.FORM_STATE_END);
        formChangeLogsEntity.setChangeContent(Constant.FORM_STATE_CONTENT_END);
        formChangeLogsEntity.setCreateBy(userId);
        formChangeLogsEntity.setCreateDate(System.currentTimeMillis());
        FORM_DAO.saveOrUpdateFormChanges(formChangeLogsEntity);

        //TODO 结束工作流
    }


    /**
     * 导出工单报表
     * @param request
     * @param response
     * @param formConditionDto
     */
    public void FormExport(HttpServletRequest request, HttpServletResponse response, FormConditionDto formConditionDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));

        Map<String,String> headMap = new LinkedHashMap<String,String>();
        headMap.put("id","工单号");
        headMap.put("applicantName","申请人");
        headMap.put("demandTypeCodeName","需求类别");
        headMap.put("demandContent","需求内容");
        headMap.put("handlerName","处理人");
        headMap.put("startTime","开始时间");
        headMap.put("endTime","解决时间");
        headMap.put("formState","事件状态");
        headMap.put("evaluateLevel","客户评分");


        List<Map> maps = FORM_DAO.selectFormListExcel(userId, formConditionDto);
        JSONArray jsonArray = new JSONArray();
//        for (int i=0;i<formListExcel.size();i++){
//            jsonArray.add(formListExcel.get(i));
//        }

        String title = "工单统计表";
        ExcelUtil.downloadExcelFile(title,headMap,jsonArray,request,response);
    }






    //保存更新用户信息
    public FormUserEntity saveOrUpdateUser(Integer userId)throws BusException{
        //校验参数
        if(StringUtils.isEmpty(userId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //查询库里是否用这个用户
        FormUserEntity formUserEntity= FORM_DAO.selectUserById(userId);
        LocalAuthDto localAuthDto = userServiceClient.selectLocalAuthById(userId);
        if (localAuthDto==null){//调取用户信息不存在，返回空
            if(formUserEntity==null){
                return null;
            }
            return formUserEntity;
        }else{//数据库没有
            FormUserEntity user = new FormUserEntity();
            user.setId(localAuthDto.getId());
            user.setUserName(localAuthDto.getUserName());
            user.setRealName(localAuthDto.getRealName());
            user.setNickName(localAuthDto.getNickName());
            user.setEmail(localAuthDto.getEmail());
            user.setCurTenantId(localAuthDto.getCurTenantId());
            user.setHeadImgUrl(localAuthDto.getHeadimgurl());
            user.setTel(localAuthDto.getTel());
            FORM_DAO.saveOrUpdateUser(user);
            return user;
        }

    }


    /**
     * 查询工单需求页面下拉框信息
     * @return
     */
    public FormPullDownDto selectFormPullDown() {
        //公司信息
        List<Map> companyList = FORM_DAO.selectCompanyList();
        //需求信息
        List<FormDemandEntity> formDemandEntities = FORM_OPT_DAO.selectDemandAll();
        //等级信息
        List<Map> priorityList = FORM_DAO.selectPriorityList();

        FormPullDownDto formPullDownDto = new FormPullDownDto();
        formPullDownDto.setCompanyList(companyList);
        formPullDownDto.setDemandList(formDemandEntities);
        formPullDownDto.setPriorityList(priorityList);

        return formPullDownDto;
    }

    /**
     * 批量删除
     * @param userId
     * @param formIds
     * @throws BusException
     */
    public void delFormListByLogic(int userId, String[] formIds) throws BusException {
        //校验参数
        if(StringUtils.isEmpty(userId)){
            throw new BusException(ErrorMsg.FORM_HANDLER_ID_FAILED);
        }

        //更新数据库
        FormUserEntity user = saveOrUpdateUser(userId);
        if (formIds.length==0){
            return;
        }else{
            for (String formId :formIds){
                //更改工单状态
                FormInfoEntity formInfoEntity=FORM_DAO.selectFormById(formId);
                if (formInfoEntity==null){
                    continue;
                }
                formInfoEntity.setUpdateBy(userId);
                formInfoEntity.setUpdateDate(System.currentTimeMillis());
                formInfoEntity.setFormState(Constant.FORM_STATE_DEL);
                formInfoEntity.setDelFlag(Constant.DEL_FLAG_TRUE);
                FORM_DAO.saveOrUpdateForm(formInfoEntity);

                //记录流转表
                FormChangeLogsEntity formChangeLogsEntity = new FormChangeLogsEntity();
                formChangeLogsEntity.setFormId(formId);
                formChangeLogsEntity.setFormState(Constant.FORM_STATE_DEL);
                formChangeLogsEntity.setChangeContent(Constant.FORM_STATE_CONTENT_DEL);
                formChangeLogsEntity.setCreateBy(userId);
                formChangeLogsEntity.setCreateDate(System.currentTimeMillis());

                FORM_DAO.saveOrUpdateFormChanges(formChangeLogsEntity);
            }
        }


    }


    /**
     * 工程师下拉框
     * @param request
     * @return
     */
    public Object selectEngineerList(HttpServletRequest request) {
        ResponseDto responseDto = groupServiceClient.groupsUsersInfo();
        Object data = responseDto==null?null:responseDto.getData();
        return data;
    }



    /**
     * 获取指定周期内告警数量趋势
     *
     * @param cycle
     * @return
     */
    public Map getFormTrend(CycleType cycle) {
        Long timePoint = CycleType.getTimePoint(cycle);
        //总工单量
        List<Object[]> list = FORM_DAO.groupByCycle(cycle, timePoint,null);
        Set<FormNumTreDto> totalData = CycleUtil.padCycle(cycle, list, timePoint);
        //处理中的工单
        List<Object[]> inHandList = FORM_DAO.groupByCycle(cycle, timePoint,FormState.INHAND);
        Set<FormNumTreDto> inHandData = CycleUtil.padCycle(cycle, inHandList, timePoint);
        //处理完成的工单
        List<Object[]> endList = FORM_DAO.groupByCycle(cycle, timePoint,FormState.END);
        Set<FormNumTreDto> endData = CycleUtil.padCycle(cycle, endList, timePoint);
        Map<Object, Object> formTrend = new HashMap<>();
        formTrend.put("iTrendDataToDate",iTrendDataToDate(totalData,cycle));
        formTrend.put("totalData",dealWithITrendData(totalData));
        formTrend.put("inHandData",dealWithITrendData(inHandData));
        formTrend.put("endData",dealWithITrendData(endData));

        return formTrend;
    }




    /**
     * 团队绩效考核
     * @param startTime
     * @param endTime
     * @param demandCode
     * @return
     */
    public Map selectGroupMeirt(HttpServletRequest request,Long startTime,Long endTime,Integer demandCode) {

        List<LinkedHashMap> data = groupServiceClient.groupsUsersInfo()==null?null:(List<LinkedHashMap>)groupServiceClient.groupsUsersInfo().getData();
        //总记录
        List<Map> totalNum = FORM_DAO.selectGroupMeirt(null, startTime, endTime, demandCode);
        //处理
        List<Map> inhandNum = FORM_DAO.selectGroupMeirt(FormState.INHAND, startTime, endTime, demandCode);
        //完结
        List<Map> endNum = FORM_DAO.selectGroupMeirt(FormState.END, startTime, endTime, demandCode);

        Map collectNum = FORM_DAO.selectTotalMeirt(startTime, endTime, demandCode);

        List totalData = dealwithGroupMeirt(data, totalNum);
        List inhandData = dealwithGroupMeirt(data, inhandNum);
        List endData = dealwithGroupMeirt(data, endNum);
        List groupNameList = dealwithGroupName(data);
        Map<Object, Object> groupMeirt = new HashMap<>();
        groupMeirt.put("totalData",totalData);
        groupMeirt.put("inhandData",inhandData);
        groupMeirt.put("endData",endData);
        groupMeirt.put("groupNameList",groupNameList);
        groupMeirt.put("collectNum",collectNum);
        
        return groupMeirt;
    }


    /**
     * 个人绩效考核
     * @param meritConditionDto
     * @return
     */
    public CommonPage selectPersonMeirt(MeritConditionDto meritConditionDto) {

        meritConditionDto.setCurPage(meritConditionDto.getCurPage()==0?1:meritConditionDto.getCurPage());
        meritConditionDto.setItems(meritConditionDto.getItems()==0?10:meritConditionDto.getItems());

        //总记录
        CommonPage personMeirtList = FORM_DAO.selectPersonMeirt(meritConditionDto);

        return personMeirtList;
    }







    /**
     * 日期处理
     * @param datas
     * @return
     */
    private List iTrendDataToDate(Set<FormNumTreDto> datas,CycleType cycle){
        List<Object> objects = new ArrayList<>();
        for (FormNumTreDto formNumTreDto:datas){
            Object data = formNumTreDto.getNum();
            objects.add(data+CycleUtil.getTimeUnit(cycle));
        }
        return objects;
    }

    /**
     * 处理趋势数据
     * @param datas
     * @return
     */
    private List dealWithITrendData(Set<FormNumTreDto> datas){
        List<Object> objects = new ArrayList<>();

        for (FormNumTreDto trendData:datas){
            Object data = trendData.getData();
            objects.add(data);
        }
        return objects;
    }

    private List dealwithGroupMeirt(List<LinkedHashMap> data,List<Map> num){
        List<Object> list = new ArrayList<>();
        if(data==null){
            return list;
        }

        for (LinkedHashMap groupUsersPageDto:data ){
            int n=0;
            int groupId = Integer.parseInt(groupUsersPageDto.get("groupId").toString());
            for(Map map:num){
                int groupId1 = Integer.parseInt(map.get("groupId").toString());
                if (groupId == groupId1){
                    n=Integer.parseInt(map.get("num").toString());
                }
            }
            list.add(n);
        }
        return list;
    }
    private List dealwithGroupName(List<LinkedHashMap> data){

        List<Object> list = new ArrayList<>();
        if(data==null){
            return list;
        }
        for (LinkedHashMap groupUsersPageDto:data ){
            list.add(groupUsersPageDto.get("groupName").toString());
        }
        return list;
    }


    public List getChildren(FormDemandEntity formDemandEntity,List ids){
        Set<FormDemandEntity> children = formDemandEntity.getChildren();
        Integer entityId = formDemandEntity.getId();
        if(children.size()!=0){
            //删除节点
            for(FormDemandEntity entity:children){
                getChildren(entity,ids);
            }
        }
        ids.add(entityId);
        return ids;
    }


    public List getParents(FormDemandEntity entity,List ids){

        if(entity==null){
            return ids;
        }else if(entity.getParent()==null){
            ids.add(entity.getId());
            return ids;
        }else{
            ids.add(entity.getId());
            List parentIds = getParents(entity.getParent(), ids);
            return parentIds;
        }
    }


}
