package com.iv.dto;

/**
 * 统计数据返回
 * @author liangk
 * @create 2018年 05月 11日
 **/
public class DataCollectionDto {

    private int processCount;
    private int endCount;
    private long satisfaction;

    public int getProcessCount() {
        return processCount;
    }

    public void setProcessCount(int processCount) {
        this.processCount = processCount;
    }

    public int getEndCount() {
        return endCount;
    }

    public void setEndCount(int endCount) {
        this.endCount = endCount;
    }

    public long getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(long satisfaction) {
        this.satisfaction = satisfaction;
    }
}
