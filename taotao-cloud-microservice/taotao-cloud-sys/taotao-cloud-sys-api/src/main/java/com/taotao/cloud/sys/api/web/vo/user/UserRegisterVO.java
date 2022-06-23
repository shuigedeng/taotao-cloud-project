package com.taotao.cloud.sys.api.web.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

import lombok.*;

/**
 * 用户注册VO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户注册VO")
public class UserRegisterVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "真实用户名")
	private String username;

	@Schema(description = "手机号")
	private String phone;

	@Schema(description = "密码")
	private String password;

}
