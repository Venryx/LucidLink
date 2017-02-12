package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Advices {
	@SerializedName("Result")
	private List<Advice> advices;

	public List<Advice> getAdvices() {
		return this.advices;
	}
}