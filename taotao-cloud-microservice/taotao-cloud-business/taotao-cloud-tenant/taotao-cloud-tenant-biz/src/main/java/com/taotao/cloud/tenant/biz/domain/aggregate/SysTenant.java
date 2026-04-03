package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 租户表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant")
public class SysTenant extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 租户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 租户名称
     */
    private String tenantName;
    
    /**
     * 负责人
     */
    private String contactPerson;
    
    /**
     * 联系电话
     */
    private String contactPhone;
    
    /**
     * 租户人员数量上限（0表示无限制）
     */
    private Integer userLimit;
    
    /**
     * 租户状态（0-禁用，1-正常）
     */
    private Integer tenantStatus;
    
    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
    
    /**
     * 租户描述
     */
    private String tenantDesc;
    
    /**
     * 浏览器icon（存储图标URL/Base64）
     */
    private String browserIcon;
    
    /**
     * 浏览器标签名称
     */
    private String browserTitle;
    
    /**
     * 系统名称
     */
    private String systemName;
    
    /**
     * 系统logo（存储logo URL/Base64）
     */
    private String systemLogo;
    
    /**
     * 系统介绍
     */
    private String systemIntro;
    
    /**
     * 版权显示文本
     */
    private String copyrightInfo;
    
    /**
     * 系统布局（default-默认，classic-经典，modern-现代等）
     */
    private String systemLayout;
    
    /**
     * 系统主题（light-亮色，dark-暗色，auto-跟随系统等）
     */
    private String systemTheme;
}
