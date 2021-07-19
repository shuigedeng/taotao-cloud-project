package com.taotao.cloud.uc.api.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 角色DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RoleDTO", description = "添加角色对象DTO")
public class RoleDTO implements Serializable {

	private static final long serialVersionUID = -1972549738577159538L;

	@Schema(description = "角色名称", required = true)
	@NotBlank(message = "角色名称不能超过为空")
	@Length(max = 20, message = "角色名称不能超过20个字符")
	private String name;

	@Schema(description = "角色标识", required = true)
	@NotBlank(message = "角色标识不能超过为空")
	@Length(max = 20, message = "角色标识不能超过20个字符")
	@Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "角色标识格式错误：最多20字符，只能包含字母或者下划线")
	private String code;

	@Schema(description = "备注")
	private String remark;


}
