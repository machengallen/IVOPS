package com.iv.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "group_info")
public class GroupEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 733085826085584253L;
	private short groupId;
	private String groupName;	
	private List<Integer> userIds;
	public GroupEntity() {
		super();
		// TODO Auto-generated constructor stub
	}			

	public GroupEntity(short groupId, String groupName, List<Integer> userIds) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.userIds = userIds;
	}

	@Id
	@GeneratedValue
	public short getGroupId() {
		return groupId;
	}
	public void setGroupId(short groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}		
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + groupId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		GroupEntity other = (GroupEntity) obj;
		if (groupId != other.groupId)
			return false;
		return true;
	}	
	
	
}
