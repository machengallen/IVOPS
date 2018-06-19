package com.iv.form.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author liangk
 * @create 2018年 06月 12日
 **/
@Entity
@Table(name = "form_file")
public class FormFileEntity implements Serializable {

    private static final long serialVersionUID = -5105790928379519797L;
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @JsonIgnore
    private String realName;
    private String url;
    @JsonIgnore
    private String path;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
