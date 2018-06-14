package com.iv.form.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author liangk
 * @create 2018年 06月 12日
 **/
@Entity
@Table(name = "form_file")
public class FormFileEntity {

    @Id
    @GeneratedValue
    private Integer id;
    //private Integer formId;
    private String filename;
    private String path;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
