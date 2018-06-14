package com.iv.dto;

import java.util.List;
import java.util.Map;

/**
 * @author liangk
 * @create 2018年 05月 09日
 **/
public class FormInfoPage {
    int count;
    List<Map> formInfoList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public List<Map> getFormInfoList() {
        return formInfoList;
    }

    public void setFormInfoList(List<Map> formInfoList) {
        this.formInfoList = formInfoList;
    }
}
