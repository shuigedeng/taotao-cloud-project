package com.taotao.cloud.uc.api.query.user;

import com.taotao.cloud.core.mvc.constraints.IntEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户查询query
 *
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "用户查询query")
public class UserQuery implements Serializable {

    private static final long serialVersionUID = -6200931899296559445L;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户真实姓名")
    private String username;

    @ApiModelProperty(value = "电话")
    @Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码格式错误")
    private String phone;

    @ApiModelProperty(value = "email")
    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty(value = "用户类型 1前端用户 2商户用户 3后台管理用户 ")
    @IntEnums(enumList = {1, 2, 3})
    private Integer type;

    @ApiModelProperty(value = "性别 1男 2女 0未知")
    @IntEnums(enumList = {0, 1, 2})
    private Integer sex;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "岗位id")
    private Long jobId;
}
