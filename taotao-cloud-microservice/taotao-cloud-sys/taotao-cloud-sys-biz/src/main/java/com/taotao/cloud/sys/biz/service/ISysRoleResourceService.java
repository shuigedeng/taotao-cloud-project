package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.sys.biz.entity.RoleResource;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.Set;

/**
 * 角色-资源服务类
 *
 * @since 2020/4/30 13:20
 */
public interface ISysRoleResourceService extends BaseSuperService<RoleResource, Long> {

	/**
	 * 添加角色-资源对应关系
	 *
	 * @param roleId
	 * @param resourceIds
	 * @return java.lang.Boolean
	 * @since 2020/10/21 09:20
	 */
	Boolean saveRoleResource(Long roleId, Set<Long> resourceIds);
}
