package com.iv.dto;


/**
 * 工单操作记录实体
 * @author liangk
 * @create 2018年 05月 09日
 **/

public class FormOperateLogsDto {


    //private String id ; //主键
    private String formId;//工单主键
    //private int handlerId;//处理人id,
    //private int handlerName;//处理人姓名,
    private Integer demandTypeCode;//操作类别,
    //private Integer secondDemandTypeCode;//二级操作类别,D
    private Integer workload ;//工作量',
    private String operateDetails;// varchar(2000) COMMENT //'操作描述',
    private Long startTime;  //开始时间,
    private Long endTime; //结束时间,
    //private Long createDate;//创建时间,


    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Integer getDemandTypeCode() {
        return demandTypeCode;
    }

    public void setDemandTypeCode(Integer demandTypeCode) {
        this.demandTypeCode = demandTypeCode;
    }



    public Integer getWorkload() {
        return workload;
    }

    public void setWorkload(Integer workload) {
        this.workload = workload;
    }

    public String getOperateDetails() {
        return operateDetails;
    }

    public void setOperateDetails(String operateDetails) {
        this.operateDetails = operateDetails;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
