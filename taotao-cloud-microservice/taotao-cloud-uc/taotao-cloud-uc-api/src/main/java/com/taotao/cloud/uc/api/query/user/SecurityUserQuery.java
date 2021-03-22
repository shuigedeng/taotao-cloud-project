package com.taotao.cloud.uc.api.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * SecurityUserQuery
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name = "SecurityUserQuery", description = "用户查询query")
public class SecurityUserQuery implements Serializable {

	private static final long serialVersionUID = -6200931899296559445L;

	@Schema(description = "用户昵称")
	private String nickname;

	@Schema(description = "用户真实姓名")
	private String username;

	@Schema(description = "电话")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码格式错误")
	private String phone;

	@Schema(description = "email")
	@Email(message = "邮箱格式错误")
	private String email;
}
