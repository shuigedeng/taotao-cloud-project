package com.taotao.cloud.member.api.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 添加会员DTO
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-14 11:24:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "添加会员DTO")
public class MemberAddDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@NotEmpty(message = "会员用户名必填")
	@Size(max = 30, message = "会员用户名最长30位")
	@Schema(description = "会员用户名")
	private String username;

	@Schema(description = "会员密码")
	private String password;

	@NotEmpty(message = "手机号码不能为空")
	@Schema(description = "手机号码", required = true)
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
	private String mobile;
}
