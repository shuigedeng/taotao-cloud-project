package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 在线用户信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_auth_online_user")
public class SysOnlineUser implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID (Token)
     */
    private String tokenValue;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 登录IP地址
     */
    private String ipAddress;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 最后活动时间
     */
    private LocalDateTime lastActivityTime;

    /**
     * Token过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 状态: 1-在线, 0-已下线
     */
    private Integer status;

    /**
     * 登出时间
     */
    private LocalDateTime logoutTime;

    /**
     * 登出类型: 1-主动登出, 2-被踢下线, 3-被顶下线, 4-Token过期
     */
    private Integer logoutType;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否被封禁(不持久化,仅用于展示)
     */
    @TableField(exist = false)
    private Boolean banned;
}

