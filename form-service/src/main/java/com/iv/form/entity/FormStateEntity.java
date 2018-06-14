package com.iv.form.entity;

import javax.persistence.*;

/**
 * @author liangk
 * @create 2018年 05月 23日
 **/
@Entity
@Table(name = "form_state")
public class FormStateEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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
