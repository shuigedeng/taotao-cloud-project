package com.taotao.cloud.uc.api.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册VO
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "AddUserVO", description = "用户注册VO")
public class AddUserVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "真实用户名")
	private String username;

	@Schema(description = "手机号")
	private String phone;

	@Schema(description = "密码")
	private String password;
}
