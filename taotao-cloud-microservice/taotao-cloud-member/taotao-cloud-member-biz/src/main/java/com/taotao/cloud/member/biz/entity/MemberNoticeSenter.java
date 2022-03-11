package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 会员消息中心表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:24:19
 */
@Entity
@Table(name = MemberNoticeSenter.TABLE_NAME)
@TableName(MemberNoticeSenter.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberNoticeSenter.TABLE_NAME, comment = "会员消息中心表")
public class MemberNoticeSenter extends BaseSuperEntity<MemberNoticeSenter, Long> {

	public static final String TABLE_NAME = "tt_member_notice_senter";

	/**
	 * 标题
	 */
	@Column(name = "title", nullable = false, columnDefinition = "varchar(64) not null comment '标题'")
	private String title;

	/**
	 * 消息内容
	 */
	@Column(name = "content", nullable = false, columnDefinition = "text not null comment '消息内容'")
	private String content;

	/**
	 * 会员id
	 */
	@Column(name = "member_ids", nullable = false, columnDefinition = "varchar(1024) not null comment '会员id'")
	private String memberIds;

	/**
	 * 发送类型,ALL 全站，SELECT 指定会员
	 */
	@Column(name = "send_type", nullable = false, columnDefinition = "varchar(32) not null comment '发送类型,ALL 全站，SELECT 指定会员'")
	private String sendType;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
}
