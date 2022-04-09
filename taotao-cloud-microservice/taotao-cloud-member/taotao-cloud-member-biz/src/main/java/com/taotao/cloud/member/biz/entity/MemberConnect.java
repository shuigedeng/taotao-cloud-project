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
 * 联合登陆表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberConnect.TABLE_NAME)
@TableName(MemberConnect.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberConnect.TABLE_NAME, comment = "联合登陆表表")
public class MemberConnect extends BaseSuperEntity<MemberConnect, Long> {

	public static final String TABLE_NAME = "tt_member_connect";

	@Column(name = "user_id", columnDefinition = "varchar(32) not null comment '用户id'")
	private Long userId;

	@Column(name = "union_id", columnDefinition = "varchar(32) not null comment '联合登录id'")
	private String unionId;

	@Column(name = "union_type", columnDefinition = "varchar(32) not null comment '联合登录类型'")
	private String unionType;

	/**
	 * 会员id
	 */
	@Column(name = "member_id", columnDefinition = "bigint not null comment '会员id'")
	private Long memberId;

	/**
	 * 平台id
	 */
	@Column(name = "platform_id", columnDefinition = "varchar(255) not null default '' comment '平台id'")
	private String platformId;

	/**
	 * 平台类型
	 *
	 * @see PlatformTypeEnum
	 */
	@Column(name = "type", columnDefinition = "int NOT NULL DEFAULT 0 COMMENT '平台类型 0:未知,1:facebook,2:google,3:wechat,4:qq,5:weibo,6:twitter'")
	private Integer type;

	/**
	 * 昵称
	 */
	@Column(name = "nickname", unique = true, columnDefinition = "varchar(255) not null comment '昵称'")
	private String nickname;

	/**
	 * 头像
	 */
	@Column(name = "avatar", columnDefinition = "varchar(255) NOT NULL DEFAULT '' comment '头像'")
	private String avatar;
}
