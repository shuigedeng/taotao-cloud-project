package com.taotao.cloud.uc.biz.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色-部门第三方表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */

@Entity
@Table(name = "tt_sys_role_dept")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_role_dept", comment = "角色-部门第三方表")
public class SysRoleDept {

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
	 * 部门ID
	 */
	@Column(name = "dept_id", nullable = false, columnDefinition = "bigint not null comment '部门ID'")
	private Long deptId;

	@Override
	public String toString() {
		return "SysRoleDept{" +
			"id=" + id +
			", roleId=" + roleId +
			", deptId=" + deptId +
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
		SysRoleDept that = (SysRoleDept) o;
		return Objects.equals(id, that.id) && Objects.equals(roleId, that.roleId)
			&& Objects.equals(deptId, that.deptId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, roleId, deptId);
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

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public SysRoleDept() {
	}

	public SysRoleDept(Long id, Long roleId, Long deptId) {
		this.id = id;
		this.roleId = roleId;
		this.deptId = deptId;
	}

	public static SysRoleDeptBuilder builder() {
		return new SysRoleDeptBuilder();
	}

	public static final class SysRoleDeptBuilder {

		private Long id;
		private Long roleId;
		private Long deptId;

		private SysRoleDeptBuilder() {
		}

		public static SysRoleDeptBuilder aSysRoleDept() {
			return new SysRoleDeptBuilder();
		}

		public SysRoleDeptBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysRoleDeptBuilder roleId(Long roleId) {
			this.roleId = roleId;
			return this;
		}

		public SysRoleDeptBuilder deptId(Long deptId) {
			this.deptId = deptId;
			return this;
		}

		public SysRoleDept build() {
			SysRoleDept sysRoleDept = new SysRoleDept();
			sysRoleDept.id = this.id;
			sysRoleDept.deptId = this.deptId;
			sysRoleDept.roleId = this.roleId;
			return sysRoleDept;
		}
	}
}
