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
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 部门表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@Entity
@Table(name = Dept.TABLE_NAME)
@TableName(Dept.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Dept.TABLE_NAME, comment = "后台部门表")
public class Dept extends BaseSuperEntity<Dept,Long> {

	public static final String TABLE_NAME = "tt_sys_dept";

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

	public Dept() {
	}

	public Dept(String name, Long parentId, String remark, Integer sortNum,
		String tenantId) {
		this.name = name;
		this.parentId = parentId;
		this.remark = remark;
		this.sortNum = sortNum;
		this.tenantId = tenantId;
	}
}
