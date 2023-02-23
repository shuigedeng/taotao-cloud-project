package com.taotao.cloud.sys.biz.service.business;

import com.taotao.cloud.sys.biz.model.entity.system.UserRelation;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.Set;

/**
 * 用户-角色服务类
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:33:42
 */
public interface IUserRelationService extends BaseSuperService<UserRelation, Long> {

	/**
	 * 添加用户-角色对应关系
	 *
	 * @param userId  用户id
	 * @param roleIds 角色id列表
	 * @return 修改结果
	 */
	Boolean saveUserRoles(Long userId, Set<Long> roleIds);
}
