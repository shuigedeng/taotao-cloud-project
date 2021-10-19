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
package com.taotao.cloud.uc.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色-部门第三方表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:50:39
 */
@Entity
@Table(name = SysRoleDept.TABLE_NAME)
@TableName(SysRoleDept.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SysRoleDept.TABLE_NAME, comment = "角色-部门第三方表")
public class SysRoleDept extends SuperEntity<SysRoleDept,Long> {

	public static final String TABLE_NAME = "uc_sys_role_dept";

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

	public SysRoleDept(Long roleId, Long deptId) {
		this.roleId = roleId;
		this.deptId = deptId;
	}
}
