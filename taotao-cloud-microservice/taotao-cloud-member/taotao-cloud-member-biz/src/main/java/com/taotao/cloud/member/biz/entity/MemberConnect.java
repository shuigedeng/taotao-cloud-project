package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.pulsar.shade.io.swagger.annotations.ApiModelProperty;

/**
 * 联合登陆表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Entity
@Table(name = MemberConnect.TABLE_NAME)
@TableName(MemberConnect.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberConnect.TABLE_NAME, comment = "联合登陆表表")
public class MemberConnect extends BaseSuperEntity<MemberConnect, Long> {

	public static final String TABLE_NAME = "tt_sys_dept";

	@Column(name = "user_id", nullable = false, columnDefinition = "varchar(32) not null comment '用户id'")
	private String userId;

	@Column(name = "union_id", nullable = false, columnDefinition = "varchar(32) not null comment '联合登录id'")
	private String unionId;

	/**
	 * @see cn.lili.modules.connect.entity.enums.ConnectEnum
	 */
	@Column(name = "union_type", nullable = false, columnDefinition = "varchar(32) not null comment '联合登录类型'")
	private String unionType;

	/**
	 * 会员id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "int(11) not null comment '会员id'")
	private Long memberId;

	/**
	 * 平台id
	 */
	@Column(name = "platform_id", nullable = false, columnDefinition = "varchar(255) not null default '' comment '平台id'")
	private String platformId = "";

	/**
	 * 平台类型
	 *
	 * @see PlatformTypeEnum
	 */
	@Column(name = "type", nullable = false, columnDefinition = "int NOT NULL DEFAULT 0 COMMENT '平台类型 0:未知,1:facebook,2:google,3:wechat,4:qq,5:weibo,6:twitter'")
	private Integer type = 0;

	/**
	 * 昵称
	 */
	@Column(name = "nickname", unique = true, nullable = false, columnDefinition = "varchar(255) not null comment '昵称'")
	private String nickname;

	/**
	 * 头像
	 */
	@Column(name = "avatar", columnDefinition = "varchar(255) NOT NULL DEFAULT '' comment '头像'")
	private String avatar = "";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getUnionType() {
		return unionType;
	}

	public void setUnionType(String unionType) {
		this.unionType = unionType;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
