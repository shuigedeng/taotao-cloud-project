package com.taotao.cloud.stock.biz.interfaces.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用户Command
 *
 * @author shuigedeng
 * @date 2021-02-20
 */
@Data
@ApiModel(value = "用户", description = "用户")
public class UserCommand {

    /**
     * id
     */
    @Schema(description =  "用户id")
    @NotBlank(message = "用户id不能为空", groups = UpdateGroup.class)
    private String id;

    /**
     * 用户名
     */
    @Schema(description =  "用户名")
    @NotBlank(message = "用户名不能为空", groups = AddGroup.class)
    private String userName;

    /**
     * 手机号
     */
    @Schema(description =  "手机号")
    @NotBlank(message = "手机号不能为空", groups = AddGroup.class)
    private String mobile;

    /**
     * 邮箱
     */
    @Schema(description =  "邮箱")
    @NotBlank(message = "邮箱不能为空", groups = AddGroup.class)
    private String email;

    /**
     * 角色列表
     */
    @Schema(description =  "角色列表")
    private List<String> roleIdList;
}
