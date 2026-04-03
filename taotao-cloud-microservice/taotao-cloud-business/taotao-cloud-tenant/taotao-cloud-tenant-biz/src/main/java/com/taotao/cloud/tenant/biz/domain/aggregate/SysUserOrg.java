package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户-组织关联表实体类
 */
@Data
@TableName("sys_user_org")
public class SysUserOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 是否主组织（0-否，1-是）
     */
    private Integer isMain;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
