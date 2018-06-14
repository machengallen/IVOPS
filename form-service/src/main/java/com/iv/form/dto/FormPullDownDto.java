package com.iv.form.dto;

import com.iv.form.entity.FormDemandEntity;

import java.util.List;
import java.util.Map;

/**
 * @author liangk
 * @create 2018年 05月 24日
 **/
public class FormPullDownDto {
    List<Map> companyList;//公司
    List<FormDemandEntity> demandList;//需求
    List<Map> priorityList;//优先级


    public List<Map> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Map> companyList) {
        this.companyList = companyList;
    }

    public List<FormDemandEntity> getDemandList() {
        return demandList;
    }

    public void setDemandList(List<FormDemandEntity> demandList) {
        this.demandList = demandList;
    }

    public List<Map> getPriorityList() {
        return priorityList;
    }

    public void setPriorityList(List<Map> priorityList) {
        this.priorityList = priorityList;
    }
}
