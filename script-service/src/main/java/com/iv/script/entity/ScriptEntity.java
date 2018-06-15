package com.iv.script.entity;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.iv.common.enumeration.ItemType;
import com.iv.common.enumeration.YesOrNo;

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
	private YesOrNo ifReviewed;//是否审核通过
	private String remark;//脚本说明
	private long quoteNUm;//引用量
	private long clickNum;//点击量
	private Set<ScriptLogEntity> scriptLog = new TreeSet<ScriptLogEntity>();//日志
	
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
	@OneToMany(cascade = {javax.persistence.CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderBy("opsDate")
	public Set<ScriptLogEntity> getScriptLog() {
		return scriptLog;
	}
	public void setScriptLog(Set<ScriptLogEntity> scriptLog) {
		this.scriptLog = scriptLog;
	}
	@Enumerated(value=EnumType.ORDINAL)
	public YesOrNo getIfReviewed() {
		return ifReviewed;
	}
	public void setIfReviewed(YesOrNo ifReviewed) {
		this.ifReviewed = ifReviewed;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getQuoteNUm() {
		return quoteNUm;
	}
	public void setQuoteNUm(long quoteNUm) {
		this.quoteNUm = quoteNUm;
	}
	public long getClickNum() {
		return clickNum;
	}
	public void setClickNum(long clickNum) {
		this.clickNum = clickNum;
	}
	
}
