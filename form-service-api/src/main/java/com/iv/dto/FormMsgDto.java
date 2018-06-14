package com.iv.dto;



/**
 * @author liangk
 * 2018年5月7日
 * form-service-api-1.0.0-SNAPSHOT
 */
public class FormMsgDto {

    private String id;
    private Integer unitCode;//单位代码
    private Integer formOwnerId;//工单所有者id
    private String formOwnerPhone;//工单所有者电话
    private Integer demandTypeCode;//需求类型code
    private Integer groupId;//组Id
    private Integer handlerId ;//处理人Id
    private String relationFormId;//关联工单号码
    private Long formApplyTime;//工单申请时间
    private Long formExpectEndTime;//工单期望结束时间
    private String demandContent;//需求内容
    private int priority;//优先级

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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

    public String getDemandContent() {
        return demandContent;
    }

    public void setDemandContent(String demandContent) {
        this.demandContent = demandContent;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
