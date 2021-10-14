package com.taotao.cloud.mail.biz.entity;

import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 邮件信息表
 *
 * @author shuigedeng
 * @since 2020/11/13 09:46
 * @version 1.0.0
 */
@Entity
@Table(name = "tt_email")
@org.hibernate.annotations.Table(appliesTo = "tt_email", comment = "邮件信息表")
public class Email extends JpaSuperEntity {

	private static final long serialVersionUID = 6887296988458221221L;

	/**
	 * 接收人邮箱(多个逗号分开)
	 */
	@Column(name = "receive_email", nullable = false, columnDefinition = "varchar(255) not null COMMENT '接收人邮箱(多个逗号分开)'")
	private String receiveEmail;

	/**
	 * 主题
	 */
	@Column(name = "subject", nullable = false, columnDefinition = "varchar(64) not null COMMENT '主题'")
	private String subject;

	/**
	 * 发送内容
	 */
	@Column(name = "content", nullable = false, columnDefinition = "varchar(2000) not null COMMENT '发送内容'")
	private String content;

	/**
	 * 模板
	 */
	@Column(name = "template", nullable = false, columnDefinition = "varchar(32) not null COMMENT '模板'")
	private String template;

	/**
	 * 发送时间
	 */
	@Column(name = "send_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '发送时间'")
	private LocalDateTime sendTime;

	public String getReceiveEmail() {
		return receiveEmail;
	}

	public void setReceiveEmail(String receiveEmail) {
		this.receiveEmail = receiveEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public LocalDateTime getSendTime() {
		return sendTime;
	}

	public void setSendTime(LocalDateTime sendTime) {
		this.sendTime = sendTime;
	}
}
