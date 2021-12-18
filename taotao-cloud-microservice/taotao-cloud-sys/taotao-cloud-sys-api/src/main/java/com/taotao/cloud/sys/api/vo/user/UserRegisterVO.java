package com.taotao.cloud.sys.api.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册VO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(description = "用户注册VO")
public record UserRegisterVO(

	@Schema(description = "真实用户名")
	String username,

	@Schema(description = "手机号")
	String phone,

	@Schema(description = "密码")
	String password) implements Serializable {

	@Serial
	static final long serialVersionUID = 5126530068827085130L;


}
