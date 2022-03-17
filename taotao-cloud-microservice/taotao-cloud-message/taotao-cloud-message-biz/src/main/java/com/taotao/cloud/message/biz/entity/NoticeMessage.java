package com.taotao.cloud.message.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.message.api.enums.NoticeMessageParameterEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * 通知类消息模板表
 */
@Data
@Entity
@Table(name = NoticeMessage.TABLE_NAME)
@TableName(NoticeMessage.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = NoticeMessage.TABLE_NAME, comment = "通知类消息模板表")
public class NoticeMessage extends BaseSuperEntity<NoticeMessage, Long> {

	public static final String TABLE_NAME = "tt_notice_message";

	/**
	 * 站内信节点
	 */
	@Column(name = "notice_node", nullable = false, columnDefinition = "varchar(255) not null default '' comment '站内信节点'")
	private String noticeNode;

	/**
	 * 站内信标题
	 */
	@Column(name = "notice_title", nullable = false, columnDefinition = "varchar(255) not null default '' comment '站内信标题'")
	private String noticeTitle;

	/**
	 * 站内信内容
	 */
	@Column(name = "notice_content", nullable = false, columnDefinition = "varchar(255) not null default '' comment '站内信内容'")
	private String noticeContent;

	/**
	 * 站内信是否开启
	 *
	 * @see SwitchEnum
	 */
	@Column(name = "notice_status", nullable = false, columnDefinition = "varchar(255) not null default '' comment '站内信是否开启'")
	private String noticeStatus;

	/**
	 * 消息变量
	 *
	 * @see NoticeMessageParameterEnum
	 */
	@Column(name = "variable", nullable = false, columnDefinition = "varchar(255) not null default '' comment '字典名称'")
	private String variable;


}
