package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class AdviceButton {
	@SerializedName("Colour")
	private String colour;
	@SerializedName("Icon")
	private String icon;
	@SerializedName("Id")
	private long id;
	@SerializedName("Order")
	private Integer order;
	@SerializedName("Title")
	private String title;

	public String getColour() {
		return this.colour;
	}

	public String getIcon() {
		return this.icon;
	}

	public long getId() {
		return this.id;
	}

	public Integer getOrder() {
		return this.order;
	}

	public String getTitle() {
		return this.title;
	}
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */