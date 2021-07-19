package com.taotao.cloud.uc.api.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户VO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserVO", description = "用户VO")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "id")
    private Long id;

	@Schema(description = "昵称")
    private String nickname;

	@Schema(description = "真实用户名")
    private String username;

	@Schema(description = "手机号")
    private String phone;

	@Schema(description = "用户类型 1前端用户 2商户用户 3后台管理用户")
    private Integer type;

	@Schema(description = "性别 1男 2女 0未知")
    private Integer sex;

	@Schema(description = "邮箱")
    private String email;

	@Schema(description = "部门ID")
    private Long deptId;

	@Schema(description = "岗位ID")
    private Long jobId;

	@Schema(description = "头像")
    private String avatar;

	@Schema(description = "是否锁定 1-正常，2-锁定")
    private Integer lockFlag;

	@Schema(description = "角色列表")
	private Set<String> roles;

	@Schema(description = "权限列表")
	private Set<String> permissions;

	@Schema(description = "创建时间")
    private LocalDateTime createTime;

	@Schema(description = "最后修改时间")
    private LocalDateTime lastModifiedTime;

}
