package com.taotao.cloud.member.biz.model.entity;

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
 * 会员消息中心表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:24:19
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberNoticeSenter.TABLE_NAME)
@TableName(MemberNoticeSenter.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberNoticeSenter.TABLE_NAME, comment = "会员消息中心表")
public class MemberNoticeSenter extends BaseSuperEntity<MemberNoticeSenter, Long> {

	public static final String TABLE_NAME = "tt_member_notice_senter";

	/**
	 * 标题
	 */
	@Column(name = "title", columnDefinition = "varchar(255) not null comment '标题'")
	private String title;

	/**
	 * 消息内容
	 */
	@Column(name = "content", columnDefinition = "text not null comment '消息内容'")
	private String content;

	/**
	 * 会员id
	 */
	@Column(name = "member_ids", columnDefinition = "varchar(1024) not null comment '会员id'")
	private String memberIds;

	/**
	 * 发送类型,ALL 全站，SELECT 指定会员
	 */
	@Column(name = "send_type", columnDefinition = "varchar(32) not null comment '发送类型,ALL 全站，SELECT 指定会员'")
	private String sendType;
}
