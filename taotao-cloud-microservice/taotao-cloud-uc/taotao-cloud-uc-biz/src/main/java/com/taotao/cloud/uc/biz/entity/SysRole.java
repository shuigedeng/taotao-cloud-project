package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Entity
@Table(name = "tt_sys_role")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_role", comment = "角色表")
public class SysRole extends BaseEntity {

	/**
	 * 角色名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '角色名称'")
	private String name;

	/**
	 * 角色标识
	 */
	@Column(name = "code", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '角色标识'")
	private String code;

	/**
	 * 备注
	 */
	@Column(name = "remark", columnDefinition = "varchar(255) comment '备注'")
	private String remark;

	/**
	 * 租户id
	 */
	@Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
	private String tenantId;

	@Override
	public String toString() {
		return "SysRole{" +
			"name='" + name + '\'' +
			", code='" + code + '\'' +
			", remark='" + remark + '\'' +
			", tenantId='" + tenantId + '\'' +
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
		SysRole sysRole = (SysRole) o;
		return Objects.equals(name, sysRole.name) && Objects.equals(code,
			sysRole.code) && Objects.equals(remark, sysRole.remark)
			&& Objects.equals(tenantId, sysRole.tenantId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name, code, remark, tenantId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public SysRole() {
	}

	public SysRole(String name, String code, String remark, String tenantId) {
		this.name = name;
		this.code = code;
		this.remark = remark;
		this.tenantId = tenantId;
	}

	public SysRole(Long id, Long createBy, Long lastModifiedBy, LocalDateTime createTime,
		LocalDateTime lastModifiedTime, int version, Boolean delFlag, String name,
		String code, String remark, String tenantId) {
		super(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version, delFlag);
		this.name = name;
		this.code = code;
		this.remark = remark;
		this.tenantId = tenantId;
	}

	public static SysRoleBuilder builder() {
		return new SysRoleBuilder();
	}

	public static final class SysRoleBuilder {

		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;
		private String name;
		private String code;
		private String remark;
		private String tenantId;

		private SysRoleBuilder() {
		}

		public static SysRoleBuilder aSysRole() {
			return new SysRoleBuilder();
		}

		public SysRoleBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysRoleBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public SysRoleBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public SysRoleBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public SysRoleBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public SysRoleBuilder version(int version) {
			this.version = version;
			return this;
		}

		public SysRoleBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public SysRoleBuilder name(String name) {
			this.name = name;
			return this;
		}

		public SysRoleBuilder code(String code) {
			this.code = code;
			return this;
		}

		public SysRoleBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public SysRoleBuilder tenantId(String tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public SysRole build() {
			SysRole sysRole = new SysRole();
			sysRole.setId(id);
			sysRole.setCreateBy(createBy);
			sysRole.setLastModifiedBy(lastModifiedBy);
			sysRole.setCreateTime(createTime);
			sysRole.setLastModifiedTime(lastModifiedTime);
			sysRole.setVersion(version);
			sysRole.setDelFlag(delFlag);
			sysRole.name = this.name;
			sysRole.tenantId = this.tenantId;
			sysRole.code = this.code;
			sysRole.remark = this.remark;
			return sysRole;
		}
	}
}
