package com.taotao.cloud.uc.biz.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户-角色第三方表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Entity
@Table(name = "tt_sys_user_role")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_user_role", comment = "用户-角色第三方表")
public class SysUserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigint not null comment 'id'")
	private Long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id", nullable = false, columnDefinition = "bigint not null comment '用户ID'")
	private Long userId;

	/**
	 * 角色ID
	 */
	@Column(name = "role_id", nullable = false, columnDefinition = "bigint not null comment '角色ID'")
	private Long roleId;

	@Override
	public String toString() {
		return "SysUserRole{" +
			"id=" + id +
			", userId=" + userId +
			", roleId=" + roleId +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysUserRole that = (SysUserRole) o;
		return Objects.equals(id, that.id) && Objects.equals(userId, that.userId)
			&& Objects.equals(roleId, that.roleId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userId, roleId);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public SysUserRole() {
	}

	public SysUserRole(Long id, Long userId, Long roleId) {
		this.id = id;
		this.userId = userId;
		this.roleId = roleId;
	}

	public static SysUserRoleBuilder builder() {
		return new SysUserRoleBuilder();
	}

	public static final class SysUserRoleBuilder {

		private Long id;
		private Long userId;
		private Long roleId;

		private SysUserRoleBuilder() {
		}

		public static SysUserRoleBuilder aSysUserRole() {
			return new SysUserRoleBuilder();
		}

		public SysUserRoleBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysUserRoleBuilder userId(Long userId) {
			this.userId = userId;
			return this;
		}

		public SysUserRoleBuilder roleId(Long roleId) {
			this.roleId = roleId;
			return this;
		}

		public SysUserRole build() {
			SysUserRole sysUserRole = new SysUserRole();
			sysUserRole.roleId = this.roleId;
			sysUserRole.userId = this.userId;
			sysUserRole.id = this.id;
			return sysUserRole;
		}
	}
}
