/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * JpaSuperEntity
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:35:39
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class JpaSuperEntity<I extends Serializable> implements Serializable {

	private static final long serialVersionUID = -3685249101751401211L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigint not null comment 'id'")
	private I id;

	/**
	 * 创建人
	 */
	@CreatedBy
	@Column(name = "create_by", columnDefinition = "bigint comment '创建人'")
	private I createBy;

	/**
	 * 最后修改人
	 */
	@LastModifiedBy
	@Column(name = "last_modified_by", columnDefinition = "bigint comment '最后修改人'")
	private I lastModifiedBy;

	/**
	 * 创建时间
	 */
	@CreatedDate
	@Column(name = "create_time", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'")
	private LocalDateTime createTime;

	/**
	 * 最后修改时间
	 */
	@LastModifiedDate
	@Column(name = "last_modified_time", columnDefinition = "TIMESTAMP comment '最后修改时间'")
	private LocalDateTime lastModifiedTime;

	/**
	 * 版本号
	 */
	@Version
	@Column(name = "version", columnDefinition = "int not null default 1 comment '版本号'")
	private int version = 1;

	/**
	 * 是否删除 0-正常 1-删除
	 */
	@Column(name = "del_flag", columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否删除 0-正常 1-删除'")
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

	public JpaSuperEntity(I id, I createBy, I lastModifiedBy, LocalDateTime createTime,
		LocalDateTime lastModifiedTime, int version, Boolean delFlag) {
		this.id = id;
		this.createBy = createBy;
		this.lastModifiedBy = lastModifiedBy;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
		this.version = version;
		this.delFlag = delFlag;
	}

	public JpaSuperEntity() {
	}

	public I getId() {
		return id;
	}

	public void setId(I id) {
		this.id = id;
	}

	public I getCreateBy() {
		return createBy;
	}

	public void setCreateBy(I createBy) {
		this.createBy = createBy;
	}

	public I getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(I lastModifiedBy) {
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
