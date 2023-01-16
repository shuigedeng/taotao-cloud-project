package com.taotao.cloud.workflow.api.common.model.login;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 *
 */
@Data
public class UserCommonInfoVO {
    @Schema(description =  "用户id")
    private String userId;
    @Schema(description =  "用户账号")
    private String userAccount;
    @Schema(description =  "用户姓名")
    private String userName;
    @Schema(description =  "用户头像")
    private String headIcon;
    @Schema(description =  "组织主键")
    private String organizeId;
    @Schema(description =  "组织名称")
    private String organizeName;
    @Schema(description =  "岗位")
    private List<UserPositionVO> positionIds;
    @Schema(description =  "上次登录")
    private Integer prevLogin;
    @Schema(description =  "上次登录时间",example = "1")
    private Long prevLoginTime;
    @Schema(description =  "上次登录IP")
    private String prevLoginIPAddress;
    @Schema(description =  "上次登录地址")
    private String prevLoginIPAddressName;
    @Schema(description =  "门户id")
    private String portalId;
    /**
     * 当前组织角色+全局角色 Id数组
     */
    private List<String> roleIds;

    /**
     * 当前组织角色+全局角色 名称集合用 , 号隔开
     */
    private String roleName;

    /**
     * 直属主管 (u.RealName + "/" + u.Account)
     */
    private String manager;

    /**
     * 手机
     */
    private String mobilePhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生日
     */
    private Long birthday;

    /**
     * 部门id
     */
    private String departmentId;

    /**
     * 部门名称 结构树
     */
    private String departmentName;
}
