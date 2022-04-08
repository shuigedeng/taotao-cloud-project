package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 14:31:31
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Member.TABLE_NAME)
@TableName(Member.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Member.TABLE_NAME, comment = "会员表")
public class Member extends BaseSuperEntity<Member, Long> {

	public static final String TABLE_NAME = "tt_member";

	/**
	 * 昵称
	 */
	@Column(name = "nickname", nullable = false, columnDefinition = "varchar(64) not null comment '昵称'")
	private String nickname;

	/**
	 * 会员用户名
	 */
	@Column(name = "username", nullable = false, columnDefinition = "varchar(64) not null comment '会员用户名'")
	private String username;

	/**
	 * 会员密码
	 */
	@Column(name = "password", nullable = false, columnDefinition = "varchar(64) not null comment '会员密码'")
	private String password;

	/**
	 * 手机号码
	 */
	@Column(name = "mobile", nullable = false, columnDefinition = "varchar(64) not null comment '手机号码'")
	private String mobile;

	/**
	 * 会员性别,1为男，2为女
	 */
	@Column(name = "sex", columnDefinition = "int null comment '会员性别,1为男，2为女'")
	private Integer sex;

	/**
	 * 会员生日 yyyy-MM-dd
	 */
	@Column(name = "birthday", columnDefinition = "varchar(64) comment '会员生日 yyyy-MM-dd'")
	private String birthday;

	/**
	 * 会员地址ID
	 */
	@Column(name = "region_id", columnDefinition = "bigint null comment '会员地址ID'")
	private Long regionId;

	/**
	 * 会员地址
	 */
	@Column(name = "region", columnDefinition = "varchar(1024) comment '会员地址'")
	private String region;

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

	/**
	 * 积分数量
	 */
	@Column(name = "point", columnDefinition = "bigint not null default 0 comment '积分数量'")
	private Long point;

	/**
	 * 积分总数量
	 */
	@Column(name = "total_point", columnDefinition = "bigint not null default 0 comment '积分总数量'")
	private Long totalPoint;

	/**
	 * 会员头像地址
	 */
	@Column(name = "face", columnDefinition = "text null comment '会员头像地址'")
	private String face;

	/**
	 * 会员状态 false正常 true禁用
	 */
	@Column(name = "disabled", columnDefinition = "boolean default false comment '会员状态 false正常 true禁用'")
	private Boolean disabled;

	/**
	 * 是否锁定 false正常 true禁用
	 */
	@Column(name = "locked", nullable = false, columnDefinition = "boolean default false comment '是否锁定 false正常 true禁用'")
	private Boolean locked;

	/**
	 * 是否开通店铺 false未开通 true开通
	 */
	@Column(name = "have_store", columnDefinition = "boolean default false comment '是否开通店铺 false未开通 true开通'")
	private Boolean haveStore;

	/**
	 * 店铺ID
	 */
	@Column(name = "store_id", columnDefinition = "bigint null comment '店铺ID'")
	private Long storeId;

	/**
	 * 最近一次登录的客户端类型
	 *
	 * @see ClientTypeEnum
	 */
	@Column(name = "client", columnDefinition = "varchar(32) comment '最近一次登录的客户端类型'")
	private String client;

	/**
	 * 最近一次登录时间
	 */
	@Column(name = "last_login_date", columnDefinition = "TIMESTAMP comment '最近一次登录时间'")
	private LocalDateTime lastLoginDate;

	/**
	 * 最近一次登录ip
	 */
	@Column(name = "last_login_ip", columnDefinition = "varchar(12) COMMENT '最近一次登录ip'")
	private String lastLoginIp;

	/**
	 * 会员等级ID 用户等级 0:普通用户 1:vip
	 */
	@Column(name = "grade", columnDefinition = "int default 0 comment '会员等级ID 用户等级 0:普通用户 1:vip'")
	private Integer grade;

	/**
	 * 用户类型 1个人用户 2企业用户
	 */
	@Column(name = "type", columnDefinition = "int default 1 comment '用户类型 1个人用户 2企业用户'")
	private Integer type;

	/**
	 * 创建ip
	 */
	@Column(name = "create_ip", columnDefinition = "varchar(12) DEFAULT '' COMMENT '创建ip'")
	private String createIp;

	/**
	 * 经验值数量
	 */
	@Column(name = "experience", columnDefinition = "bigint default 0 comment '经验值数量'")
	private Long experience;

	public Member(String username, String password, String mobile) {
		this.username = username;
		this.password = password;
		this.mobile = mobile;
		this.nickname = mobile;
		this.disabled = false;
		this.locked = false;
		this.haveStore = false;
		this.sex = 0;
		this.point = 0L;
		this.totalPoint = 0L;
		this.lastLoginDate = LocalDateTime.now();
	}

	public Member(String username, String password, String mobile, String nickname, String face) {
		this.username = username;
		this.password = password;
		this.mobile = mobile;
		this.nickname = nickname;
		this.disabled = false;
		this.haveStore = false;
		this.locked = false;
		this.face = face;
		this.sex = 0;
		this.point = 0L;
		this.totalPoint = 0L;
		this.lastLoginDate = LocalDateTime.now();
	}

	public Member(String username, String password, String face, String nickname, Integer sex) {
		this.username = username;
		this.password = password;
		this.mobile = "";
		this.nickname = nickname;
		this.disabled = false;
		this.haveStore = false;
		this.locked = false;
		this.face = face;
		this.sex = sex;
		this.point = 0L;
		this.totalPoint = 0L;
		this.lastLoginDate = LocalDateTime.now();
	}
}
