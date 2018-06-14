package com.iv.form.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author liangk
 * @create 2018年 05月 17日
 **/
@Entity
@Table(name = "form_audit")
public class FormAuditEntity {
    private String id;//主键
    private String formId;//工单Id
    private Integer grade;//评分
    private String content;//评价内容
    private Boolean isPass;//是否通过
    private Integer createBy;//创建人
    private Long createDate;//创建时间

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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Column(length=1000,name="content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Boolean getPass() {
        return isPass;
    }

    public void setPass(Boolean pass) {
        isPass = pass;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }
}
