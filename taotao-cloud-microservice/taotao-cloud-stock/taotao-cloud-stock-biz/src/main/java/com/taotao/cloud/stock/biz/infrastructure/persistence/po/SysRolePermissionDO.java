package com.taotao.cloud.stock.biz.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xtoon.cloud.common.mybatis.util.BaseDO;
import lombok.Data;

/**
 * 角色权限关联DO
 *
 * @author haoxin
 * @date 2021-02-15
 **/
@Data
@TableName("sys_role_permission")
public class SysRolePermissionDO extends BaseDO {

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 权限ID
     */
    private String permissionId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 备注
     */
    private String remarks;
}
