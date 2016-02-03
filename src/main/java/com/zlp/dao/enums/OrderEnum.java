package com.zlp.dao.enums;

public enum OrderEnum {
	
	ASC("升序", 1), DESC("降序", -1);
	
	private String des;
	
	private int value;
	
	private OrderEnum(String des, int value) {
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
