package com.iv.dto;

/**
 * @author liangk
 * @create 2018年 05月 21日
 **/
public class FormExcelDto {

    private String id;//工单号
    private String applicantName;//申请人
    private String demandTypeCodeName;//需求类别
    private String demandContent;//需求内容
    private String handlerName;//处理人
    private String startTime;//开始时间
    private String endTime;//解决时间
    private String formState;//事件状态
    private String evaluateLevel;//客户评分

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getDemandTypeCodeName() {
        return demandTypeCodeName;
    }

    public void setDemandTypeCodeName(String demandTypeCodeName) {
        this.demandTypeCodeName = demandTypeCodeName;
    }

    public String getDemandContent() {
        return demandContent;
    }

    public void setDemandContent(String demandContent) {
        this.demandContent = demandContent;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFormState() {
        return formState;
    }

    public void setFormState(String formState) {
        this.formState = formState;
    }

    public String getEvaluateLevel() {
        return evaluateLevel;
    }

    public void setEvaluateLevel(String evaluateLevel) {
        this.evaluateLevel = evaluateLevel;
    }
}
