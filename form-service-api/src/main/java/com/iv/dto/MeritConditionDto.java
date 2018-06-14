package com.iv.dto;

/**
 * @author liangk
 * @create 2018年 06月 01日
 **/
public class MeritConditionDto {

    private Integer groupId;
    private Long startTime;
    private Long endTime;
    private Integer demandCode;
    private Integer curPage;//当前页
    private Integer items;//每页显示条数

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getDemandCode() {
        return demandCode;
    }

    public void setDemandCode(Integer demandCode) {
        this.demandCode = demandCode;
    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getItems() {
        return items;
    }

    public void setItems(Integer items) {
        this.items = items;
    }
}
