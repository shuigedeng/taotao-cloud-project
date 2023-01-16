package com.taotao.cloud.workflow.api.common.model.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 */
@Data
public class AppInfoModel {
    @Schema(description =  "用户id")
    private String id;
    @Schema(description =  "用户账号")
    private String account;
    @Schema(description =  "用户姓名")
    private String realName;
    @Schema(description =  "用户头像")
    private String headIcon;
    @Schema(description =  "组织名称")
    private String organizeName;
    @Schema(description =  "部门名称")
    private String departmentName;
    @Schema(description =  "角色名称")
    private String roleName;
    @Schema(description =  "岗位名称")
    private String positionName;
    @Schema(description =  "性别")
    private Integer gender;
    @Schema(description =  "生日")
    private Long birthday;
    @Schema(description =  "手机号码")
    private String mobilePhone;
    @Schema(description =  "邮箱")
    private String email;

}
