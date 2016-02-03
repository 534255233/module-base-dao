package com.zlp.dao.enums;

public enum OperateEnum {
	
	ADD("添加", 1), UPDATE("更新", -1);
	
	private String des;
	
	private int value;
	
	private OperateEnum(String des, int value) {
		this.des = des;
		this.value = value;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	};
	
}
