package com.taotao.cloud.message.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.message.api.enums.MessageSendClient;
import com.taotao.cloud.message.api.enums.RangeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;


/**
 * 消息表
 */
@Entity
@Table(name = Message.TABLE_NAME)
@TableName(Message.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Message.TABLE_NAME, comment = "消息表")
@Data
public class Message extends BaseSuperEntity<Message, Long> {

	public static final String TABLE_NAME = "tt_message";

	/**
	 * 标题
	 */
	@Column(name = "title", nullable = false, columnDefinition = "varchar(255) not null default '' comment '标题'")
	private String title;

	/**
	 * 字典名称
	 */
	@Column(name = "内容", nullable = false, columnDefinition = "varchar(255) not null default '' comment '内容'")
	private String content;

	/**
	 * 发送范围
	 *
	 * @see RangeEnum
	 */
	@Column(name = "message_range", nullable = false, columnDefinition = "varchar(255) not null default '' comment '发送范围'")
	private String messageRange;

	/**
	 * 发送客户端 商家或者会员
	 *
	 * @see MessageSendClient
	 */
	@Column(name = "message_client", nullable = false, columnDefinition = "varchar(255) not null default '' comment '发送客户端 商家或者会员'")
	private String messageClient;

	/**
	 * 发送指定用户id
	 */
	@Column(name = "user_ids", nullable = false, columnDefinition = "varchar(255) not null default '' comment '发送指定用户id'")
	private String[] userIds;

	/**
	 * 发送指定用户名称
	 */
	@Column(name = "user_names", nullable = false, columnDefinition = "varchar(255) not null default '' comment '发送指定用户名称'")
	private String[] userNames;
}
