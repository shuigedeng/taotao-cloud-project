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
package com.taotao.cloud.sys.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * SysDict
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:04
 */
@Entity
@Table(name = Dict.TABLE_NAME)
@TableName(Dict.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Dict.TABLE_NAME, comment = "字典表")
public class Dict extends SuperEntity<Dict,Long> {

	public static final String TABLE_NAME = "tt_sys_dict";


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

	public Dict() {
	}

	public Dict(String dictName, String dictCode, String description,
		Integer sortNum, String remark, LocalDateTime createTime,
		LocalDateTime lastModifiedTime) {
		this.dictName = dictName;
		this.dictCode = dictCode;
		this.description = description;
		this.sortNum = sortNum;
		this.remark = remark;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}
}
