package com.iv.script.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.iv.common.enumeration.ItemType;

@Entity
@Table(name = "script_library")
public class ScriptEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1601506321533017547L;
	private int id;
	private String name;// 文件名，自动生成
	private String type;// 文本类型
	private String alias;// 用户自定义别名
	private ItemType itemType;// 业务类型
	private long creDate;
	private AuthorEntity creator;
	private long modDate;
	private AuthorEntity modifier;
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(unique = true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getCreDate() {
		return creDate;
	}
	public void setCreDate(long creDate) {
		this.creDate = creDate;
	}
	public long getModDate() {
		return modDate;
	}
	public void setModDate(long modDate) {
		this.modDate = modDate;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public ItemType getItemType() {
		return itemType;
	}
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	public AuthorEntity getCreator() {
		return creator;
	}
	public void setCreator(AuthorEntity creator) {
		this.creator = creator;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	public AuthorEntity getModifier() {
		return modifier;
	}
	public void setModifier(AuthorEntity modifier) {
		this.modifier = modifier;
	}
	
}
