package com.taotao.cloud.stock.biz.interfaces.command;

import com.xtoon.cloud.common.web.util.validator.group.AddGroup;
import com.xtoon.cloud.common.web.util.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 权限Command
 *
 * @author haoxin
 * @date 2021-02-18
 **/
@Data
@ApiModel(value = "权限", description = "权限")
public class PermissionCommand {

    /**
     * id
     */
    @ApiModelProperty(value = "ID")
    @NotBlank(message = "ID不能为空", groups = UpdateGroup.class)
    private String id;

    /**
     * 父级ID
     */
    @ApiModelProperty(value = "父级ID")
    @NotBlank(message = "父级ID不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String parentId;

    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称")
    @NotBlank(message = "权限名称不能为空", groups = {AddGroup.class})
    private String permissionName;

    /**
     * 权限类型
     */
    @ApiModelProperty(value = "权限类型")
    @NotBlank(message = "权限类型不能为空", groups = {AddGroup.class})
    private String permissionType;

    /**
     * 权限级别
     */
    @ApiModelProperty(value = "权限级别")
    @NotBlank(message = "权限级别不能为空", groups = {AddGroup.class})
    private String permissionLevel;

    /**
     * 权限编码
     */
    @ApiModelProperty(value = "权限编码")
    private String permissionCodes;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String menuIcon;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private int orderNum;

    /**
     * 菜单url
     */
    @ApiModelProperty(value = "菜单url")
    private String menuUrl;
}
