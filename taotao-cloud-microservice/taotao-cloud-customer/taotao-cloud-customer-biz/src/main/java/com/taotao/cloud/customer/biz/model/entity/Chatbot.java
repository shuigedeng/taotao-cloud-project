package com.taotao.cloud.customer.biz.model.entity;

import com.taotao.cloud.data.jpa.base.entity.JpaSuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 机器人客服表
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/13 09:46
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tt_chatbot")
@org.hibernate.annotations.Table(appliesTo = "tt_chatbot", comment = "机器人客服表")
public class Chatbot extends JpaSuperEntity {

	private static final long serialVersionUID = 6887296988458221221L;

	/**
	 * 机器人名称
	 */
	@Column(name = "name", columnDefinition = "varchar(32) not null comment '机器人名称'")
	private String name;

	/**
	 * 基础url
	 */
	@Column(name = "base_url", columnDefinition = "varchar(255) not null comment '基础url'")
	private String baseUrl;

	/**
	 * 首选语言
	 */
	@Column(name = "primary_language", columnDefinition = "varchar(20) not null comment '首选语言'")
	private String primaryLanguage;

	/**
	 * 兜底回复
	 */
	@Column(name = "fallback", columnDefinition = "varchar(255) not null comment '兜底回复'")
	private String fallback;

	/**
	 * 欢迎语
	 */
	@Column(name = "welcome", columnDefinition = "varchar(255) not null comment '欢迎语'")
	private String welcome;

	/**
	 * 渠道类型
	 */
	@Column(name = "channel", columnDefinition = "varchar(32) not null comment '渠道类型'")
	private String channel;

	/**
	 * 渠道标识
	 */
	@Column(name = "channel_mark", columnDefinition = "varchar(255) not null comment '渠道标识'")
	private String channelMark;

	/**
	 * 是否开启 0-未开启，1-开启
	 */
	@Column(name = "enabled", columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否开启 0-未开启 1-开启'")
	private boolean enabled;

	/**
	 * 工作模式
	 *
	 * @see WorkModeEnum
	 */
	@Column(name = "work_mode", columnDefinition = "int not null default 0 comment '工作模式'")
	private int workMode;
}
