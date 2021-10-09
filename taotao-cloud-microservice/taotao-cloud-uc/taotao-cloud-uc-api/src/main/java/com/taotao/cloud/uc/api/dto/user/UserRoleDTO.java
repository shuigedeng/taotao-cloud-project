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
package com.taotao.cloud.uc.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 角色DTO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:22:55
 */
@Schema(name = "UserRoleDTO", description = "用户-角色DTO")
public class UserRoleDTO implements Serializable {

	private static final long serialVersionUID = -1972549738577159538L;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id", required = true)
	@NotNull(message = "用户id不能为空")
	private Long userId;
	/**
	 * 角色id列表
	 */
	@Schema(description = "角色id列表", required = true)
	@NotEmpty(message = "角色id列表不能为空")
	private Set<Long> roleIds;

	@Override
	public String toString() {
		return "UserRoleDTO{" +
			"userId=" + userId +
			", roleIds=" + roleIds +
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
		UserRoleDTO that = (UserRoleDTO) o;
		return Objects.equals(userId, that.userId) && Objects.equals(roleIds,
			that.roleIds);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, roleIds);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Set<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Set<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public UserRoleDTO() {
	}

	public UserRoleDTO(Long userId, Set<Long> roleIds) {
		this.userId = userId;
		this.roleIds = roleIds;
	}

	public static UserRoleDTOBuilder builder() {
		return new UserRoleDTOBuilder();
	}

	public static final class UserRoleDTOBuilder {

		private Long userId;
		private Set<Long> roleIds;

		private UserRoleDTOBuilder() {
		}

		public static UserRoleDTOBuilder anUserRoleDTO() {
			return new UserRoleDTOBuilder();
		}

		public UserRoleDTOBuilder userId(Long userId) {
			this.userId = userId;
			return this;
		}

		public UserRoleDTOBuilder roleIds(Set<Long> roleIds) {
			this.roleIds = roleIds;
			return this;
		}

		public UserRoleDTO build() {
			UserRoleDTO userRoleDTO = new UserRoleDTO();
			userRoleDTO.setUserId(userId);
			userRoleDTO.setRoleIds(roleIds);
			return userRoleDTO;
		}
	}
}
