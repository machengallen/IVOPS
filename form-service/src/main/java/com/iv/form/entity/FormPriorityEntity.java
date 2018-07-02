package com.iv.form.entity;

import javax.persistence.*;

/**
 * @author liangk
 * @create 2018年 05月 24日
 **/
@Entity
@Table(name = "form_priority")
public class FormPriorityEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE,generator="tableGenerator")
    @TableGenerator(name="tableGenerator",initialValue=0,allocationSize=1)
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
