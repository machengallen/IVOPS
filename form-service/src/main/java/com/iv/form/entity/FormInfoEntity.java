package com.iv.form.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liangk
 * 2018年5月7日
 * form-service-api-1.0.0-SNAPSHOT
 */
@Entity
@Table(name = "form_info")
public class FormInfoEntity implements Serializable {

    private static final long serialVersionUID = -5956249874638701781L;
    @Id
    @GenericGenerator(name = "FormId", strategy = "com.iv.form.util.FormGenerator")
    @GeneratedValue(generator = "FormId")
    private String id;
    private Integer unitCode;//单位代码
    private Integer formOwnerId;//工单所有者id
    private String formOwnerPhone;//工单所有者电话
    private Integer applicantId;//申请者Id
    private Integer demandTypeCode;//需求类型code
    private Integer groupId;//组Id
    private Integer handlerId ;//处理人Id
    private String relationFormId;//关联工单号码
    private Long formApplyTime;//工单申请时间
    private Long formExpectEndTime;//工单期望结束时间
    @Column(length=1000,name="demand_content")
    private String demandContent;//需求内容
    private Long formRealEndTime;//工单真正结束时间
    private Integer priority;//优先级
    private Integer formState ;//工单状态
    private Integer sourceType;//来源类型
    private Integer createBy;//创建者
    private Long createDate;//创建时间
    private Integer updateBy ;//更新者
    private Long updateDate;//更新时间
    private Byte  delFlag;//删除标记

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "r_form_file", joinColumns = { @JoinColumn(name = "form_id") }, inverseJoinColumns = {@JoinColumn(name = "file_id") })
    private Set<FormFileEntity> files = new HashSet<FormFileEntity>();



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(Integer unitCode) {
        this.unitCode = unitCode;
    }

    public Integer getFormOwnerId() {
        return formOwnerId;
    }

    public void setFormOwnerId(Integer formOwnerId) {
        this.formOwnerId = formOwnerId;
    }

    public String getFormOwnerPhone() {
        return formOwnerPhone;
    }

    public void setFormOwnerPhone(String formOwnerPhone) {
        this.formOwnerPhone = formOwnerPhone;
    }

    public Integer getApplicantId() {
        return applicantId;
    }


    public String getDemandContent() {
        return demandContent;
    }

    public void setDemandContent(String demandContent) {
        this.demandContent = demandContent;
    }

    public void setApplicantId(Integer applicantId) {
        this.applicantId = applicantId;
    }

    public Integer getDemandTypeCode() {
        return demandTypeCode;
    }

    public void setDemandTypeCode(Integer demandTypeCode) {
        this.demandTypeCode = demandTypeCode;
    }

    public Integer getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Integer handlerId) {
        this.handlerId = handlerId;
    }

    public String getRelationFormId() {
        return relationFormId;
    }

    public void setRelationFormId(String relationFormId) {
        this.relationFormId = relationFormId;
    }

    public Long getFormApplyTime() {
        return formApplyTime;
    }

    public void setFormApplyTime(Long formApplyTime) {
        this.formApplyTime = formApplyTime;
    }

    public Long getFormExpectEndTime() {
        return formExpectEndTime;
    }

    public void setFormExpectEndTime(Long formExpectEndTime) {
        this.formExpectEndTime = formExpectEndTime;
    }

    public Long getFormRealEndTime() {
        return formRealEndTime;
    }

    public void setFormRealEndTime(Long formRealEndTime) {
        this.formRealEndTime = formRealEndTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getFormState() {
        return formState;
    }

    public void setFormState(Integer formState) {
        this.formState = formState;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
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

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Byte getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Set<FormFileEntity> getFiles() {
        return files;
    }

    public void setFiles(Set<FormFileEntity> files) {
        this.files = files;
    }
}
