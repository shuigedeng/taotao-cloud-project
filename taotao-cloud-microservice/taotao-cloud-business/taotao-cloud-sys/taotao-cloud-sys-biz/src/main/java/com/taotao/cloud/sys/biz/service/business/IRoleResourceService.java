package com.taotao.cloud.sys.biz.service.business;

import com.taotao.cloud.sys.biz.model.entity.system.RoleResource;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.Set;

/**
 * 角色-菜单服务类 
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 15:01:39
 */
public interface IRoleResourceService extends BaseSuperService<RoleResource, Long> {

	/**
	 * 添加角色-菜单对应关系
	 *
	 * @param roleId  角色id
	 * @param menuIds 菜单id列表
	 * @return 是否成功
	 * @since 2020/10/21 09:20
	 */
	Boolean saveRoleMenu(Long roleId, Set<Long> menuIds);
}
