package com.taotao.cloud.uc.api.query.user;

import com.taotao.cloud.core.model.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 用户分页查询query
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name = "UserPageQuery", description = "用户分页查询query")
public class UserPageQuery extends BasePageQuery {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "用户昵称")
	private String nickname;

	@Schema(description = "用户真实姓名")
	private String username;

	@Schema(description = "电话")
	private String phone;

	@Schema(description = "email")
	private String email;

	@Schema(description = "部门id")
	private Long deptId;

	@Schema(description = "岗位id")
	private Long jobId;
}
