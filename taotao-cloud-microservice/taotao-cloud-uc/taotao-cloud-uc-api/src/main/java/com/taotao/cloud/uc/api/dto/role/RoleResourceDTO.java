package com.taotao.cloud.uc.api.dto.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

/**
 * 角色-资源DTO
 *
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "角色-资源DTO")
public class RoleResourceDTO implements Serializable {

    private static final long serialVersionUID = -1972549738577159538L;

    @NotBlank(message = "角色id不能为空")
    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @Length(max = 20, message = "资源id不能为空")
    @ApiModelProperty(value = "资源id")
    private Set<Long> resourceIds;
}
