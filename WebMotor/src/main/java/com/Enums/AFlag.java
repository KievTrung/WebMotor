package com.Enums;

public enum AFlag {
	NOTHING(0),
	PASSWORD(1),
	PHONE(2), 
	EMAIL(4),
	ADDRESS(8),
	ISADMIN(16),
	ISACTIVE(32),
	ALL(255);
	
	private int value;
	
	private AFlag(int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	private void setValue(int value) {
		this.value = value;
	}
}
