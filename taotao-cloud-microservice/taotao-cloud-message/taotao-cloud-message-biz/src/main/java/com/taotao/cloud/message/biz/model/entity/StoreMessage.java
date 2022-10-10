package com.taotao.cloud.message.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.message.api.enums.MessageStatusEnum;
import com.taotao.cloud.web.base.entity.AbstractListener;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

/**
 * 店铺消息表
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = StoreMessage.TABLE_NAME)
@TableName(StoreMessage.TABLE_NAME)
@EntityListeners({AbstractListener.class})
@org.hibernate.annotations.Table(appliesTo = StoreMessage.TABLE_NAME, comment = "店铺消息表")
public class StoreMessage extends BaseSuperEntity<StoreMessage, Long> {

	public static final String TABLE_NAME = "tt_store_message";

	private static final long serialVersionUID = 1L;
	/**
	 * 关联消息id
	 */
	@Column(name = "message_id", columnDefinition = "varchar(255) not null default '' comment '关联消息id'")
	private String messageId;

	/**
	 * 关联店铺id
	 */
	@Column(name = "store_id", columnDefinition = "varchar(255) not null default '' comment '关联店铺id'")
	private String storeId;

	/**
	 * 关联店铺名称
	 */
	@Column(name = "store_name", columnDefinition = "varchar(255) not null default '' comment '关联店铺名称'")
	private String storeName;

	/**
	 * 状态 0默认未读 1已读 2回收站
	 *
	 * @see MessageStatusEnum
	 */
	@Column(name = "status", columnDefinition = "varchar(255) not null default '' comment '状态 0默认未读 1已读 2回收站'")
	private String status = MessageStatusEnum.UN_READY.name();

	/**
	 * 消息标题
	 */
	@Column(name = "title", columnDefinition = "varchar(255) not null default '' comment '消息标题'")
	private String title;

	/**
	 * 消息内容
	 */
	@Column(name = "content", columnDefinition = "varchar(255) not null default '' comment '消息内容'")
	private String content;
}
