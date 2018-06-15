package com.iv.form.entity;

import javax.persistence.*;

/**
 * @author liangk
 * @create 2018年 06月 15日
 **/
@Entity
@Table(name = "form_audit_person")
public class FormAuditPersonEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private Integer userId;
    private Short groupId;
    private String formId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Short getGroupId() {
        return groupId;
    }

    public void setGroupId(Short groupId) {
        this.groupId = groupId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
