package com.iv.report.dto;


public class GroupByItemTypeDto implements Comparable<GroupByItemTypeDto>{
	private String itemType;
	private float mtta;
	private float mttr;
	private float count;
	private Float proportion;
	
	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public float getMtta() {
		return mtta;
	}

	public void setMtta(float mtta) {
		this.mtta = mtta;
	}

	public float getMttr() {
		return mttr;
	}

	public void setMttr(float mttr) {
		this.mttr = mttr;
	}

	public float getCount() {
		return count;
	}

	public void setCount(float count) {
		this.count = count;
	}

	public Float getProportion() {
		return proportion;
	}

	public void setProportion(Float proportion) {
		this.proportion = proportion;
	}

	@Override
	public int compareTo(GroupByItemTypeDto o) {
		// TODO Auto-generated method stub
		if(this.getCount() > o.getCount()){
			return 1;
		}else{
			return -1;
		}
	}
	
}
