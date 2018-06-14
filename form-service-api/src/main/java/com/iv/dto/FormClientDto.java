package com.iv.dto;

/**
 * 客户
 * @author liangk
 * @create 2018年 05月 22日
 **/
public class FormClientDto {
    private Integer id;//主键
    private String name;//名称
    private String phone;//电话
    private String email;//邮箱
    private Integer companyId;//公司Id
    private Boolean isPrincipal;//是否负责人

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



    public Boolean getPrincipal() {
        return isPrincipal;
    }

    public void setPrincipal(Boolean principal) {
        isPrincipal = principal;
    }
}
