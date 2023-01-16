package com.taotao.cloud.message.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.enums.SwitchEnum;
import com.taotao.cloud.message.api.enums.NoticeMessageParameterEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.JpaEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;

/**
 * 通知类消息模板表
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = NoticeMessage.TABLE_NAME)
@TableName(NoticeMessage.TABLE_NAME)
@EntityListeners({JpaEntityListener.class})
@org.hibernate.annotations.Table(appliesTo = NoticeMessage.TABLE_NAME, comment = "通知类消息模板表")
public class NoticeMessage extends BaseSuperEntity<NoticeMessage, Long> {

	public static final String TABLE_NAME = "tt_notice_message";

	/**
	 * 站内信节点
	 */
	@Column(name = "notice_node", columnDefinition = "varchar(255) not null default '' comment '站内信节点'")
	private String noticeNode;

	/**
	 * 站内信标题
	 */
	@Column(name = "notice_title", columnDefinition = "varchar(255) not null default '' comment '站内信标题'")
	private String noticeTitle;

	/**
	 * 站内信内容
	 */
	@Column(name = "notice_content", columnDefinition = "varchar(255) not null default '' comment '站内信内容'")
	private String noticeContent;

	/**
	 * 站内信是否开启
	 *
	 * @see SwitchEnum
	 */
	@Column(name = "notice_status", columnDefinition = "varchar(255) not null default '' comment '站内信是否开启'")
	private String noticeStatus;

	/**
	 * 消息变量
	 *
	 * @see NoticeMessageParameterEnum
	 */
	@Column(name = "variable", columnDefinition = "varchar(255) not null default '' comment '字典名称'")
	private String variable;


}
