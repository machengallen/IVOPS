package com.iv.form.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author liangk
 * @create 2018年 05月 22日
 **/

@Entity
@Table(name = "form_company")
public class FormCompanyEntity implements Serializable {
    private static final long serialVersionUID = -6209619779291937936L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer createBy;//创建人
    private Long createDate;//创建时间
    private Integer updateBy;//更新
    private Long updateDate;//更新人

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

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }
}
