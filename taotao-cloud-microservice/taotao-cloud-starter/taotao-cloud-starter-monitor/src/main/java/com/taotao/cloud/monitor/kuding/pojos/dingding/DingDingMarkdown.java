package com.taotao.cloud.monitor.kuding.pojos.dingding;

public class DingDingMarkdown {

	private String title;

	private String text;

	public DingDingMarkdown(String title, String text) {
		this.title = title;
		this.text = text;
	}

	public DingDingMarkdown() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "DingDingMarkdown [title=" + title + ", text=" + text + "]";
	}

}
