package com.taotao.cloud.uc.api.dto.role;

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
 * 角色DTO
 *
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "角色DTO")
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = -1972549738577159538L;

    @NotBlank(message = "角色名称不能超过为空")
    @Length(max = 20, message = "角色名称不能超过20个字符")
    @ApiModelProperty(value = "角色名称")
    private String name;

    @NotBlank(message = "角色标识不能超过为空")
    @Length(max = 20, message = "角色标识不能超过20个字符")
    @ApiModelProperty(value = "角色标识")
    @Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "角色标识格式错误：最多20字符，只能包含字母或者下划线")
    private String code;

    @ApiModelProperty(value = "备注")
    private String remark;


}
