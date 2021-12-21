package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员消息
 *
 * @since 2020-02-25 14:10:16
 */
@Entity
@Table(name = MemberNoticeLog.TABLE_NAME)
@TableName(MemberNoticeLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberNoticeLog.TABLE_NAME, comment = "会员消息表")
public class MemberNoticeLog extends BaseSuperEntity<MemberNoticeLog, Long> {

	public static final String TABLE_NAME = "li_member_notice_log";

	@Column(name = "title", nullable = false, columnDefinition = "varchar(32) not null comment '标题'")
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "varchar(32) not null comment '消息内容'")
	private String content;

	@Column(name = "member_ids", nullable = false, columnDefinition = "varchar(32) not null comment '会员id'")
	private String memberIds;

	@Column(name = "admin_id", nullable = false, columnDefinition = "varchar(32) not null comment '管理员id'")
	private String adminId;

	@Column(name = "admin_name", nullable = false, columnDefinition = "varchar(32) not null comment '管理员名称'")
	private String adminName;

	@Column(name = "send_time", nullable = false, columnDefinition = "TIMESTAMP comment '发送时间'")
	private LocalDateTime sendTime;

	@Column(name = "send_type", nullable = false, columnDefinition = "int not null default 0 comment '发送类型,0全站，1指定会员'")
	private Integer sendType;

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

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public LocalDateTime getSendTime() {
		return sendTime;
	}

	public void setSendTime(LocalDateTime sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
}
