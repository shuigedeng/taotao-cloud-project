package com.taotao.cloud.uc.api.dto.user;

import com.taotao.cloud.web.mvc.constraints.IntEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 用户DTO
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户DTO")
public class UserDTO implements Serializable {

	private static final long serialVersionUID = -1972549738577159538L;

	@Schema(description = "用户昵称", required = true)
	@NotBlank(message = "用户名不能超过为空")
	@Length(max = 20, message = "用户名不能超过20个字符")
	@Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "用户昵称限制格式错误：最多20字符，包含文字、字母和数字")
	private String nickname;

	@Schema(description = "用户真实姓名", required = true)
	@NotBlank(message = "用户真实姓名不能超过为空")
	@Length(max = 20, message = "用户真实姓名不能超过20个字符")
	@Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "用户真实姓名格式错误：最多20字符，包含文字、字母和数字")
	private String username;

	@Schema(description = "用户类型 1前端用户 2商户用户 3后台管理用户", required = true)
	@NotNull(message = "用户类型不能为空")
	@IntEnums(value = {1, 2, 3})
	private Integer type;

	@Schema(description = "性别 1男 2女 0未知", required = true)
	@NotNull(message = "用户性别不能为空")
	@IntEnums(value = {0, 1, 2})
	private Integer sex;

	@Schema(description = "手机号", required = true)
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码格式错误")
	private String phone;

	@Schema(description = "邮箱")
	@Email(message = "邮箱格式错误")
	private String email;

	@Schema(description = "部门ID")
	private Integer deptId;

	@Schema(description = "岗位ID")
	private Integer jobId;

	@Schema(description = "头像")
	private String avatar;
}
