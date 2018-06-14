package com.iv.dto;

import java.util.List;
import java.util.Map;

/**
 * @author liangk
 * @create 2018年 05月 16日
 **/
public class FormGroupDto {
    private List<Map> clients;
    private Object formGroupMemList;


    public List<Map> getClients() {
        return clients;
    }

    public void setClients(List<Map> clients) {
        this.clients = clients;
    }

    public Object getFormGroupMemList() {
        return formGroupMemList;
    }

    public void setFormGroupMemList(Object formGroupMemList) {
        this.formGroupMemList = formGroupMemList;
    }
}
