package com.taotao.cloud.uc.biz.entity;// package com.taotao.cloud.uc.biz.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * 字典表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Entity
@Table(name = "tt_sys_dict")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_dict", comment = "字典表")
public class SysDict {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigint not null comment 'id'")
	private Long id;

	/**
	 * 字典名称
	 */
	@Column(name = "dict_name", nullable = false, columnDefinition = "varchar(255) not null default '' comment '字典名称'")
	private String dictName;

	/**
	 * 字典编码
	 */
	@Column(name = "dict_code", unique = true, nullable = false, columnDefinition = "varchar(255) not null comment '字典编码'")
	private String dictCode;

	/**
	 * 描述
	 */
	@Column(name = "description", columnDefinition = "varchar(255) comment '描述'")
	private String description;

	/**
	 * 排序值
	 */
	@Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
	private Integer sortNum = 0;

	/**
	 * 备注信息
	 */
	@Column(name = "remark", columnDefinition = "varchar(255) comment '备注信息'")
	private String remark;


	@CreatedDate
	@Column(name = "create_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'")
	private LocalDateTime createTime;

	@LastModifiedDate
	@Column(name = "last_modified_time", columnDefinition = "TIMESTAMP comment '最后修改时间'")
	private LocalDateTime lastModifiedTime;

	@Override
	public String toString() {
		return "SysDict{" +
			"id=" + id +
			", dictName='" + dictName + '\'' +
			", dictCode='" + dictCode + '\'' +
			", description='" + description + '\'' +
			", sortNum=" + sortNum +
			", remark='" + remark + '\'' +
			", createTime=" + createTime +
			", lastModifiedTime=" + lastModifiedTime +
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
		SysDict sysDict = (SysDict) o;
		return Objects.equals(id, sysDict.id) && Objects.equals(dictName,
			sysDict.dictName) && Objects.equals(dictCode, sysDict.dictCode)
			&& Objects.equals(description, sysDict.description)
			&& Objects.equals(sortNum, sysDict.sortNum) && Objects.equals(
			remark, sysDict.remark) && Objects.equals(createTime, sysDict.createTime)
			&& Objects.equals(lastModifiedTime, sysDict.lastModifiedTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, dictName, dictCode, description, sortNum, remark, createTime,
			lastModifiedTime);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public SysDict() {
	}

	public SysDict(Long id, String dictName, String dictCode, String description,
		Integer sortNum, String remark, LocalDateTime createTime,
		LocalDateTime lastModifiedTime) {
		this.id = id;
		this.dictName = dictName;
		this.dictCode = dictCode;
		this.description = description;
		this.sortNum = sortNum;
		this.remark = remark;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	public static SysDictBuilder builder() {
		return new SysDictBuilder();
	}

	public static final class SysDictBuilder {

		private Long id;
		private String dictName;
		private String dictCode;
		private String description;
		private Integer sortNum = 0;
		private String remark;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;

		private SysDictBuilder() {
		}

		public static SysDictBuilder aSysDict() {
			return new SysDictBuilder();
		}

		public SysDictBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysDictBuilder dictName(String dictName) {
			this.dictName = dictName;
			return this;
		}

		public SysDictBuilder dictCode(String dictCode) {
			this.dictCode = dictCode;
			return this;
		}

		public SysDictBuilder description(String description) {
			this.description = description;
			return this;
		}

		public SysDictBuilder sortNum(Integer sortNum) {
			this.sortNum = sortNum;
			return this;
		}

		public SysDictBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public SysDictBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public SysDictBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public SysDict build() {
			SysDict sysDict = new SysDict();
			sysDict.setId(id);
			sysDict.setDictName(dictName);
			sysDict.setDictCode(dictCode);
			sysDict.setDescription(description);
			sysDict.setSortNum(sortNum);
			sysDict.setRemark(remark);
			sysDict.setCreateTime(createTime);
			sysDict.setLastModifiedTime(lastModifiedTime);
			return sysDict;
		}
	}
}
