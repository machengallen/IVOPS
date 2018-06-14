package com.iv.dto;

import java.io.Serializable;

/**
 * @author liangk
 * @create 2018年 05月 09日
 **/
public class FormInfoDto implements Serializable {

    private String id;
    private Integer unitCode;//单位代码
    private String unitName;//单位代码
    private Integer formOwnerId;//工单所有者id
    private String formOwnerPhone;//工单所有者电话
    private Integer applicantId;//申请者Id
    private Integer demandTypeCode;//需求类型code
    private String demandTypeCodeName;//需求类型名称
    private Integer handlerId ;//处理人Id
    private String relationFormId;//关联工单号码
    private Long formApplyTime;//工单申请时间
    private Long formExpectEndTime;//工单期望结束时间
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
    private String formOwnerName;//表单拥有者姓名
    private String applicantName;//申请者姓名
    private String handlerName;//处理者姓名
    private String createName;//创建者姓名
    private String updateName;//更新者姓名
    private Boolean isMark;//是否被标记
    private Integer evaluateLevel;//评分

    public Integer getEvaluateLevel() {
        return evaluateLevel;
    }

    public void setEvaluateLevel(Integer evaluateLevel) {
        this.evaluateLevel = evaluateLevel;
    }

    public FormInfoDto() {
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getDemandTypeCodeName() {
        return demandTypeCodeName;
    }

    public void setDemandTypeCodeName(String demandTypeCodeName) {
        this.demandTypeCodeName = demandTypeCodeName;
    }

    public Boolean getMark() {
        return isMark;
    }

    public void setMark(Boolean mark) {
        isMark = mark;
    }

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

    public String getFormOwnerName() {
        return formOwnerName;
    }

    public void setFormOwnerName(String formOwnerName) {
        this.formOwnerName = formOwnerName;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getDemandContent() {
        return demandContent;
    }

    public void setDemandContent(String demandContent) {
        this.demandContent = demandContent;
    }
}
