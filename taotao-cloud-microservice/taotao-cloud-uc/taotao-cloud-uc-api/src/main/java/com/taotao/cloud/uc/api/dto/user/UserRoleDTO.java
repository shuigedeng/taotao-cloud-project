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
 * @since 2020/5/14 10:44
 */
@Schema(name = "UserRoleDTO", description = "用户-角色DTO")
public class UserRoleDTO implements Serializable {

	private static final long serialVersionUID = -1972549738577159538L;

	@Schema(description = "用户id", required = true)
	@NotNull(message = "用户id不能为空")
	private Long userId;

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
