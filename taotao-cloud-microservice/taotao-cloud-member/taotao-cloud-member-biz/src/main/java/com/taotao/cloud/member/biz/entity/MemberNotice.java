package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 会员站内信
 *
 * @since 2020-02-25 14:10:16
 */
@Entity
@Table(name = MemberNotice.TABLE_NAME)
@TableName(MemberNotice.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberNotice.TABLE_NAME, comment = "会员站内信")
public class MemberNotice extends BaseSuperEntity<MemberNotice, Long> {

	public static final String TABLE_NAME = "li_member_notice";

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员id'")
	private String memberId;

	@Column(name = "is_read", nullable = false, columnDefinition = "boolean not null default false comment '是否已读'")
	private Boolean isRead;

	@Column(name = "receive_time", nullable = false, columnDefinition = "bigint not null default 0 comment '阅读时间'")
	private Long receiveTime;

	@Column(name = "title", nullable = false, columnDefinition = "varchar(32) not null comment '标题'")
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "varchar(32) not null comment '站内信内容'")
	private String content;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Boolean getRead() {
		return isRead;
	}

	public void setRead(Boolean read) {
		isRead = read;
	}

	public Long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Long receiveTime) {
		this.receiveTime = receiveTime;
	}

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
}
