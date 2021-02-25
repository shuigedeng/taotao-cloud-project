package com.taotao.cloud.uc.biz.service;

import java.util.Set;

/**
 * 角色-资源服务类
 *
 * @author dengtao
 * @date 2020/4/30 13:20
 */
public interface ISysRoleResourceService {
    /**
     * 添加角色-资源对应关系
     *
     * @param roleId
     * @param resourceIds
     * @return java.lang.Boolean
     * @author dengtao
     * @date 2020/10/21 09:20
     * @since v1.0
     */
    public Boolean saveRoleResource(Long roleId, Set<Long> resourceIds);
}
