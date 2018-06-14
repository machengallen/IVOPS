package com.iv.form.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author liangk
 * @create 2018年 05月 09日
 **/
@Entity
@Table(name = "form_evaluate")
public class FormEvaluateEntity implements Serializable {

    private static final long serialVersionUID = 7501032016798011562L;
    private String id;//主键
    private String formId;//工单主键
    private Integer evaluateLevel;//评价等级
    private String clientOpinion;//客户意见
    private Long endTime;//结束时间
    private Integer operateType; //操作类型 : 0：机器默认评分  1：客户评分


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

    public Integer getEvaluateLevel() {
        return evaluateLevel;
    }

    public void setEvaluateLevel(Integer evaluateLevel) {
        this.evaluateLevel = evaluateLevel;
    }

    @Column(length=1000,name="client_opinion")
    public String getClientOpinion() {
        return clientOpinion;
    }

    public void setClientOpinion(String clientOpinion) {
        this.clientOpinion = clientOpinion;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }


    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }
}
