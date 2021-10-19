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
 * 用户-角色第三方表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:04:45
 */
@Entity
@Table(name = SysUserRole.TABLE_NAME)
@TableName(SysUserRole.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SysUserRole.TABLE_NAME, comment = "用户-角色第三方表")
public class SysUserRole extends SuperEntity<SysUserRole,Long> {

	public static final String TABLE_NAME = "uc_sys_user_role";

	/**
	 * 用户ID
	 */
	@Column(name = "user_id", nullable = false, columnDefinition = "bigint not null comment '用户ID'")
	private Long userId;

	/**
	 * 角色ID
	 */
	@Column(name = "role_id", nullable = false, columnDefinition = "bigint not null comment '角色ID'")
	private Long roleId;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public SysUserRole() {
	}

	public SysUserRole(Long userId, Long roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	public static SysUserRoleBuilder builder() {
		return new SysUserRoleBuilder();
	}

	public static final class SysUserRoleBuilder {

		private Long id;
		private Long userId;
		private Long roleId;

		private SysUserRoleBuilder() {
		}


		public SysUserRoleBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysUserRoleBuilder userId(Long userId) {
			this.userId = userId;
			return this;
		}

		public SysUserRoleBuilder roleId(Long roleId) {
			this.roleId = roleId;
			return this;
		}

		public SysUserRole build() {
			SysUserRole sysUserRole = new SysUserRole();
			sysUserRole.setId(id);
			sysUserRole.setUserId(userId);
			sysUserRole.setRoleId(roleId);
			return sysUserRole;
		}
	}
}
