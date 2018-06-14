package com.iv.dto;



/**
 * 工单升级记录
 * @author liangk
 * @create 2018年 05月 09日
 **/

public class FormUpgradeLogsDto {
    private String id;//主键
    private String formId;//表单Id
    private Integer createBy;//创建人
    private Long createDate;//创建时间
    private String upgradeReason;//升级记录


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


    public String getUpgradeReason() {
        return upgradeReason;
    }

    public void setUpgradeReason(String upgradeReason) {
        this.upgradeReason = upgradeReason;
    }
}
