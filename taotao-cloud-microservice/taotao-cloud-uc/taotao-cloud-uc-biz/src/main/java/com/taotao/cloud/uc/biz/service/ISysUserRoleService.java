package com.taotao.cloud.uc.biz.service;

import java.util.Set;

/**
 * 用户-角色服务类
 *
 * @author dengtao
 * @date 2020/4/30 13:20
 */
public interface ISysUserRoleService {
    /**
     * 添加用户-角色对应关系
     *
     * @param userId
     * @param roleIds
     * @return java.lang.Boolean
     * @author dengtao
     * @date 2020/10/21 09:20
     * @since v1.0
     */
    public Boolean saveUserRoles(Long userId, Set<Long> roleIds);
}
