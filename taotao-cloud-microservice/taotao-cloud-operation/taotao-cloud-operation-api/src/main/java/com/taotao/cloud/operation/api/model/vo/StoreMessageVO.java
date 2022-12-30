package com.taotao.cloud.operation.api.model.vo;

import lombok.Data;

/**
 * 店铺消息表
 */
@Data
public class StoreMessageVO {
	/**
	 * 关联消息id
	 */
	private String messageId;

	/**
	 * 关联店铺id
	 */
	private String storeId;

	/**
	 * 关联店铺名称
	 */
	private String storeName;

	/**
	 * 状态 0默认未读 1已读 2回收站
	 *
	 * @see MessageStatusEnum
	 */
	private String status = MessageStatusEnum.UN_READY.name();

	/**
	 * 消息标题
	 */
	private String title;

	/**
	 * 消息内容
	 */
	private String content;
}
