package com.taotao.cloud.member.api.model.dto;

import cn.hutool.core.lang.RegexPool;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 会员注册DTO
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-14 11:25:41
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "MemberDTO", description = "会员注册DTO")
public class MemberDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1972549738577159538L;

	@Schema(description = "用户昵称", required = true)
	@NotBlank(message = "用户昵称不能超过为空")
	@Length(max = 20, message = "用户昵称不能超过20个字符")
	@Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9\\*]*$", message = "用户昵称限制格式错误：最多20字符，包含文字、字母和数字")
	private String nickname;

	@Schema(description = "用户密码", required = true)
	@NotBlank(message = "用户密码不能超过为空")
	@Length(max = 18, message = "密码不能超过20个字符")
	@Length(min = 6, message = "密码不能小于6个字符")
	@Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$", message = "密码格式错误：密码至少包含 数字和英文，长度6-20个字符")
	private String password;

	@Schema(description = "手机号", required = true)
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = RegexPool.MOBILE, message = "手机号码格式错误")
	private String phone;
}
