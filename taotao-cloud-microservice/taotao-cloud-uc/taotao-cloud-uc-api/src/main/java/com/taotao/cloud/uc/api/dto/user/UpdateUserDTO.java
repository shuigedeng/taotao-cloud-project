package com.taotao.cloud.uc.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户更新DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema( description = "用户更新DTO")
public record UpdateUserDTO(

	@Schema(description = "昵称", required = true)
	@NotBlank(message = "昵称不能为空")
	@Max(value = 10, message = "昵称不能超过10个字符")
	String nickname,

	@Schema(description = "真实用户名", required = true)
	@NotBlank(message = "真实用户名不能为空")
	@Max(value = 10, message = "真实用户名不能超过10个字符")
	String username,

	@Schema(description = "手机号", required = true)
	@NotBlank(message = "真实用户名不能为空")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码不正确")
	String phone,

	@Schema(description = "邮箱")
	@Email(message = "邮箱格式错误")
	String email,

	@Schema(description = "头像")
	String avatar,

	@Schema(description = "部门ID")
	Integer deptId,

	@Schema(description = "岗位ID")
	Integer jobId,

	@Schema(description = "是否锁定用户")
	Boolean lockFlag,

	@Schema(description = "是否删除用户")
	Integer delFlag,

	@Schema(description = "角色id列表")
	List<Integer> roleList) implements Serializable {

	@Serial
	static final long serialVersionUID = 7527760213215827929L;


}
