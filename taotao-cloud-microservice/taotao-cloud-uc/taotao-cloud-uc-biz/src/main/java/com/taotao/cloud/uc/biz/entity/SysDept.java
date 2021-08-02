package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 部门表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Entity
@Table(name = "tt_sys_dept")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_dept", comment = "后台部门表")
public class SysDept extends BaseEntity {

	/**
	 * 部门名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '部门名称'")
	private String name;

	/**
	 * 上级部门id
	 */
	@Column(name = "parent_id", columnDefinition = "int not null default 0 comment '上级部门id'")
	private Long parentId = 0L;

	/**
	 * 备注
	 */
	@Column(name = "remark", columnDefinition = "varchar(255) comment '备注'")
	private String remark;

	/**
	 * 排序值
	 */
	@Column(name = "sort_num", columnDefinition = "int not null default 0 comment '排序值'")
	private Integer sortNum = 0;

	/**
	 * 租户id
	 */
	@Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
	private String tenantId;

	@Override
	public String toString() {
		return "SysDept{" +
			"name='" + name + '\'' +
			", parentId=" + parentId +
			", remark='" + remark + '\'' +
			", sortNum=" + sortNum +
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
		SysDept sysDept = (SysDept) o;
		return Objects.equals(name, sysDept.name) && Objects.equals(parentId,
			sysDept.parentId) && Objects.equals(remark, sysDept.remark)
			&& Objects.equals(sortNum, sysDept.sortNum) && Objects.equals(
			tenantId, sysDept.tenantId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, parentId, remark, sortNum, tenantId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public SysDept() {
	}

	public SysDept(String name, Long parentId, String remark, Integer sortNum,
		String tenantId) {
		this.name = name;
		this.parentId = parentId;
		this.remark = remark;
		this.sortNum = sortNum;
		this.tenantId = tenantId;
	}

	public SysDept(Long id, Long createBy, Long lastModifiedBy, LocalDateTime createTime,
		LocalDateTime lastModifiedTime, int version, Boolean delFlag,
		String name, Long parentId, String remark, Integer sortNum, String tenantId) {
		super(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version, delFlag);
		this.name = name;
		this.parentId = parentId;
		this.remark = remark;
		this.sortNum = sortNum;
		this.tenantId = tenantId;
	}

	public static SysDeptBuilder builder() {
		return new SysDeptBuilder();
	}

	public static final class SysDeptBuilder {

		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;
		private String name;
		private Long parentId = 0L;
		private String remark;
		private Integer sortNum = 0;
		private String tenantId;

		private SysDeptBuilder() {
		}

		public static SysDeptBuilder aSysDept() {
			return new SysDeptBuilder();
		}

		public SysDeptBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysDeptBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public SysDeptBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public SysDeptBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public SysDeptBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public SysDeptBuilder version(int version) {
			this.version = version;
			return this;
		}

		public SysDeptBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public SysDeptBuilder name(String name) {
			this.name = name;
			return this;
		}

		public SysDeptBuilder parentId(Long parentId) {
			this.parentId = parentId;
			return this;
		}

		public SysDeptBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public SysDeptBuilder sortNum(Integer sortNum) {
			this.sortNum = sortNum;
			return this;
		}

		public SysDeptBuilder tenantId(String tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public SysDept build() {
			SysDept sysDept = new SysDept();
			sysDept.setId(id);
			sysDept.setCreateBy(createBy);
			sysDept.setLastModifiedBy(lastModifiedBy);
			sysDept.setCreateTime(createTime);
			sysDept.setLastModifiedTime(lastModifiedTime);
			sysDept.setVersion(version);
			sysDept.setDelFlag(delFlag);
			sysDept.setName(name);
			sysDept.setParentId(parentId);
			sysDept.setRemark(remark);
			sysDept.setSortNum(sortNum);
			sysDept.setTenantId(tenantId);
			return sysDept;
		}
	}
}
