package com.taotao.cloud.message.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.message.api.enums.MessageStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * 店铺消息表
 */
@Data
@Entity
@Table(name = StoreMessage.TABLE_NAME)
@TableName(StoreMessage.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = StoreMessage.TABLE_NAME, comment = "店铺消息表")
public class StoreMessage extends BaseSuperEntity<StoreMessage, Long> {

	public static final String TABLE_NAME = "tt_store_message";

	private static final long serialVersionUID = 1L;
	/**
	 * 关联消息id
	 */
	@Column(name = "message_id", nullable = false, columnDefinition = "varchar(255) not null default '' comment '关联消息id'")
	private String messageId;

	/**
	 * 关联店铺id
	 */
	@Column(name = "store_id", nullable = false, columnDefinition = "varchar(255) not null default '' comment '关联店铺id'")
	private String storeId;

	/**
	 * 关联店铺名称
	 */
	@Column(name = "store_name", nullable = false, columnDefinition = "varchar(255) not null default '' comment '关联店铺名称'")
	private String storeName;

	/**
	 * 状态 0默认未读 1已读 2回收站
	 *
	 * @see MessageStatusEnum
	 */
	@Column(name = "status", nullable = false, columnDefinition = "varchar(255) not null default '' comment '状态 0默认未读 1已读 2回收站'")
	private String status = MessageStatusEnum.UN_READY.name();

	/**
	 * 消息标题
	 */
	@Column(name = "title", nullable = false, columnDefinition = "varchar(255) not null default '' comment '消息标题'")
	private String title;

	/**
	 * 消息内容
	 */
	@Column(name = "content", nullable = false, columnDefinition = "varchar(255) not null default '' comment '消息内容'")
	private String content;
}
