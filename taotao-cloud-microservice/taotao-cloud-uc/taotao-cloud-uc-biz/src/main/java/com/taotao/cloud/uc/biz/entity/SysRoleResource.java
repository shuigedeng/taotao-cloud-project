package com.taotao.cloud.uc.biz.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色-资源第三方表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */

@Entity
@Table(name = "tt_sys_role_resource")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_role_resource", comment = "角色-资源第三方表")
public class SysRoleResource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigint not null comment 'id'")
	private Long id;

	/**
	 * 角色ID
	 */
	@Column(name = "role_id", nullable = false, columnDefinition = "bigint not null comment '角色ID'")
	private Long roleId;

	/**
	 * 资源ID
	 */
	@Column(name = "resource_id", nullable = false, columnDefinition = "bigint not null comment '资源ID'")
	private Long resourceId;

	@Override
	public String toString() {
		return "SysRoleResource{" +
			"id=" + id +
			", roleId=" + roleId +
			", resourceId=" + resourceId +
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
		SysRoleResource that = (SysRoleResource) o;
		return Objects.equals(id, that.id) && Objects.equals(roleId, that.roleId)
			&& Objects.equals(resourceId, that.resourceId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, roleId, resourceId);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public SysRoleResource() {
	}

	public SysRoleResource(Long id, Long roleId, Long resourceId) {
		this.id = id;
		this.roleId = roleId;
		this.resourceId = resourceId;
	}

	public static SysRoleResourceBuilder builder() {
		return new SysRoleResourceBuilder();
	}

	public static final class SysRoleResourceBuilder {

		private Long id;
		private Long roleId;
		private Long resourceId;

		private SysRoleResourceBuilder() {
		}

		public static SysRoleResourceBuilder aSysRoleResource() {
			return new SysRoleResourceBuilder();
		}

		public SysRoleResourceBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysRoleResourceBuilder roleId(Long roleId) {
			this.roleId = roleId;
			return this;
		}

		public SysRoleResourceBuilder resourceId(Long resourceId) {
			this.resourceId = resourceId;
			return this;
		}

		public SysRoleResource build() {
			SysRoleResource sysRoleResource = new SysRoleResource();
			sysRoleResource.resourceId = this.resourceId;
			sysRoleResource.roleId = this.roleId;
			sysRoleResource.id = this.id;
			return sysRoleResource;
		}
	}
}
