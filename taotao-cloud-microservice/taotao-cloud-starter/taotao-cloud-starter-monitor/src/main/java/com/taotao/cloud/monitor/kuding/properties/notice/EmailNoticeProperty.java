package com.taotao.cloud.monitor.kuding.properties.notice;

import com.taotao.cloud.monitor.kuding.properties.enums.EmailTextType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = "prometheus.email")
public class EmailNoticeProperty {

	public static final String PREFIX = "taotao.cloud.monitor.notice.email";

	/**
	 * 是否开启邮件通知
	 */
	private boolean enabled = false;

	/**
	 * 收件人
	 */
	private String[] to;

	/**
	 * 抄送
	 */
	private String[] cc;

	/**
	 * 密抄送
	 */
	private String[] bcc;

	/**
	 * 邮件的通知文本类型
	 */
	private EmailTextType emailTextType = EmailTextType.TEXT;

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the to
	 */
	public String[] getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String[] to) {
		this.to = to;
	}

	/**
	 * @return the cc
	 */
	public String[] getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(String[] cc) {
		this.cc = cc;
	}

	/**
	 * @return the bcc
	 */
	public String[] getBcc() {
		return bcc;
	}

	/**
	 * @param bcc the bcc to set
	 */
	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

	/**
	 * @return the emailTextType
	 */
	public EmailTextType getEmailTextType() {
		return emailTextType;
	}

	/**
	 * @param emailTextType the emailTextType to set
	 */
	public void setEmailTextType(EmailTextType emailTextType) {
		this.emailTextType = emailTextType;
	}

}
