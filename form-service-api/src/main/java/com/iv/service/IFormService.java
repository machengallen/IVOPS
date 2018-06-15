package com.iv.service;

import com.iv.common.enumeration.CycleType;
import com.iv.common.response.ResponseDto;
import com.iv.dto.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liangk
 * 2018年5月7日
 * form-service-api-1.0.0-SNAPSHOT
 */
public interface IFormService {


    //········工单管理       开始·······//

    /**
     *  新增：新增或者修改工单
     * @param formMsgDto
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateFormInfo", method = RequestMethod.POST)
    ResponseDto saveOrUpdateFormInfo(@RequestParam("request") HttpServletRequest  request,@RequestBody FormMsgDto formMsgDto);

    /**
     *  查询：工单列表（加筛选条件）(分页)
     * @param formConditionDto
     * @return
     */
    @RequestMapping(value = "/select/formListPage", method = RequestMethod.POST)
    ResponseDto selectFormListPage(@RequestParam("request") HttpServletRequest  request,@RequestBody FormConditionDto formConditionDto);

    /**
     * 查询：根据工单Id查询工单信息
     * @param formId
     * @return
     */
    @RequestMapping(value = "/select/formById", method = RequestMethod.GET)
    ResponseDto selectFormById(@RequestParam("formId") String formId);

    /**
     * 查询：我的工单（分页）
     * @param formConditionDto
     * @return
     */
    @RequestMapping(value = "/select/myFormListPage", method = RequestMethod.POST)
    ResponseDto selectMyFormListPage(@RequestParam("request") HttpServletRequest  request,@RequestBody FormConditionDto formConditionDto);


    /**
     * 查询：工单回收站（分页）
     * @param formConditionDto
     * @return
     */
    @RequestMapping(value = "/select/delFormListPage", method = RequestMethod.POST)
    ResponseDto selectDelFormListPage(@RequestParam("request") HttpServletRequest  request,@RequestBody FormConditionDto formConditionDto);

    //查询：标星工单

    /**
     * 查询：标星工单列表（分页）
     * @param formConditionDto
     * @return
     */
    @RequestMapping(value = "/select/markFormListPage", method = RequestMethod.POST)
    ResponseDto selectMarkFormListPage(@RequestParam("request") HttpServletRequest  request,@RequestBody FormConditionDto formConditionDto);

    /**
     * 更新：逻辑删除表单，表单进入回收站
     * @param formId
     * @return
     */
    @RequestMapping(value = "/update/delFormByLogic", method = RequestMethod.GET)
    ResponseDto delFormByLogic(@RequestParam("request") HttpServletRequest  request,@RequestParam("formId") String formId);


    /**
     * 更新：批量删除表单
     * @param formId
     * @return
     */
    @RequestMapping(value = "/update/delFormListByLogic", method = RequestMethod.GET)
    ResponseDto delFormListByLogic(@RequestParam("request") HttpServletRequest  request,@RequestParam("formIds") String[] formIds);



    /**
     * 删除：物理删除表单
     * @param formId
     * @return
     *//*
    @RequestMapping(value = "/del/delFormByPhysics", method = RequestMethod.POST)
    ResponseDto delFormByPhysics(@RequestParam("request") HttpServletRequest  request,@RequestParam("formId") String formId);*/

    /**
     * 新增：标星工单
     * @param formId
     * @return
     */
    @RequestMapping(value = "/save/MarkForm", method = RequestMethod.GET)
    ResponseDto saveMarkForm(@RequestParam("request") HttpServletRequest  request,@RequestParam("formId") String formId);

    /**
     * 删除：删除标星
     * @param formId
     * @return
     */
    @RequestMapping(value = "/del/MarkForm", method = RequestMethod.GET)
    ResponseDto delMarkForm(@RequestParam("request") HttpServletRequest  request,@RequestParam("formId") String formId);

    //········工单管理       结束·······//



    //··················工单面板   开始··················//

    /**
     * 查询：当前工单
     * @return
     */
    @RequestMapping(value = "/select/currentForm", method = RequestMethod.GET)
    ResponseDto selectCurrentForm(@RequestParam("request") HttpServletRequest  request);



    /**
     * 查询：工单汇总数据 （处理中，已完结，满意度）
     * @return
     */
    @RequestMapping(value = "/select/dataCollection", method = RequestMethod.GET)
    ResponseDto selectDataCollection(@RequestParam("cycle") CycleType cycle);


    /**
     * 查询：成员结单柱状图
     * @return
     */
//    @RequestMapping(value = "/select/barGraphByGroup", method = RequestMethod.GET)
//    ResponseDto selectBarGraphByGroup();

    /**
     * 查询：类型比例饼状图
     * @return
     */
    @RequestMapping(value = "/select/pieChartByKind", method = RequestMethod.GET)
    ResponseDto selectPieChartByKind(CycleType cycle);

    //···················工单面板   结束·················//









    //````````````````````````````````````开始``````````````````````````````````//
    //··········消息中心····在MsgServiceClient类中······//
    //查询：消息 系统消息 工单消息 工单反馈
    //````````````````````````````````````结束``````````````````````````````````//


    //··········统计中心··········//
    //查询：工单统计报表
    //查询：工单分类统计
    //查询：满意度评价统计
    //查询：绩效考核（团队考核、个人考核）


    //````````````````````````````其他························//

    /**
     * 根据公司id查询公司负责人以及运维组
     * @param id
     * @return
     */
    @RequestMapping(value = "/select/groupAndClients", method = RequestMethod.GET)
    ResponseDto selectGroupAndClient(@RequestParam("request") HttpServletRequest  request,@RequestParam("id")Integer id);

    /**
     * 查询所有公司
     * @return
     */
    @RequestMapping(value = "/select/companyInfoList", method = RequestMethod.GET)
    ResponseDto selectCompanyInfoList();


    /**
     * 查询条件下拉框
     * @return
     */
    @RequestMapping(value = "/select/conditionInfoList", method = RequestMethod.GET)
    ResponseDto selectConditionInfoList(@RequestParam("request") HttpServletRequest  request);


    /**
     * 开始处理
     * @param request
     * @return
     */
    @RequestMapping(value = "/execute/takeOver", method = RequestMethod.GET)
    ResponseDto executeTakeOver(@RequestParam("request") HttpServletRequest  request,@RequestParam("formId")String formId);

    /**
     * 处理成功
     * @param request
     * @return
     */
    @RequestMapping(value = "/execute/dealWithEnd", method = RequestMethod.POST)
    ResponseDto executeDealWithEnd(@RequestParam("request") HttpServletRequest  request,@RequestBody FormOperateLogsDto formOperateLogsDto);

    /**
     * 流转升级
     * @param request
     * @return
     */
    @RequestMapping(value = "/execute/upgrade", method = RequestMethod.GET)
    ResponseDto executeUpgrade(@RequestParam("request") HttpServletRequest  request, @RequestParam("formId") String formId,@RequestParam("groupId")Integer groupId ,@RequestParam("userId")Integer userId,@RequestParam("reason")String reason);

    /**
     * 审核通过
     * @param request
     * @param formAuditDto
     * @return
     */
    @RequestMapping(value = "/execute/auditPass", method = RequestMethod.POST)
    ResponseDto executeAuditPass(@RequestParam("request") HttpServletRequest  request,@RequestBody FormAuditDto formAuditDto);

    /**
     * 审核驳回
     * @param request
     * @param formAuditDto
     * @return
     */
    @RequestMapping(value = "/execute/auditReject", method = RequestMethod.POST)
    ResponseDto executeAuditReject(@RequestParam("request") HttpServletRequest  request,@RequestBody FormAuditDto formAuditDto);

    /**
     * 评价
     * @param request
     * @param formEvaluateDto
     * @return
     */
    @RequestMapping(value = "/execute/evaluate", method = RequestMethod.POST)
    ResponseDto executeEvaluate(@RequestParam("request") HttpServletRequest  request, @RequestBody FormEvaluateDto formEvaluateDto);




    /**
     * 工单信息报表
     * @param request
     * @param response
     * @param formConditionDto
     */
    @RequestMapping(value = "formExport" ,method = RequestMethod.POST)
    void FormExport(@RequestParam("request")HttpServletRequest request, @RequestParam("response")HttpServletResponse response, @RequestBody FormConditionDto formConditionDto);

    /**
     * 工单新增页面下拉选项
     * @return
     */
    @RequestMapping(value = "select/formPullDown" ,method = RequestMethod.POST)
    ResponseDto selectFormPullDown();




    /**
     * 查询流转工程师列表
     * @param request
     * @return
     */
    @RequestMapping(value = "select/engineerList" ,method = RequestMethod.GET)
    ResponseDto selectEngineerList(@RequestParam("request")HttpServletRequest request);

    /**
     * 趋势图
     * @param cycle
     * @return
     */
    @RequestMapping(value = "select/formTrend" ,method = RequestMethod.GET)
    ResponseDto selectFormTrend(CycleType cycle);

    /**
     * 团队绩效
     * @param startTime
     * @param endTime
     * @param demandCode
     * @return
     */
    @RequestMapping(value = "select/groupMeirt" ,method = RequestMethod.GET)
    ResponseDto selectGroupMeirt(@RequestParam("request")HttpServletRequest request,@RequestParam("startTime")Long startTime,@RequestParam("endTime")Long endTime,@RequestParam("demandCode")Integer demandCode);


    /**
     * 个人绩效
     * @param request
     * @param meritConditionDto
     * @return
     */
    @RequestMapping(value = "select/personMeirt" ,method = RequestMethod.POST)
    ResponseDto selectPersonMeirt(@RequestParam("request")HttpServletRequest request,@RequestBody MeritConditionDto meritConditionDto);


}
