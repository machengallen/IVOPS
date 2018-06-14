package com.iv.dto;


import java.io.Serializable;

/**
 * @author liangk
 * @create 2018年 05月 09日
 **/

public class FormEvaluateDto implements Serializable {



    //private String id;//主键
    private String formId;//工单主键
    private Integer evaluateLevel;//评价等级
    private String clientOpinion;//客户意见
    //private Long endTime;//结束时间
    //private Integer operateType; //操作类型 : 0：机器默认评分  1：客户评分



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

    public String getClientOpinion() {
        return clientOpinion;
    }

    public void setClientOpinion(String clientOpinion) {
        this.clientOpinion = clientOpinion;
    }


}
