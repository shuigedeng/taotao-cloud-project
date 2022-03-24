package com.taotao.cloud.stock.biz.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xtoon.cloud.common.mybatis.util.BaseDO;
import lombok.Data;

/**
 * 用户角色关联DO
 *
 * @author shuigedeng
 * @date 2021-02-15
 **/
@Data
@TableName("sys_user_role")
public class SysUserRoleDO extends BaseDO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 备注
     */
    private String remarks;
}
