package com.iv.dto;


/**
 * @author liangk
 * @create 2018年 05月 17日
 **/

public class FormAuditDto {
//    private String id;//主键
    private String formId;//工单Id
    private Integer grade;//评分
    private String content;//评价内容
//    private AuditType auditType;//审核类型
//    private Integer createBy;//创建人


//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public AuditType getAuditType() {
//        return auditType;
//    }
//
//    public void setAuditType(AuditType auditType) {
//        this.auditType = auditType;
//    }

}
