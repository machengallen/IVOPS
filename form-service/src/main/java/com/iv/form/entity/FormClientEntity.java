package com.iv.form.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author liangk
 * @create 2018年 05月 22日
 **/
@Entity
@Table(name = "form_client")
public class FormClientEntity implements Serializable {


    private static final long serialVersionUID = 3818034484260331350L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;//主键
    private String name;//名称
    private String phone;//电话
    private String email;//邮箱
    private Integer companyId;//公司Id
    private Boolean isPrincipal;//是否负责人
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public Boolean getPrincipal() {
        return isPrincipal;
    }

    public void setPrincipal(Boolean principal) {
        isPrincipal = principal;
    }
}
