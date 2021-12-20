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
 * 角色表 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:52:30
 */
@Entity
@Table(name = Role.TABLE_NAME)
@TableName(Role.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Role.TABLE_NAME, comment = "角色表")
public class Role extends BaseSuperEntity<Role,Long> {

	public static final String TABLE_NAME = "tt_sys_role";

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

	public Role() {
	}


}
