package com.iv.dto;

import java.util.List;
import java.util.Map;

/**
 * @author liangk
 * @create 2018年 05月 09日
 **/
public class CommonPage {
    int count;
    List<Map> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public List<Map> getList() {
        return list;
    }

    public void setList(List<Map> list) {
        this.list = list;
    }
}
