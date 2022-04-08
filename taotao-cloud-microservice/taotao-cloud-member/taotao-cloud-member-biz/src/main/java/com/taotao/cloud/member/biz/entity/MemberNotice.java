package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员站内信表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:18:49
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberNotice.TABLE_NAME)
@TableName(MemberNotice.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberNotice.TABLE_NAME, comment = "会员站内信表")
public class MemberNotice extends BaseSuperEntity<MemberNotice, Long> {

	public static final String TABLE_NAME = "tt_member_notice";

	/**
	 * 会员id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "bigint not null comment '会员id'")
	private Long memberId;

	/**
	 * 是否已读
	 */
	@Column(name = "read", nullable = false, columnDefinition = "boolean not null default false comment '是否已读'")
	private Boolean read;

	/**
	 * 阅读时间
	 */
	@Column(name = "receive_time", nullable = false, columnDefinition = "bigint not null default 0 comment '阅读时间'")
	private Long receiveTime;

	/**
	 * 标题
	 */
	@Column(name = "title", nullable = false, columnDefinition = "varchar(32) not null comment '标题'")
	private String title;

	/**
	 * 站内信内容
	 */
	@Column(name = "content", nullable = false, columnDefinition = "varchar(1024) not null comment '站内信内容'")
	private String content;
}
