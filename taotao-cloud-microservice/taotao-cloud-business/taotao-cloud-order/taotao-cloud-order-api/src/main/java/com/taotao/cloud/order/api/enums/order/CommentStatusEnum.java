package com.taotao.cloud.order.api.enums.order;

/**
 * 评论状态枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:21:51
 */
public enum CommentStatusEnum {

	/**
	 * 新订单，不能进行评论
	 */
	NEW("新订单，不能进行评论"),
	/**
	 * 未完成的评论
	 */
	UNFINISHED("未完成评论"),
	/**
	 * 待追评的评论信息
	 */
	WAIT_CHASE("待追评评论"),
	/**
	 * 已经完成评论
	 */
	FINISHED("已经完成评论");

	private final String description;

	CommentStatusEnum(String description) {
		this.description = description;
	}

	public String description() {
		return this.description;
	}

}
