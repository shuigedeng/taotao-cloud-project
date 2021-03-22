package com.taotao.cloud.uc.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * 角色DTO
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserRoleDTO", description = "用户-角色DTO")
public class UserRoleDTO implements Serializable {

	private static final long serialVersionUID = -1972549738577159538L;

	@Schema(description = "用户id", required = true)
	@NotNull(message = "用户id不能为空")
	private Long userId;

	@Schema(description = "角色id列表", required = true)
	@NotEmpty(message = "角色id列表不能为空")
	private Set<Long> roleIds;
}
