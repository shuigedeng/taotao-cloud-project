package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户新增/修改DTO
 */
@Data
public class SysUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（修改时必传）
     */
    private Long id;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 用户名（租户内唯一）
     */
    private String username;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 用户类型（0-系统管理员，1-租户管理员，2-普通用户）
     */
    private Integer userType;

    /**
     * 用户触点（app/pc/h5/wechat）
     */
    private String userClient;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    private Integer gender;

    /**
     * 密码（新增时必传）
     */
    private String password;

    /**
     * 用户状态（0-禁用，1-正常，2-锁定）
     */
    private Integer userStatus;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建部门
     */
    private Long createDept;
}
