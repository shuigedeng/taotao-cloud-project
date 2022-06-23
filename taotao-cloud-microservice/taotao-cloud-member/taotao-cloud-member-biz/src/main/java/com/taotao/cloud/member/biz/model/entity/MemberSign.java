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
 * 会员签到表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:31:33
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberSign.TABLE_NAME)
@TableName(MemberSign.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberSign.TABLE_NAME, comment = "会员签到表")
public class MemberSign extends BaseSuperEntity<MemberSign, Long> {

	public static final String TABLE_NAME = "tt_member_sign";

	/**
	 * 会员用户名
	 */
	@Column(name = "member_ame", columnDefinition = "varchar(32) not null comment '会员用户名'")
	private String memberName;

	/**
	 * 会员用户ID
	 */
	@Column(name = "member_id", columnDefinition = "bigint not null comment '会员用户ID'")
	private Long memberId;

	/**
	 * 连续签到天数
	 */
	@Column(name = "sign_day", columnDefinition = "int not null default 0 comment '连续签到天数'")
	private Integer signDay;

}
