package com.iv.dto;

import java.util.List;
import java.util.Map;

/**
 * @author liangk
 * @create 2018年 05月 08日
 **/
public class FormAllMsgDto {
    private Map formInfo;
    private List<Map> files;
//    private Map formEvaluate;//评价
//    private List<Map> formAuditList;//审核
//    private List<Map> formChangeLogsList;//流转记录
//    private List<Map> formOperateLogsList;//操作记录
//    private List<Map> formUpgradeLogsList;//升级记录

    public Map getFormInfo() {
        return formInfo;
    }

    public void setFormInfo(Map formInfo) {
        this.formInfo = formInfo;
    }

    public List<Map> getFiles() {
        return files;
    }

    public void setFiles(List<Map> files) {
        this.files = files;
    }

    //    public Map getFormEvaluate() {
//        return formEvaluate;
//    }
//
//    public void setFormEvaluate(Map formEvaluate) {
//        this.formEvaluate = formEvaluate;
//    }
//
//    public List<Map> getFormAuditList() {
//        return formAuditList;
//    }
//
//    public void setFormAuditList(List<Map> formAuditList) {
//        this.formAuditList = formAuditList;
//    }
//
//    public List<Map> getFormChangeLogsList() {
//        return formChangeLogsList;
//    }
//
//    public void setFormChangeLogsList(List<Map> formChangeLogsList) {
//        this.formChangeLogsList = formChangeLogsList;
//    }
//
//    public List<Map> getFormOperateLogsList() {
//        return formOperateLogsList;
//    }
//
//    public void setFormOperateLogsList(List<Map> formOperateLogsList) {
//        this.formOperateLogsList = formOperateLogsList;
//    }
//
//    public List<Map> getFormUpgradeLogsList() {
//        return formUpgradeLogsList;
//    }
//
//    public void setFormUpgradeLogsList(List<Map> formUpgradeLogsList) {
//        this.formUpgradeLogsList = formUpgradeLogsList;
//    }


}
