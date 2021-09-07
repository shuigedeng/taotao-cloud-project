package com.taotao.cloud.prometheus.model;


import com.taotao.cloud.prometheus.enums.DingdingTextType;

public class DingDingMarkdownNotice extends DingDingNotice {

	private DingDingMarkdown markdown;

	/**
	 * @param at
	 * @param msgtype
	 */
	public DingDingMarkdownNotice(String msg, String title, String[] phones) {
		super(new DingDingAt(phones), DingdingTextType.MARKDOWN.getMsgType());
		markdown = new DingDingMarkdown(title, msg);
	}

	/**
	 * @return the markdown
	 */
	public DingDingMarkdown getMarkdown() {
		return markdown;
	}

	/**
	 * @param markdown the markdown to set
	 */
	public void setMarkdown(DingDingMarkdown markdown) {
		this.markdown = markdown;
	}

	@Override
	public String toString() {
		return "DingDingMarkdownNotice [markdown=" + markdown + ", at=" + at + ", msgtype=" + msgtype + "]";
	}

}
