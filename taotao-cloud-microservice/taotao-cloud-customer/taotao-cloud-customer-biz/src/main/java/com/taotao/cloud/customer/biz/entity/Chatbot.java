package com.taotao.cloud.customer.biz.entity;

import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 机器人客服表
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/13 09:46
 */
@Entity
@Table(name = "tt_chatbot")
@org.hibernate.annotations.Table(appliesTo = "tt_chatbot", comment = "机器人客服表")
public class Chatbot extends JpaSuperEntity {

	private static final long serialVersionUID = 6887296988458221221L;

	/**
	 * 机器人名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '机器人名称'")
	private String name;

	/**
	 * 基础url
	 */
	@Column(name = "base_url", nullable = false, columnDefinition = "varchar(255) not null comment '基础url'")
	private String baseUrl;

	/**
	 * 首选语言
	 */
	@Column(name = "primary_language", nullable = false, columnDefinition = "varchar(20) not null comment '首选语言'")
	private String primaryLanguage;

	/**
	 * 兜底回复
	 */
	@Column(name = "fallback", nullable = false, columnDefinition = "varchar(255) not null comment '兜底回复'")
	private String fallback;

	/**
	 * 欢迎语
	 */
	@Column(name = "welcome", nullable = false, columnDefinition = "varchar(255) not null comment '欢迎语'")
	private String welcome;

	/**
	 * 渠道类型
	 */
	@Column(name = "channel", nullable = false, columnDefinition = "varchar(32) not null comment '渠道类型'")
	private String channel;

	/**
	 * 渠道标识
	 */
	@Column(name = "channel_mark", nullable = false, columnDefinition = "varchar(255) not null comment '渠道标识'")
	private String channelMark;

	/**
	 * 是否开启 0-未开启，1-开启
	 */
	@Column(name = "enabled", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否开启 0-未开启 1-开启'")
	private boolean enabled;

	/**
	 * 工作模式
	 * @see WorkModeEnum
	 */
	@Column(name = "work_mode", nullable = false, columnDefinition = "int not null default 0 comment '工作模式'")
	private int workMode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public String getFallback() {
		return fallback;
	}

	public void setFallback(String fallback) {
		this.fallback = fallback;
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannelMark() {
		return channelMark;
	}

	public void setChannelMark(String channelMark) {
		this.channelMark = channelMark;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getWorkMode() {
		return workMode;
	}

	public void setWorkMode(int workMode) {
		this.workMode = workMode;
	}
}
