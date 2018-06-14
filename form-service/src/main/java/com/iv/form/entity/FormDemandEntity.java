package com.iv.form.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liangk
 * @create 2018年 05月 17日
 **/

@Entity
@Table(name = "form_demand")
public class FormDemandEntity {
    private static final long serialVersionUID = -3509286267377476242L;
    @Id
    @GeneratedValue
    private Integer id;

    /**组织名称*/
    @Column(length=64)
    private String label;

    /**备注*/
    @Column(length=64)
    private String content;

    /**父组织*/
    @ManyToOne
    @JoinColumn(name="parent_id")
    @JsonIgnore
    private FormDemandEntity parent;

    /**子组织*/
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="parent")
    private Set<FormDemandEntity> children = new HashSet<FormDemandEntity>();


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FormDemandEntity getParent() {
        return parent;
    }

    public void setParent(FormDemandEntity parent) {
        this.parent = parent;
    }

    public Set<FormDemandEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<FormDemandEntity> children) {
        this.children = children;
    }
}
