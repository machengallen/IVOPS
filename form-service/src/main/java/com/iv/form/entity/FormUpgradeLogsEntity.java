package com.iv.form.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 工单升级记录
 * @author liangk
 * @create 2018年 05月 09日
 **/
@Entity
@Table(name = "form_upgrade_logs")
public class FormUpgradeLogsEntity implements Serializable {
    private String id;//主键
    private String formId;//表单Id
    private Integer createBy;//创建人
    private Long createDate;//创建时间
    private String upgradeReason;//升级记录
    private Integer handlerId;//接手工程师

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

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    @Column(length=1000,name="upgrade_reason")
    public String getUpgradeReason() {
        return upgradeReason;
    }

    public void setUpgradeReason(String upgradeReason) {
        this.upgradeReason = upgradeReason;
    }

    public Integer getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Integer handlerId) {
        this.handlerId = handlerId;
    }
}
