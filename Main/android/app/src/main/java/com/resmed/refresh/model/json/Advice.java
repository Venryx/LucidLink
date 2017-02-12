package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Advice {
	@SerializedName("ArticleUrl")
	private String articleUrl;
	@SerializedName("Buttons")
	private List<AdviceButton> buttons;
	@SerializedName("Category")
	private int category;
	@SerializedName("Content")
	private String content = "";
	@SerializedName("DateGenerated")
	private String dateTime = "";
	@SerializedName("Details")
	private String details = "";
	@SerializedName("HtmlContent")
	private String htmlContent = "";
	@SerializedName("HypnogramInfo")
	private HypnogramInfo hypnogramInfo = new HypnogramInfo();
	@SerializedName("HypnogramRelated")
	private int hypnogramRelated;
	@SerializedName("Icon")
	private String icon = "";
	@SerializedName("Id")
	private long id;
	@SerializedName("IsHtml")
	private int isHtml;
	@SerializedName("OrderId")
	private int orderId;
	@SerializedName("Orderid")
	private int orderid;
	@SerializedName("RecordId")
	private long recordId;
	@SerializedName("Subtitle")
	private String subtitle;
	@SerializedName("Title")
	private String title = "";
	@SerializedName("Type")
	private String type = "";

	public void check() {
		if (this.type == null) {
			this.type = "";
		}
		if (this.title == null) {
			this.title = "";
		}
		if (this.content == null) {
			this.content = "";
		}
		if (this.details == null) {
			this.details = "";
		}
		if (this.htmlContent == null) {
			this.htmlContent = "";
		}
		if (this.dateTime == null) {
			this.dateTime = "";
		}
		if (this.icon == null) {
			this.icon = "";
		}
		if (this.hypnogramInfo == null) {
			this.hypnogramInfo = new HypnogramInfo();
		}
		if (this.articleUrl == null) {
			this.articleUrl = "";
		}
		if (this.subtitle == null) {
			this.subtitle = "";
		}
	}

	public String getArticleUrl() {
		return this.articleUrl;
	}

	public List<AdviceButton> getButtons() {
		return this.buttons;
	}

	public int getCategory() {
		return this.category;
	}

	public String getContent() {
		return this.content;
	}

	public String getDateTime() {
		return this.dateTime;
	}

	public String getDetails() {
		return this.details;
	}

	public String getHtmlContent() {
		return this.htmlContent;
	}

	public HypnogramInfo getHypnogramInfo() {
		return this.hypnogramInfo;
	}

	public String getIcon() {
		return this.icon;
	}

	public long getId() {
		return this.id;
	}

	public Integer getOrderId() {
		return Integer.valueOf(this.orderId);
	}

	public int getOrderid() {
		return this.orderid;
	}

	public long getRecordId() {
		return this.recordId;
	}

	public String getSubtitle() {
		return this.subtitle;
	}

	public String getTitle() {
		return this.title;
	}

	public String getType() {
		return this.type;
	}

	public boolean isHtml() {
		return this.isHtml == 1;
	}

	public boolean isHypnogramRelated() {
		return this.hypnogramRelated == 1;
	}
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */