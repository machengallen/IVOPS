package com.iv.script.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "temporary_script_info")
public class TemporaryScriptEntity implements Serializable {

	/**
	 * 临时文件信息实体类
	 */
	private static final long serialVersionUID = -2684894460223631400L;
	private int id;
	private String name;// 文件名，自动生成
	private String type;// 文本类型
	private String alias;// 用户自定义别名
	private long creDate;//创建时间
	public TemporaryScriptEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TemporaryScriptEntity(int id, String name, String type, String alias, long creDate) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.alias = alias;
		this.creDate = creDate;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public long getCreDate() {
		return creDate;
	}
	public void setCreDate(long creDate) {
		this.creDate = creDate;
	}
	
}
