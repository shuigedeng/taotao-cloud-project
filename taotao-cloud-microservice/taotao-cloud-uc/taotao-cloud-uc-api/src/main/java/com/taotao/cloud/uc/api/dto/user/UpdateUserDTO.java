package com.taotao.cloud.uc.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户更新DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UpdateUserDTO", description = "用户更新DTO")
public class UpdateUserDTO implements Serializable {

	private static final long serialVersionUID = 7527760213215827929L;

	@Schema(description = "昵称", required = true)
	@NotBlank(message = "昵称不能为空")
	@Max(value = 10, message = "昵称不能超过10个字符")
	private String nickname;

	@Schema(description = "真实用户名", required = true)
	@NotBlank(message = "真实用户名不能为空")
	@Max(value = 10, message = "真实用户名不能超过10个字符")
	private String username;

	@Schema(description = "手机号", required = true)
	@NotBlank(message = "真实用户名不能为空")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码不正确")
	private String phone;

	@Schema(description = "邮箱")
	@Email(message = "邮箱格式错误")
	private String email;

	@Schema(description = "头像")
	private String avatar;

	@Schema(description = "部门ID")
	private Integer deptId;

	@Schema(description = "岗位ID")
	private Integer jobId;

	@Schema(description = "是否锁定用户")
	private Boolean lockFlag;

	@Schema(description = "是否删除用户")
	private Integer delFlag;

	@Schema(description = "角色id列表")
	private List<Integer> roleList;
}
