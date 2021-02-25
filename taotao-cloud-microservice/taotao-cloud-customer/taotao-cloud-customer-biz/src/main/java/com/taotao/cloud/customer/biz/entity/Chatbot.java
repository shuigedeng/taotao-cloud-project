package com.taotao.cloud.customer.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 机器人客服表
 *
 * @author dengtao
 * @date 2020/11/13 09:46
 * @since v1.0
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_chatbot")
@org.hibernate.annotations.Table(appliesTo = "tt_chatbot", comment = "机器人客服表")
public class Chatbot extends BaseEntity {

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
	@Builder.Default
	@Column(name = "enabled", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否开启 0-未开启 1-开启'")
	private Boolean enabled = false;

	/**
	 * 工作模式
	 *
	 * @see WorkModeEnum
	 */
	@Column(name = "work_mode", nullable = false, columnDefinition = "int not null default 0 comment '工作模式'")
	@Builder.Default
	private Integer workMode = 0;

}
