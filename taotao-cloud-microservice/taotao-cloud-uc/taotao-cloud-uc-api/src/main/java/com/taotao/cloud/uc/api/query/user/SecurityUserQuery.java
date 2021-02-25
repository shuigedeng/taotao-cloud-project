package com.taotao.cloud.uc.api.query.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * SecurityUserQuery
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
@ApiModel(value = "Security查询query")
public class SecurityUserQuery implements Serializable {

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
}
