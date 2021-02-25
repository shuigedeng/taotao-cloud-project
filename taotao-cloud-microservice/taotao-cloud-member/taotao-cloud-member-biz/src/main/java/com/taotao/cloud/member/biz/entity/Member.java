package com.taotao.cloud.member.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 会员(c端用户)表
 *
 * @author dengtao
 * @date 2020/6/15 11:00
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_member")
@org.hibernate.annotations.Table(appliesTo = "tt_member", comment = "会员(c端用户)表")
public class Member extends BaseEntity {
	/**
	 * 昵称
	 */
	@Column(name = "nickname", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '昵称'")
	private String nickname;

	/**
	 * 用户名
	 */
	@Column(name = "username", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '用户名'")
	private String username;

	/**
	 * 手机号
	 */
	@Column(name = "phone", unique = true, nullable = false, columnDefinition = "varchar(14) not null comment '手机号'")
	private String phone;

	/**
	 * 密码
	 */
	@Column(name = "password", updatable = true, nullable = false, columnDefinition = "varchar(255) not null comment '密码'")
	private String password;

	/**
	 * 头像
	 */
	@Builder.Default
	@Column(name = "avatar", columnDefinition = "varchar(255) NOT NULL DEFAULT '' comment '头像'")
	private String avatar = "";

	/**
	 * @see UserSexEnum
	 */
	@Column(name = "gender", nullable = false, columnDefinition = "tinyint(1) unsigned not null default 0 comment '性别 1男 2女 0未知'")
	@Builder.Default
	private byte gender = 0;

	/**
	 * 邮箱
	 */
	@Column(name = "email", unique = true, columnDefinition = "varchar(30) comment '邮箱'")
	private String email;

	/**
	 * @see UserLevelEnum
	 */
	@Column(name = "level", nullable = false, columnDefinition = "tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '用户等级 0:普通用户 1:vip'")
	@Builder.Default
	private byte level = 0;

	@Builder.Default
	@Column(name = "type", nullable = false, columnDefinition = "tinyint(1) unsigned not null default 1 comment '用户类型 1个人用户 2企业用户'")
	private byte type = 1;

	/**
	 * 创建ip
	 */
	@Column(name = "create_ip", columnDefinition = "varchar(12) DEFAULT '' COMMENT '创建ip'")
	@Builder.Default
	private String createIp = "";

	/**
	 * 最后一次登陆时间
	 */
	@Column(name = "last_login_time", columnDefinition = "TIMESTAMP comment '最后一次登陆时间'")
	@Builder.Default
	private LocalDateTime lastLoginTime = LocalDateTime.now();

	/**
	 * 最后一次登陆ip
	 */
	@Column(name = "last_login_ip", columnDefinition = "varchar(12) DEFAULT '' COMMENT '最后一次登陆ip'")
	@Builder.Default
	private String lastLoginIp = "";

	/**
	 * 是否锁定 1-正常，2-锁定
	 */
	@Builder.Default
	@Column(name = "is_lock", nullable = false, columnDefinition = "tinyint(1) unsigned NOT NULL DEFAULT 1 comment '是否锁定 1-正常，0-锁定'")
	private Byte isLock = 1;

	/**
	 * 状态 1:启用, 0:停用
	 */
	@Builder.Default
	@Column(name = "status", nullable = false, columnDefinition = "tinyint(1) unsigned NOT NULL DEFAULT 1 comment '状态 1:启用, 0:停用'")
	private Byte status = 1;

	/**
	 * 登录次数
	 */
	@Column(name = "login_times", columnDefinition = " int(11) DEFAULT 0 COMMENT '登录次数'")
	private Integer loginTimes;

	/**
	 * 省code
	 */
	@Column(name = "province_code", columnDefinition = "varchar(32) COMMENT '省code'")
	private String provinceCode;

	/**
	 * 市code
	 */
	@Column(name = "city_code", columnDefinition = "varchar(32) COMMENT '市code'")
	private String cityCode;

	/**
	 * 区、县code
	 */
	@Column(name = "area_code", columnDefinition = "varchar(32) COMMENT '区、县code'")
	private String areaCode;
}
