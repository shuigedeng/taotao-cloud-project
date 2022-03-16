package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.sys.biz.entity.system.RoleMenu;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.Set;

/**
 * 角色-菜单服务类
 *
 * @since 2020/4/30 13:20
 */
public interface IRoleMenuService extends BaseSuperService<RoleMenu, Long> {

	/**
	 * 添加角色-菜单对应关系
	 *
	 * @param roleId
	 * @param menuIds
	 * @return java.lang.Boolean
	 * @since 2020/10/21 09:20
	 */
	Boolean saveRoleMenu(Long roleId, Set<Long> menuIds);
}
