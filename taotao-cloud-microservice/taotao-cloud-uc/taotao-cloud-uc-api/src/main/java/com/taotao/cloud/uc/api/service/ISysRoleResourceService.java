package com.taotao.cloud.uc.api.service;

import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.io.Serializable;
import java.util.Set;

/**
 * 角色-资源服务类
 *
 * @since 2020/4/30 13:20
 */
public interface ISysRoleResourceService<T extends SuperEntity<I>, I extends Serializable> extends
	BaseSuperService<T, I> {

	///**
	// * 添加角色-资源对应关系
	// *
	// * @param roleId
	// * @param resourceIds
	// * @return java.lang.Boolean
	// * @since 2020/10/21 09:20
	// */
	//public Boolean saveRoleResource(Long roleId, Set<Long> resourceIds);
}
