/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.sys.api.bo.role.RoleBO;
import com.taotao.cloud.sys.biz.entity.Role;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;
import java.util.Set;

/**
 * ISysRoleService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:42:57
 */
public interface ISysRoleService extends BaseSuperService<Role, Long> {

	/**
	 * 根据用户id列表获取角色列表
	 *
	 * @param userIds userIds
	 * @return {@link List&lt;com.taotao.cloud.sys.biz.entity.SysRole&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:43:25
	 */
	List<RoleBO> findRoleByUserIds(Set<Long> userIds);
	/**
	 * 查询所有角色列表
	 *
	 * @return {@link List&lt;com.taotao.cloud.sys.biz.entity.SysRole&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:45:23
	 */
	List<RoleBO> findAllRoles();


	/**
	 * 根据code列表获取角色信息
	 *
	 * @param codes codes
	 * @return {@link List&lt;com.taotao.cloud.sys.biz.entity.SysRole&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:45:41
	 */
	List<RoleBO> findRoleByCodes(Set<String> codes);

	/**
	 * 根据code查询角色是否存在
	 *
	 * @param code code
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-09 20:43:33
	 */
	Boolean existRoleByCode(String code);


	/**
	 * 根据角色id更新资源信息(角色分配资源)
	 *
	 * @param roleId      roleId
	 * @param resourceIds resourceIds
	 * @return {@link Boolean }
	 * @author shuigedeng
	 * @since 2021-10-09 20:45:35
	 */
	Boolean saveRoleResources(Long roleId, Set<Long> resourceIds);



}
