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
 * 会员消息表
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = MemberMessage.TABLE_NAME)
@TableName(MemberMessage.TABLE_NAME)
@EntityListeners({AbstractListener.class})
@org.hibernate.annotations.Table(appliesTo = MemberMessage.TABLE_NAME, comment = "会员消息表")
public class MemberMessage extends BaseSuperEntity<MemberMessage, Long> {

	public static final String TABLE_NAME = "tt_member_message";
	/**
	 * 会员id
	 */
	@Column(name = "member_id", columnDefinition = "varchar(255) not null default '' comment '会员id'")
	private String memberId;

	/**
	 * 会员名称
	 */
	@Column(name = "member_name", columnDefinition = "varchar(255) not null default '' comment '会员名称'")
	private String memberName;

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

	/**
	 * 关联消息id
	 */
	@Column(name = "message_id", columnDefinition = "varchar(255) not null default '' comment '关联消息id'")
	private String messageId;

	/**
	 * 状态
	 *
	 * @see MessageStatusEnum
	 */
	@Column(name = "status", columnDefinition = "varchar(255) not null default '' comment '状态'")
	private String status = MessageStatusEnum.UN_READY.name();

}
