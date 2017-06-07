package com.resmed.refresh.bed;

public enum LedsState {
	GREEN("GREEN", 3, "grn"),
	GREENFLASH("GREENFLASH", 4, "grnFlash"),
	OFF("OFF", 0, "off"),
	RED("RED", 1, "red"),
	REDFLASH("REDFLASH", 2, "redFlash"),
	YELLOW("YELLOW", 5, "yel"),
	YELLOWFLASH("YELLOWFLASH", 6, "yelFlash");

	private String state;

	private LedsState(final String s, final int n, final String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return this.state;
	}
}