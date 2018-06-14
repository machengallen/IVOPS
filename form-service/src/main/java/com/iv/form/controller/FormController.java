package com.iv.form.controller;


import com.iv.common.enumeration.CycleType;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.constant.BusException;
import com.iv.constant.ErrorMsg;
import com.iv.dto.*;
import com.iv.form.dto.ConditionInfoDto;
import com.iv.form.dto.FormPullDownDto;
import com.iv.form.entity.FormInfoEntity;
import com.iv.form.service.FormService;
import com.iv.service.IFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author liangk
 * 2018年5月7日
 * form-service-api-1.0.0-SNAPSHOT
 */
@RestController
@Api(description = "工单中心Api")
public class FormController implements IFormService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormController.class);

    @Autowired
    private FormService formService;



    @Override
    @ApiOperation(value="新增或修改工单",notes = "90101")
    public ResponseDto saveOrUpdateFormInfo(HttpServletRequest request, @RequestBody FormMsgDto formMsgDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();

        try {
            //保存工单
            FormInfoEntity formInfoEntity=formService.saveOrUpdateFormInfo( formMsgDto ,userId);
            dto.setData(formInfoEntity);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_INFO_SAVE_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_INFO_SAVE_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_INFO_SAVE_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="工单列表分页",notes = "90102")
    public ResponseDto selectFormListPage(HttpServletRequest request,@RequestBody FormConditionDto formConditionDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));

        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            FormInfoPage listPage = formService.selectFormListPage(request,formConditionDto,userId);
            dto.setData(listPage);
            dto.setErrorMsg(ErrorMsg.OK);
            return dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="工单信息",notes = "90103")
    public ResponseDto selectFormById(String formId) {

        ResponseDto dto = new ResponseDto();

        try {
            //查询工单信息
            FormAllMsgDto formAllMsgDto = formService.selectFormById(formId);
            dto.setData(formAllMsgDto);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_INFO_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_INFO_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="我的工单列表分页",notes = "90104")
    public ResponseDto selectMyFormListPage(HttpServletRequest request,@RequestBody FormConditionDto formConditionDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            FormInfoPage listPage = formService.selectMyFormListPage(request,userId,formConditionDto);
            dto.setData(listPage);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="工单回收站",notes = "90105")
    public ResponseDto selectDelFormListPage(HttpServletRequest request,@RequestBody FormConditionDto formConditionDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            FormInfoPage listPage = formService.selectDelFormListPage(request,userId,formConditionDto);
            dto.setData(listPage);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="标星工单列表",notes = "90106")
    public ResponseDto selectMarkFormListPage(HttpServletRequest request,@RequestBody FormConditionDto formConditionDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            FormInfoPage listPage = formService.selectMarkFormListPage(request,userId,formConditionDto);
            dto.setData(listPage);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="删除工单",notes = "90107")
    public ResponseDto delFormByLogic(HttpServletRequest request,String formId) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            formService.delFormByLogic(userId,formId);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_DEL_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DEL_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DEL_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="批量删除",notes = "90108")
    public ResponseDto delFormListByLogic(HttpServletRequest request, String[] formIds) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            formService.delFormListByLogic(userId,formIds);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_DEL_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DEL_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DEL_FAILED);
        }
        return dto;
    }

   /* @Override
    @ApiOperation("删除工单：彻底删除工单")
    public ResponseDto delFormByPhysics(HttpServletRequest request,String formId) {
        return null;
    }*/

    @Override
    @ApiOperation(value="标识工单为标星工单",notes = "90109")
    public ResponseDto saveMarkForm(HttpServletRequest request,String formId) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            formService.saveMarkForm(userId,formId);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_MARK_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_MARK_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_MARK_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="取消标星工单标识",notes = "90110")
    public ResponseDto delMarkForm(HttpServletRequest request, String formId) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            formService.delMarkForm(userId,formId);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_MARK_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_MARK_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_MARK_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="当前工单",notes = "90111")
    public ResponseDto selectCurrentForm(HttpServletRequest request) {
        return null;
    }

    @Override
    @ApiOperation(value="工单面板数据统计：处理中、已完结、满意度",notes = "90112")
    public ResponseDto selectDataCollection(CycleType cycle) {
        ResponseDto dto = new ResponseDto();
        try {
            Long timePoint = CycleType.getTimePoint(cycle);
            Map dataCollection = formService.selectDataCollection(timePoint);
            dto.setData(dataCollection);
            dto.setErrorMsg(ErrorMsg.OK);
            return dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }

//    @Override
//    @ApiOperation("成员结单柱状图")
//    public ResponseDto selectBarGraphByGroup() {
//        ResponseDto dto = new ResponseDto();
//        try {
//            //查询工单信息
//            List list = formService.selectBarGraphByGroup();
//            dto.setData(list);
//            dto.setErrorMsg(ErrorMsg.OK);
//            return dto;
//        } catch(Exception e) {
//            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
//            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
//        }
//        return dto;
//    }

    @Override
    @ApiOperation(value="类型比例饼状图",notes = "90113")
    public ResponseDto selectPieChartByKind(CycleType cycle) {
        ResponseDto dto = new ResponseDto();
        try {
            Long timePoint = CycleType.getTimePoint(cycle);

            List<Map> maps = formService.selectPieChartByKind(timePoint);
            dto.setData(maps);
            dto.setErrorMsg(ErrorMsg.OK);
            return dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="根据公司查询负责人以及团队信息",notes = "90114")
    public ResponseDto selectGroupAndClient(HttpServletRequest request,Integer id) {
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            FormGroupDto formGroupDto = formService.selectGroupAndClient(request,id);
            dto.setData(formGroupDto);
            dto.setErrorMsg(ErrorMsg.OK);
            return dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="查询公司列表",notes = "90115")
    public ResponseDto selectCompanyInfoList() {
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            List<Map> formCompanyDtos = formService.selectCompanyList();
            dto.setData(formCompanyDtos);
            dto.setErrorMsg(ErrorMsg.OK);
            return dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="查询条件下拉列表",notes = "90116")
    public ResponseDto selectConditionInfoList(HttpServletRequest request) {
        ResponseDto dto = new ResponseDto();
        try {
            //查询工单信息
            ConditionInfoDto conditionInfoDto = formService.selectConditionInfoList(request);
            dto.setData(conditionInfoDto);
            dto.setErrorMsg(ErrorMsg.OK);
            return dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }



    @Override
    @ApiOperation(value="工单开始处理",notes = "90117")
    public ResponseDto executeTakeOver(HttpServletRequest request, String formId) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();
        try {
            //保存工单
            formService.executeTakeOver( formId ,userId);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_INFO_SAVE_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_INFO_SAVE_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_INFO_SAVE_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="工单处理完成",notes = "90118")
    public ResponseDto executeDealWithEnd(HttpServletRequest request, @RequestBody FormOperateLogsDto formOperateLogsDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();

        try {
            //保存工单
            formService.executeDealWithEnd( formOperateLogsDto ,userId);

            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_SUBMIT_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="工单升级",notes = "90119")
    public ResponseDto executeUpgrade(HttpServletRequest request, String formId,Integer handlerId,String reason) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();

        try {
            //工单升级
            formService.executeUpgrade( userId ,formId,handlerId,reason);

            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_SUBMIT_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="工单审核通过",notes = "90120")
    public ResponseDto executeAuditPass(HttpServletRequest request,@RequestBody FormAuditDto formAuditDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();

        try {
            //审核通过
            formService.executeAuditPass( userId ,formAuditDto);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_SUBMIT_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="工单审核驳回",notes = "90121")
    public ResponseDto executeAuditReject(HttpServletRequest request, @RequestBody FormAuditDto formAuditDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();

        try {
            //审核通过
            formService.executeAuditReject( userId ,formAuditDto);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_SUBMIT_FAILED);
        }
        return dto;
    }

    @Override
    @ApiOperation(value="工单评价",notes = "90122")
    public ResponseDto executeEvaluate(HttpServletRequest request, @RequestBody FormEvaluateDto formEvaluateDto) {
        int userId = Integer.parseInt(JWTUtil.getJWtJson(request.getHeader("Authorization")).getString("userId"));
        ResponseDto dto = new ResponseDto();

        try {
            //审核通过
            formService.executeEvaluate( userId ,formEvaluateDto);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch (BusException be){//业务异常
            dto.setErrorMsg(be.getErrorMsg());
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),be);
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_SUBMIT_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_SUBMIT_FAILED);
        }
        return dto;
    }

    /**
     * 导出工单统计数据
     * @param request
     * @param response
     * @param formConditionDto
     */
    @Override
    @ApiOperation(value="导出工单报表",notes = "90123")
    public void FormExport(HttpServletRequest request, HttpServletResponse response, FormConditionDto formConditionDto) {
        formService.FormExport(request,response,formConditionDto);
    }


    /**
     * 查询工单需求页面下拉框信息
     * @return
     */
    @Override
    @ApiOperation(value="工单需求页面下拉框",notes = "90124")
    public ResponseDto selectFormPullDown() {
        ResponseDto dto = new ResponseDto();
        try {
            FormPullDownDto formPullDownDto=formService.selectFormPullDown();
            dto.setData(formPullDownDto);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }




    /**
     * 查询工程师列表
     * @param request
     * @return
     */
    @Override
    @ApiOperation(value="流转工程师列表",notes = "90126")
    public ResponseDto selectEngineerList(HttpServletRequest request) {
        ResponseDto dto = new ResponseDto();
        try {
            Object engineerList=formService.selectEngineerList(request);
            dto.setData(engineerList);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }


    /**
     * 工单趋势图
     * @param cycle
     * @return
     */
    @Override
    @ApiOperation(value="工单趋势图",notes = "90127")
    public ResponseDto selectFormTrend(CycleType cycle) {
        ResponseDto dto = new ResponseDto();
        try {
            Map formTrend = formService.getFormTrend(cycle);
            dto.setData(formTrend);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }


    /**
     * 团队绩效
     * @param startTime
     * @param endTime
     * @param demandCode
     * @return
     */
    @Override
    @ApiOperation(value="团队绩效",notes = "90128")
    public ResponseDto selectGroupMeirt(HttpServletRequest request,Long startTime,Long endTime,Integer demandCode){
        ResponseDto dto = new ResponseDto();
        try {
            Map groupMeirt = formService.selectGroupMeirt(request,startTime,endTime,demandCode);
            dto.setData(groupMeirt);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }


    /**
     * 团队绩效
     * @param request
     * @param meritConditionDto
     * @return
     */
    @Override
    @ApiOperation(value="个人绩效",notes = "90129")
    public ResponseDto selectPersonMeirt(HttpServletRequest request,@RequestBody MeritConditionDto meritConditionDto){
        ResponseDto dto = new ResponseDto();
        try {
            CommonPage personMeirt = formService.selectPersonMeirt(meritConditionDto);
            dto.setData(personMeirt);
            dto.setErrorMsg(ErrorMsg.OK);
            return  dto;
        } catch(Exception e) {
            LOGGER.info(ErrorMsg.FORM_DATA_FAILED.toString(),e);
            dto.setErrorMsg(ErrorMsg.FORM_DATA_FAILED);
        }
        return dto;
    }
}
