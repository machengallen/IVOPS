package com.iv.dto;

import java.io.Serializable;

/**
 * @author liangk
 * @create 2018年 05月 22日
 **/
public class FormCompanyDto implements Serializable {
    private static final long serialVersionUID = 5713485320612326787L;
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
