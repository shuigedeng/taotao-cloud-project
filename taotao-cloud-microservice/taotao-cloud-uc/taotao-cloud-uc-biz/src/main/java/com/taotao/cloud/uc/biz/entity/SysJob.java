package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import com.taotao.cloud.uc.biz.entity.SysDictItem.SysDictItemBuilder;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 岗位表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Entity
@Table(name = "tt_sys_job")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_job", comment = "岗位表")
public class SysJob extends BaseEntity {

	/**
	 * 岗位名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '岗位名称'")
	private String name;

	/**
	 * 部门id
	 */
	@Column(name = "dept_id", columnDefinition = "bigint not null comment '部门id'")
	private Long deptId;

	/**
	 * 备注
	 */
	@Column(name = "remark", columnDefinition = "varchar(255) comment '备注'")
	private String remark;

	/**
	 * 排序值
	 */
	@Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
	private Integer sortNum = 0;

	/**
	 * 租户id
	 */
	@Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
	private String tenantId;

	public SysJob() {
	}

	public SysJob(String name, Long deptId, String remark, Integer sortNum, String tenantId) {
		this.name = name;
		this.deptId = deptId;
		this.remark = remark;
		this.sortNum = sortNum;
		this.tenantId = tenantId;
	}

	public SysJob(Long id, Long createBy, Long lastModifiedBy, LocalDateTime createTime,
		LocalDateTime lastModifiedTime, int version, Boolean delFlag,
		String name, Long deptId, String remark, Integer sortNum, String tenantId) {
		super(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version, delFlag);
		this.name = name;
		this.deptId = deptId;
		this.remark = remark;
		this.sortNum = sortNum;
		this.tenantId = tenantId;
	}

	@Override
	public String
	toString() {
		return "SysJob{" +
			"name='" + name + '\'' +
			", deptId=" + deptId +
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
		if (!super.equals(o)) {
			return false;
		}
		SysJob sysJob = (SysJob) o;
		return Objects.equals(name, sysJob.name) && Objects.equals(deptId,
			sysJob.deptId) && Objects.equals(remark, sysJob.remark)
			&& Objects.equals(sortNum, sysJob.sortNum) && Objects.equals(
			tenantId, sysJob.tenantId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name, deptId, remark, sortNum, tenantId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
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

	public static SysJobBuilder builder() {
		return new SysJobBuilder();
	}
	public static final class SysJobBuilder {

		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;
		private String name;
		private Long deptId;
		private String remark;
		private Integer sortNum = 0;
		private String tenantId;

		private SysJobBuilder() {
		}

		public static SysJobBuilder aSysJob() {
			return new SysJobBuilder();
		}

		public SysJobBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysJobBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public SysJobBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public SysJobBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public SysJobBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public SysJobBuilder version(int version) {
			this.version = version;
			return this;
		}

		public SysJobBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public SysJobBuilder name(String name) {
			this.name = name;
			return this;
		}

		public SysJobBuilder deptId(Long deptId) {
			this.deptId = deptId;
			return this;
		}

		public SysJobBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public SysJobBuilder sortNum(Integer sortNum) {
			this.sortNum = sortNum;
			return this;
		}

		public SysJobBuilder tenantId(String tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public SysJob build() {
			SysJob sysJob = new SysJob();
			sysJob.setId(id);
			sysJob.setCreateBy(createBy);
			sysJob.setLastModifiedBy(lastModifiedBy);
			sysJob.setCreateTime(createTime);
			sysJob.setLastModifiedTime(lastModifiedTime);
			sysJob.setVersion(version);
			sysJob.setDelFlag(delFlag);
			sysJob.setName(name);
			sysJob.setDeptId(deptId);
			sysJob.setRemark(remark);
			sysJob.setSortNum(sortNum);
			sysJob.setTenantId(tenantId);
			return sysJob;
		}
	}
}
