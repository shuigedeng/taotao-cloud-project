package com.taotao.cloud.member.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 第三方登录信息
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Entity
@Table(name = "tt_member_platform")
@org.hibernate.annotations.Table(appliesTo = "tt_member_platform", comment = "第三方登录信息")
public class MemberPlatform extends BaseEntity {

	/**
	 * 会员id
	 *
	 * @see Member
	 */
	@Column(name = "member_user_id", nullable = false, columnDefinition = "int(11) not null comment '会员id'")
	private Long memberUserId;

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
	@Column(name = "type", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 COMMENT '平台类型 0:未知,1:facebook,2:google,3:wechat,4:qq,5:weibo,6:twitter'")
	private byte type = 0;

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

	@Override
	public String toString() {
		return "MemberPlatform{" +
			"memberUserId=" + memberUserId +
			", platformId='" + platformId + '\'' +
			", type=" + type +
			", nickname='" + nickname + '\'' +
			", avatar='" + avatar + '\'' +
			"} " + super.toString();
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		MemberPlatform that = (MemberPlatform) o;
		return type == that.type && Objects.equals(memberUserId, that.memberUserId)
			&& Objects.equals(platformId, that.platformId) && Objects.equals(
			nickname, that.nickname) && Objects.equals(avatar, that.avatar);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), memberUserId, platformId, type, nickname, avatar);
	}

	public Long getMemberUserId() {
		return memberUserId;
	}

	public void setMemberUserId(Long memberUserId) {
		this.memberUserId = memberUserId;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
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

	public MemberPlatform() {
	}

	public MemberPlatform(Long memberUserId, String platformId, byte type, String nickname,
		String avatar) {
		this.memberUserId = memberUserId;
		this.platformId = platformId;
		this.type = type;
		this.nickname = nickname;
		this.avatar = avatar;
	}

	public MemberPlatform(Long id, Long createBy, Long lastModifiedBy,
		LocalDateTime createTime, LocalDateTime lastModifiedTime, int version,
		Boolean delFlag, Long memberUserId, String platformId, byte type, String nickname,
		String avatar) {
		super(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version, delFlag);
		this.memberUserId = memberUserId;
		this.platformId = platformId;
		this.type = type;
		this.nickname = nickname;
		this.avatar = avatar;
	}

	public static MemberPlatformBuilder builder() {
		return new MemberPlatformBuilder();
	}

	public static final class MemberPlatformBuilder {

		private Long memberUserId;
		private String platformId = "";
		private byte type = 0;
		private String nickname;
		private String avatar = "";
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private MemberPlatformBuilder() {
		}

		public static MemberPlatformBuilder aMemberPlatform() {
			return new MemberPlatformBuilder();
		}

		public MemberPlatformBuilder memberUserId(Long memberUserId) {
			this.memberUserId = memberUserId;
			return this;
		}

		public MemberPlatformBuilder platformId(String platformId) {
			this.platformId = platformId;
			return this;
		}

		public MemberPlatformBuilder type(byte type) {
			this.type = type;
			return this;
		}

		public MemberPlatformBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public MemberPlatformBuilder avatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public MemberPlatformBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public MemberPlatformBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public MemberPlatformBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public MemberPlatformBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public MemberPlatformBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public MemberPlatformBuilder version(int version) {
			this.version = version;
			return this;
		}

		public MemberPlatformBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public MemberPlatform build() {
			MemberPlatform memberPlatform = new MemberPlatform();
			memberPlatform.setMemberUserId(memberUserId);
			memberPlatform.setPlatformId(platformId);
			memberPlatform.setType(type);
			memberPlatform.setNickname(nickname);
			memberPlatform.setAvatar(avatar);
			memberPlatform.setId(id);
			memberPlatform.setCreateBy(createBy);
			memberPlatform.setLastModifiedBy(lastModifiedBy);
			memberPlatform.setCreateTime(createTime);
			memberPlatform.setLastModifiedTime(lastModifiedTime);
			memberPlatform.setVersion(version);
			memberPlatform.setDelFlag(delFlag);
			return memberPlatform;
		}
	}
}
