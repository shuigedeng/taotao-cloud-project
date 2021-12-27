package com.taotao.cloud.stock.biz.application.command;

import com.xtoon.cloud.common.web.util.validator.group.AddGroup;
import com.xtoon.cloud.common.web.util.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 角色Command
 *
 * @author haoxin
 * @date 2021-02-18
 **/
@Data
@ApiModel(value = "角色", description = "角色")
public class RoleCommand {

    /**
     * id
     */
    @ApiModelProperty(value = "角色id")
    @NotBlank(message = "角色id不能为空", groups = UpdateGroup.class)
    private String id;

    /**
     * 角色编码
     */
    @ApiModelProperty(value = "角色编码")
    @NotBlank(message = "角色编码不能为空", groups = AddGroup.class)
    private String roleCode;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空", groups = AddGroup.class)
    private String roleName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 权限
     */
    @ApiModelProperty(value = "权限")
    private List<String> permissionIdList;
}
