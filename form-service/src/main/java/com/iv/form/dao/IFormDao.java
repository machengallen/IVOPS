package com.iv.form.dao;

import com.iv.common.enumeration.CycleType;
import com.iv.dto.*;
import com.iv.dto.FormInfoPage;
import com.iv.enumeration.FormState;
import com.iv.form.entity.*;

import java.util.List;
import java.util.Map;

/**
 * 工单相关接口
 * @author liangk
 * 2018年5月7日
 * form-service-api-1.0.0-SNAPSHOT
 */
public interface IFormDao {

    void saveOrUpdateForm(FormInfoEntity formInfoEntity);


    //根据工单Id查询工单信息
    FormInfoEntity selectFormById(String formId);
    //根据工单Id查询工单信息Map
    Map selectFormMapById(String formId);

    //查询工单的流转记录
    List<FormChangeLogsEntity> selectFormChangeById(String formId);
    //查询工单的流转记录
    List<Map> selectFormChangeMapById(String formId);

    //查询工单操作记录
    List<FormOperateLogsEntity>  selectFormOperateById(String formId);
    //查询工单操作记录
    List<Map>  selectFormOperateMapById(String formId);

    //查询工单评价记录
    FormEvaluateEntity selectFormEvaluateById(String formId);
    //查询工单评价记录
    Map selectFormEvaluateMapById(String formId);

    //查询工单升级记录
    List<FormUpgradeLogsEntity>  selectFormUpgradeById(String formId);
    //查询工单升级记录
    List<Map>  selectFormUpgradeMapById(String formId);

    //我的工单分页查询
    FormInfoPage selectMyFormListPage(int userId, FormConditionDto formConditionDto,String demandIds);

    //我的回收站
    FormInfoPage selectDelFormListPage(int userId, FormConditionDto formConditionDto,String demandIds);

    //查询标星邮件
    FormInfoPage selectMarkFormListPage(int userId, FormConditionDto formConditionDto,String demandIds);

    //逻辑删除工单
    void delFormByLogic(int userId,String formId);
    void delFormByLogic(FormInfoEntity formInfoEntity);

    //保存修改工单流转记录
    void saveOrUpdateFormChanges(FormChangeLogsEntity formChangeLogsEntity);

    //保存操作记录
    void saveOrUpdateFormOperate(FormOperateLogsEntity formOperateLogsEntity);


    //保存评价记录
    void saveOrUpdateFormEvaluate(FormEvaluateEntity formEvaluateEntity);

    //标星工单
    void saveOrUpdateMarkForm(FormMarksEntity formMarksEntity);

    //删除标星工单
    void delMarkForm(int userId, String formId);

    //工单类型饼状图
    List<Map> selectPieChartByKind(Long date);

    //柱状图
    //List selectBarGraphByGroup();

    //工单统计数据
    Map selectDataCollection(Long date);

    //工单列表
    FormInfoPage selectFormListPage(FormConditionDto formConditionDto,Integer userId,String demandIds);

    //查询公司负责人信息
    List<Map> selectCompanyClients(Integer id);

    //查询公司信息
    List<Map> selectCompanyList();

    //导出工单报表
    List<Map> selectFormListExcel(Integer userId, FormConditionDto formConditionDto);



    //查询用户信息
    FormUserEntity selectUserById(Integer userId);

    //保存更新用户信息
    void saveOrUpdateUser(FormUserEntity formUserEntity);

    //查询工单状态
    List<Map> selectFormStateList();

    //申请人列表
    List<Map> selectApplicantList();

    //需求列表
    List<Map> selectDemandList();

    //审核记录
    List<Map> selectFormAuditMapById(String formId);

    //等级列表
    List<Map> selectPriorityList();
    FormPriorityEntity selectPriorityById(Integer id);

    //升级记录
    void saveOrUpdateUpgrade(FormUpgradeLogsEntity formUpgradeLogsEntity);

    //需求
    List<Map> selectDemandByParent(Integer parentId);
    //需求
    FormDemandEntity selectDemandById(Integer id);

    //保存审核记录
    void saveOrUpdateAudit(FormAuditEntity formAuditEntity);

    //趋势图
    List<Object[]> groupByCycle(CycleType cycle, Long timePoint,FormState formState);

    //工单状态
    FormStateEntity selectFormStateById(Integer id);

    //用户
    FormUserEntity selectFormUser(Integer handlerId);

    //公司
    FormClientEntity selectCompanyById(Integer id);

    //查询邮件基础信息
    //FormInfoEntityDto selectEmailById(Integer form);

    //组绩效
    List<Map> selectGroupMeirt(FormState formState,Long startTime,Long endTime,Integer demandCode);

    //总绩效
    Map selectTotalMeirt(Long startTime,Long endTime,Integer demandCode);

    //个人绩效
    CommonPage selectPersonMeirt(MeritConditionDto meritConditionDto);

    //审核人
    void saveOrUpdateFormAuditPerson(FormAuditPersonEntity formAuditPersonEntity);
}
