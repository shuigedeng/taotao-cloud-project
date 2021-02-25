package com.taotao.cloud.member.api.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 会员注册DTO
 *
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "会员注册DTO")
public class MemberDTO implements Serializable {

    private static final long serialVersionUID = -1972549738577159538L;

    @ApiModelProperty(value = "用户昵称", required = true)
    @NotBlank(message = "用户昵称不能超过为空")
    @Length(max = 20, message = "用户昵称不能超过20个字符")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9\\*]*$", message = "用户昵称限制格式错误：最多20字符，包含文字、字母和数字")
    private String nickname;

    @ApiModelProperty(value = "用户密码", required = true)
    @NotBlank(message = "用户密码不能超过为空")
    @Length(max = 18, message = "密码不能超过20个字符")
    @Length(min = 6, message = "密码不能小于6个字符")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$", message = "密码格式错误：密码至少包含 数字和英文，长度6-20个字符")
    private String password;

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码格式错误")
    private String phone;
}
