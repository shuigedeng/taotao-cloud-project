package com.taotao.cloud.uc.api.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 角色-资源DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "RoleResourceDTO", description = "角色-资源DTO")
public class RoleResourceDTO implements Serializable {

	private static final long serialVersionUID = -1972549738577159538L;

	@Schema(description = "角色id", required = true)
	@NotBlank(message = "角色id不能为空")
	private Long roleId;

	@Schema(description = "资源id列表", required = true)
	@NotEmpty(message = "资源id列表不能为空")
	private Set<Long> resourceIds;

	public RoleResourceDTO() {
	}

	public RoleResourceDTO(Long roleId, Set<Long> resourceIds) {
		this.roleId = roleId;
		this.resourceIds = resourceIds;
	}

	@Override
	public String toString() {
		return "RoleResourceDTO{" +
			"roleId=" + roleId +
			", resourceIds=" + resourceIds +
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
		RoleResourceDTO that = (RoleResourceDTO) o;
		return Objects.equals(roleId, that.roleId) && Objects.equals(resourceIds,
			that.resourceIds);
	}

	@Override
	public int hashCode() {
		return Objects.hash(roleId, resourceIds);
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Set<Long> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(Set<Long> resourceIds) {
		this.resourceIds = resourceIds;
	}

	public static RoleResourceDTOBuilder builder() {
		return new RoleResourceDTOBuilder();
	}


	public static final class RoleResourceDTOBuilder {

		private Long roleId;
		private Set<Long> resourceIds;

		private RoleResourceDTOBuilder() {
		}

		public static RoleResourceDTOBuilder aRoleResourceDTO() {
			return new RoleResourceDTOBuilder();
		}

		public RoleResourceDTOBuilder roleId(Long roleId) {
			this.roleId = roleId;
			return this;
		}

		public RoleResourceDTOBuilder resourceIds(Set<Long> resourceIds) {
			this.resourceIds = resourceIds;
			return this;
		}

		public RoleResourceDTO build() {
			RoleResourceDTO roleResourceDTO = new RoleResourceDTO();
			roleResourceDTO.setRoleId(roleId);
			roleResourceDTO.setResourceIds(resourceIds);
			return roleResourceDTO;
		}
	}
}
