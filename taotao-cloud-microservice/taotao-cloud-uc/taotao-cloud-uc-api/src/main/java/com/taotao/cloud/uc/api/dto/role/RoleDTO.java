package com.taotao.cloud.uc.api.dto.role;

import com.taotao.cloud.uc.api.vo.DeptTreeVo.DeptTreeVoBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 角色DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
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

	@Override
	public String toString() {
		return "RoleDTO{" +
			"name='" + name + '\'' +
			", code='" + code + '\'' +
			", remark='" + remark + '\'' +
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
		RoleDTO roleDTO = (RoleDTO) o;
		return Objects.equals(name, roleDTO.name) && Objects.equals(code,
			roleDTO.code) && Objects.equals(remark, roleDTO.remark);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, code, remark);
	}

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

	public RoleDTO() {
	}

	public RoleDTO(String name, String code, String remark) {
		this.name = name;
		this.code = code;
		this.remark = remark;
	}

	public static RoleDTOBuilder builder() {
		return new RoleDTOBuilder();
	}


	public static final class RoleDTOBuilder {

		private String name;
		private String code;
		private String remark;

		private RoleDTOBuilder() {
		}

		public static RoleDTOBuilder aRoleDTO() {
			return new RoleDTOBuilder();
		}

		public RoleDTOBuilder name(String name) {
			this.name = name;
			return this;
		}

		public RoleDTOBuilder code(String code) {
			this.code = code;
			return this;
		}

		public RoleDTOBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public RoleDTO build() {
			RoleDTO roleDTO = new RoleDTO();
			roleDTO.setName(name);
			roleDTO.setCode(code);
			roleDTO.setRemark(remark);
			return roleDTO;
		}
	}
}
