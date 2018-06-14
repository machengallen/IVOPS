package com.iv.form.dto;

import com.iv.form.entity.FormDemandEntity;

import java.util.List;
import java.util.Map;

/**
 * @author liangk
 * @create 2018年 05月 17日
 **/
public class ConditionInfoDto {

    private List<Map> formStateDtos;//状态
    private List<Map> formApplicantDtos;//申请人
    private Object formHandlerDtos;//处理人
    private List<FormDemandEntity> formDemandDtos;//需求
    private List<Map> formCompanyDtos;//公司
    private List<Map> formPriorityDtos;//优先级

    public List<Map> getFormStateDtos() {
        return formStateDtos;
    }

    public void setFormStateDtos(List<Map> formStateDtos) {
        this.formStateDtos = formStateDtos;
    }

    public List<Map> getFormApplicantDtos() {
        return formApplicantDtos;
    }

    public void setFormApplicantDtos(List<Map> formApplicantDtos) {
        this.formApplicantDtos = formApplicantDtos;
    }

    public Object getFormHandlerDtos() {
        return formHandlerDtos;
    }

    public void setFormHandlerDtos(Object formHandlerDtos) {
        this.formHandlerDtos = formHandlerDtos;
    }

    public List<FormDemandEntity> getFormDemandDtos() {
        return formDemandDtos;
    }

    public void setFormDemandDtos(List<FormDemandEntity> formDemandDtos) {
        this.formDemandDtos = formDemandDtos;
    }

    public List<Map> getFormCompanyDtos() {
        return formCompanyDtos;
    }

    public void setFormCompanyDtos(List<Map> formCompanyDtos) {
        this.formCompanyDtos = formCompanyDtos;
    }

    public List<Map> getFormPriorityDtos() {
        return formPriorityDtos;
    }

    public void setFormPriorityDtos(List<Map> formPriorityDtos) {
        this.formPriorityDtos = formPriorityDtos;
    }
}
