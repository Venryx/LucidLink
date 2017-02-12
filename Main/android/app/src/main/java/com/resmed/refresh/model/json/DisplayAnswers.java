package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class DisplayAnswers {
	@SerializedName("Id")
	private int id;
	@SerializedName("Text")
	private String text;

	public int getId() {
		return this.id;
	}

	public String getText() {
		return this.text;
	}
}