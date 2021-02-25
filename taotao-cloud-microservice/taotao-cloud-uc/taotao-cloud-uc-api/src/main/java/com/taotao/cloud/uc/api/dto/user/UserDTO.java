package com.taotao.cloud.uc.api.dto.user;

import com.taotao.cloud.core.mvc.constraints.IntEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户DTO
 *
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户DTO")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -1972549738577159538L;

    @ApiModelProperty(value = "用户昵称")
    @NotBlank(message = "用户名不能超过为空")
    @Length(max = 20, message = "用户名不能超过20个字符")
    @Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "用户昵称限制格式错误：最多20字符，包含文字、字母和数字")
    private String nickname;

    @ApiModelProperty(value = "用户真实姓名")
    @NotBlank(message = "用户真实姓名不能超过为空")
    @Length(max = 20, message = "用户真实姓名不能超过20个字符")
    @Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "用户真实姓名格式错误：最多20字符，包含文字、字母和数字")
    private String username;

    @ApiModelProperty(value = "用户类型 1前端用户 2商户用户 3后台管理用户 ")
    @NotNull(message = "用户类型不能为空")
    @IntEnums(enumList = {1, 2, 3})
    private Integer type;

    @ApiModelProperty(value = "性别 1男 2女 0未知")
    @IntEnums(enumList = {0, 1, 2})
    private Integer sex;

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码格式错误")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "岗位ID")
    private Integer jobId;

    @ApiModelProperty(value = "头像")
    private String avatar;
}
