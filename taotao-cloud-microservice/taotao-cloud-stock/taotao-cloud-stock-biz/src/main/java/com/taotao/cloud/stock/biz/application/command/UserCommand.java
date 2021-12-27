package com.taotao.cloud.stock.biz.application.command;

import com.xtoon.cloud.common.web.util.validator.group.AddGroup;
import com.xtoon.cloud.common.web.util.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用户Command
 *
 * @author haoxin
 * @date 2021-02-20
 **/
@Data
@ApiModel(value = "用户", description = "用户")
public class UserCommand {

    /**
     * id
     */
    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空", groups = UpdateGroup.class)
    private String id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空", groups = AddGroup.class)
    private String userName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空", groups = AddGroup.class)
    private String mobile;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空", groups = AddGroup.class)
    private String email;

    /**
     * 角色列表
     */
    @ApiModelProperty(value = "角色列表")
    private List<String> roleIdList;
}
