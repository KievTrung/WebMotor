package com.Enums;

public enum IndexMode {
	NONE(0),
	CATEGORY(1),
	FEATURE(2),
	BOTH(4);
	
	private int mode; 
	
	private IndexMode(int mode) {
		this.setMode(mode);
	}
	
	public int getValue() {
		return mode;
	}
	
	private void setMode(int mode) {
		this.mode = mode;
	}
}
