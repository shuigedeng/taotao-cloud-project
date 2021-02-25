package com.taotao.cloud.uc.api.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户VO", description = "用户VO")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 5126530068827085130L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "真实用户名")
    private String username;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户类型 1前端用户 2商户用户 3后台管理用户")
    private Integer type;

    @ApiModelProperty(value = "性别 1男 2女 0未知")
    private Integer sex;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "岗位ID")
    private Long jobId;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "是否锁定 1-正常，2-锁定")
    private Integer lockFlag;

	@ApiModelProperty(value = "角色")
	private Set<String> roles;

	@ApiModelProperty(value = "权限")
	private Set<String> permissions;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime lastModifiedTime;

}
