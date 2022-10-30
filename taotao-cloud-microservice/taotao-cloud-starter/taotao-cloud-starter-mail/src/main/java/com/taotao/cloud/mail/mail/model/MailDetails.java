package com.taotao.cloud.mail.mail.model;


import java.io.File;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 17:06
 */
public class MailDetails {

	/**
	 * 发件人
	 */
	private String from;

	/**
	 * 收件人
	 */
	private String[] to;

	/**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 是否渲染html
	 */
	private Boolean showHtml;

	/**
	 * 邮件内容
	 */
	private String content;

	/**
	 * 抄送
	 */
	private String[] cc;

	/**
	 * 密送
	 */
	private String[] bcc;

	/**
	 * 附件
	 */
	private File[] files;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Boolean getShowHtml() {
		return showHtml;
	}

	public void setShowHtml(Boolean showHtml) {
		this.showHtml = showHtml;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}
}
