package com.taotao.cloud.uc.api.dto.user;

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
 * 用户重置密码DTO
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RestPasswordUserDTO", description = "用户重置密码DTO")
public class RestPasswordUserDTO implements Serializable {

	private static final long serialVersionUID = -5002412807608124376L;

	@Schema(description = "手机号", required = true)
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码不正确")
	private String phone;

	@Schema(description = "原密码", required = true)
	@NotBlank(message = "原密码不能为空")
	private String oldPassword;

	@Schema(description = "新密码", required = true)
	@NotBlank(message = "新密码不能为空")
	@Length(min = 6, max = 128, message = "密码长度不能小于6位")
	private String newPassword;
}
