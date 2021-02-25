package com.taotao.cloud.uc.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 用户更新DTO
 *
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户更新DTO")
public class UpdateUserDTO implements Serializable {

    private static final long serialVersionUID = 7527760213215827929L;

    @ApiModelProperty(value = "昵称")
    @Max(value = 10, message = "昵称不能超过10个字符")
    private String nickname;

    @ApiModelProperty(value = "真实用户名")
    @Max(value = 10, message = "真实用户名不能超过10个字符")
    private String username;

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码不正确")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "岗位ID")
    private Integer jobId;

    @ApiModelProperty(value = "是否锁定用户")
    private String lockFlag;

    @ApiModelProperty(value = "是否删除用户")
    private String delFlag;

    @ApiModelProperty(value = "角色id列表")
    private List<Integer> roleList;
}
