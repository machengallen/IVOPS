package com.iv.script.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.iv.common.enumeration.OpsType;
@Entity
@Table(name="script_log")
public class ScriptLogEntity implements Comparable<ScriptLogEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3189606638738860047L;
	private String id;
	/*操作类型：创建、编辑*/
	private OpsType opsType;
	/*操作人*/
	private String opsUser;
	/*操作时间*/
	private long opsDate;
	
	@Id
	@GenericGenerator(name = "idGen", strategy = "uuid")
	@GeneratedValue(generator = "idGen")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public OpsType getOpsType() {
		return opsType;
	}
	public void setOpsType(OpsType opsType) {
		this.opsType = opsType;
	}
	public String getOpsUser() {
		return opsUser;
	}
	public void setOpsUser(String opsUser) {
		this.opsUser = opsUser;
	}
	public long getOpsDate() {
		return opsDate;
	}
	public void setOpsDate(long opsDate) {
		this.opsDate = opsDate;
	}
	
	@Override
	public int compareTo(ScriptLogEntity arg0) {
		// TODO Auto-generated method stub
		return new Date(this.getOpsDate()).compareTo(new Date(arg0.getOpsDate()));
	}

}
