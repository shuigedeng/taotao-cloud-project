package com.taotao.cloud.uc.api.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用户查询query
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name = "UserQuery", description = "用户查询query")
public class UserQuery implements Serializable {

	private static final long serialVersionUID = -6200931899296559445L;

	@Schema(description = "用户昵称")
	private String nickname;

	@Schema(description = "用户真实姓名")
	private String username;

	@Schema(description = "电话")
	private String phone;

	@Schema(description = "email")
	private String email;

	@Schema(description = "用户类型 1前端用户 2商户用户 3后台管理用户")
//	@IntEnums(value = {1, 2, 3})
	private Integer type;

	@Schema(description = "性别 1男 2女 0未知")
//	@IntEnums(value = {0, 1, 2})
	private Integer sex;

	@Schema(description = "部门id")
	private Long deptId;

	@Schema(description = "岗位id")
	private Long jobId;
}
