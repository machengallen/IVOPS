package com.iv.form.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 工单操作记录实体
 * @author liangk
 * @create 2018年 05月 09日
 **/
@Entity
@Table(name = "form_operate_logs")
public class FormOperateLogsEntity implements Serializable {


    private String id ; //主键
    private String formId;//工单主键
    private Integer handlerId;//处理人id,
    private Integer demandTypeCode;//操作类别,
    //private Integer secondDemandTypeCode;//二级操作类别,D
    private Integer workload ;//工作量',
    private String operateDetails;// varchar(2000) COMMENT //'操作描述',
    private Long startTime;  //开始时间,
    private Long endTime; //结束时间,
    private Long createDate;//创建时间,

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Integer getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Integer handlerId) {
        this.handlerId = handlerId;
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
    @Column(length=1000,name="operate_details")
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

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }
}
