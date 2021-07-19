package com.taotao.cloud.uc.biz.service;

import java.util.Set;

/**
 * 用户-角色服务类
 *
 * @author shuigedeng
 * @since 2020/4/30 13:20
 */
public interface ISysUserRoleService {
    /**
     * 添加用户-角色对应关系
     *
     * @param userId
     * @param roleIds
     * @return java.lang.Boolean
     * @author shuigedeng
     * @since 2020/10/21 09:20
     * @version 1.0.0
     */
    public Boolean saveUserRoles(Long userId, Set<Long> roleIds);
}
