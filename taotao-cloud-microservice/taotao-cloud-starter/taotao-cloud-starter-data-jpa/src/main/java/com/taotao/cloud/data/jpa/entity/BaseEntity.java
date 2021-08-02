/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.data.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.groups.Default;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 基础实体类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/28 16:28
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -3685249101751401211L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigint not null comment 'id'")
	private Long id;

	@CreatedBy
	@Column(name = "create_by", columnDefinition = "bigint comment '创建人'")
	private Long createBy;

	@LastModifiedBy
	@Column(name = "last_modified_by", columnDefinition = "bigint comment '最后修改人'")
	private Long lastModifiedBy;

	@CreatedDate
	@Column(name = "create_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'")
	private LocalDateTime createTime;

	@LastModifiedDate
	@Column(name = "last_modified_time", columnDefinition = "TIMESTAMP comment '最后修改时间'")
	private LocalDateTime lastModifiedTime;

	@Version
	@Column(name = "version", nullable = false, columnDefinition = "int not null default 1 comment '版本号'")
	private int version = 1;

	@Column(name = "del_flag", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否删除 0-正常 1-删除'")
	private Boolean delFlag = false;

	/**
	 * 保存和缺省验证组
	 */
	public interface Save extends Default {

	}

	/**
	 * 更新和缺省验证组
	 */
	public interface Update extends Default {

	}

	public BaseEntity() {
	}

	public BaseEntity(Long id, Long createBy, Long lastModifiedBy, LocalDateTime createTime,
		LocalDateTime lastModifiedTime, int version, Boolean delFlag) {
		this.id = id;
		this.createBy = createBy;
		this.lastModifiedBy = lastModifiedBy;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
		this.version = version;
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "BaseEntity{" +
			"id=" + id +
			", createBy=" + createBy +
			", lastModifiedBy=" + lastModifiedBy +
			", createTime=" + createTime +
			", lastModifiedTime=" + lastModifiedTime +
			", version=" + version +
			", delFlag=" + delFlag +
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
		BaseEntity that = (BaseEntity) o;
		return version == that.version && Objects.equals(id, that.id)
			&& Objects.equals(createBy, that.createBy) && Objects.equals(
			lastModifiedBy, that.lastModifiedBy) && Objects.equals(createTime,
			that.createTime) && Objects.equals(lastModifiedTime, that.lastModifiedTime)
			&& Objects.equals(delFlag, that.delFlag);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version,
			delFlag);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

}
