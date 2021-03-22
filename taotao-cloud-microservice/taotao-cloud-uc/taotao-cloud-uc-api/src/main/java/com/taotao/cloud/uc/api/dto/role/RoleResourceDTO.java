package com.taotao.cloud.uc.api.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 角色-资源DTO
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RoleResourceDTO", description = "角色-资源DTO")
public class RoleResourceDTO implements Serializable {

	private static final long serialVersionUID = -1972549738577159538L;

	@Schema(description = "角色id", required = true)
	@NotBlank(message = "角色id不能为空")
	private Long roleId;

	@Schema(description = "资源id列表", required = true)
	@NotEmpty(message = "资源id列表不能为空")
	private Set<Long> resourceIds;
}
