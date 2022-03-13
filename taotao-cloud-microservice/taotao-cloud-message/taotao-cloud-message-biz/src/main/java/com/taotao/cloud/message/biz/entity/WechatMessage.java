package com.taotao.cloud.message.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;


/**
 * 微信消息表
 */
@Entity
@Table(name = WechatMessage.TABLE_NAME)
@TableName(WechatMessage.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = WechatMessage.TABLE_NAME, comment = "微信消息表")
@Data
public class WechatMessage extends BaseSuperEntity<WechatMessage, Long> {

	public static final String TABLE_NAME = "tt_wechat_message";

	/**
	 * 模版名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(255) not null default '' comment '模版名称'")
	private String name;

	/**
	 * 微信模版码
	 */
	@Column(name = "code", nullable = false, columnDefinition = "varchar(255) not null default '' comment '微信模版码'")
	private String code;

	/**
	 * 关键字
	 */
	@Column(name = "keywords", nullable = false, columnDefinition = "varchar(255) not null default '' comment '关键字'")
	private String keywords;

	/**
	 * 是否开启
	 */
	@Column(name = "enable", nullable = false, columnDefinition = "boolean not null default '' comment '是否开启'")
	private Boolean enable = true;

	/**
	 * 订单状态
	 */
	@Column(name = "order_status", nullable = false, columnDefinition = "varchar(255) not null default '' comment '订单状态'")
	private String orderStatus;

	/**
	 * 模版头部信息
	 */
	@Column(name = "first", nullable = false, columnDefinition = "varchar(255) not null default '' comment '模版头部信息'")
	private String first;

	/**
	 * 模版备注（位于最下方）
	 */
	@Column(name = "remark", nullable = false, columnDefinition = "varchar(255) not null default '' comment '模版备注（位于最下方）'")
	private String remark;


}
