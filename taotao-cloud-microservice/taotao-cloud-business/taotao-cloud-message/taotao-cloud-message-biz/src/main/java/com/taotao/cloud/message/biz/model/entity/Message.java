package com.taotao.cloud.message.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.message.api.enums.MessageSendClientEnum;
import com.taotao.cloud.message.api.enums.RangeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.JpaEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * 消息表
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Message.TABLE_NAME)
@TableName(Message.TABLE_NAME)
@EntityListeners({JpaEntityListener.class})
@org.hibernate.annotations.Table(appliesTo = Message.TABLE_NAME, comment = "消息表")
public class Message extends BaseSuperEntity<Message, Long> {

	public static final String TABLE_NAME = "tt_message";

	/**
	 * 标题
	 */
	@Column(name = "title", columnDefinition = "varchar(255) not null default '' comment '标题'")
	private String title;

	/**
	 * 字典名称
	 */
	@Column(name = "内容", columnDefinition = "varchar(255) not null default '' comment '内容'")
	private String content;

	/**
	 * 发送范围
	 *
	 * @see RangeEnum
	 */
	@Column(name = "message_range", columnDefinition = "varchar(255) not null default '' comment '发送范围'")
	private String messageRange;

	/**
	 * 发送客户端 商家或者会员
	 *
	 * @see MessageSendClientEnum
	 */
	@Column(name = "message_client", columnDefinition = "varchar(255) not null default '' comment '发送客户端 商家或者会员'")
	private String messageClient;

	/**
	 * 发送指定用户id
	 */
	@Column(name = "user_ids", columnDefinition = "varchar(255) not null default '' comment '发送指定用户id'")
	private String[] userIds;

	/**
	 * 发送指定用户名称
	 */
	@Column(name = "user_names", columnDefinition = "varchar(255) not null default '' comment '发送指定用户名称'")
	private String[] userNames;
}
