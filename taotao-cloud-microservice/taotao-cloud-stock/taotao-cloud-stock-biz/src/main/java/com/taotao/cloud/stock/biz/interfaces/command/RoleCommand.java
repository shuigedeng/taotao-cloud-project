package com.taotao.cloud.stock.biz.interfaces.command;

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
 * @author shuigedeng
 * @date 2021-02-18
 */
@Data
@ApiModel(value = "角色", description = "角色")
public class RoleCommand {

    /**
     * id
     */
    @Schema(description =  "角色id")
    @NotBlank(message = "角色id不能为空", groups = UpdateGroup.class)
    private String id;

    /**
     * 角色编码
     */
    @Schema(description =  "角色编码")
    @NotBlank(message = "角色编码不能为空", groups = AddGroup.class)
    private String roleCode;

    /**
     * 角色名称
     */
    @Schema(description =  "角色名称")
    @NotBlank(message = "角色名称不能为空", groups = AddGroup.class)
    private String roleName;

    /**
     * 备注
     */
    @Schema(description =  "备注")
    private String remarks;

    /**
     * 权限
     */
    @Schema(description =  "权限")
    private List<String> permissionIdList;
}
