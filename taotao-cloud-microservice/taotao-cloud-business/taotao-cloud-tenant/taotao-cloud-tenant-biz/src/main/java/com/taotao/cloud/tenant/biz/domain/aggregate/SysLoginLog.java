package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录日志表实体
 */
@Data
@TableName("sys_login_log")
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long userId;

    private String username;

    private String loginType;

    private Integer loginStatus;

    private String loginIp;

    private String loginLocation;

    private String browser;

    private String os;

    private String userAgent;

    private String loginMessage;

    private LocalDateTime loginTime;
}
