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
package com.taotao.cloud.uc.api.service;

import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.io.Serializable;

/**
 * ISysRoleService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:42:57
 */
public interface ISysRoleService<T extends SuperEntity<I>, I extends Serializable> extends
	BaseSuperService<T, I> {

	///**
	// * 根据id获取角色信息
	// *
	// * @param id id
	// * @return {@link SysRole }
	// * @author shuigedeng
	// * @since 2021-10-09 20:43:08
	// */
	//SysRole findRoleById(Long id);
	//
	///**
	// * 根据用户id列表获取角色列表
	// *
	// * @param userIds userIds
	// * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysRole&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:43:25
	// */
	//List<SysRole> findRoleByUserIds(Set<Long> userIds);
	//
	///**
	// * 根据code查询角色是否存在
	// *
	// * @param code code
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:43:33
	// */
	//Boolean existRoleByCode(String code);
	//
	///**
	// * 根据code获取角色信息
	// *
	// * @param code code
	// * @return {@link SysRole }
	// * @author shuigedeng
	// * @since 2021-10-09 20:43:40
	// */
	//SysRole findRoleByCode(String code);
	//
	///**
	// * 添加角色
	// *
	// * @param roleDTO roleDTO
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:43:49
	// */
	//Boolean saveRole(RoleDTO roleDTO);
	//
	///**
	// * 修改角色
	// *
	// * @param id      id
	// * @param roleDTO roleDTO
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:43:55
	// */
	//Boolean updateRole(Long id, RoleDTO roleDTO);
	//
	///**
	// * 根据id删除角色
	// *
	// * @param id id
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:44:05
	// */
	//Boolean deleteRole(Long id);
	//
	///**
	// * 分页查询角色集合
	// *
	// * @param pageable  pageable
	// * @param roleQuery roleQuery
	// * @return {@link Page&lt;com.taotao.cloud.uc.biz.entity.SysRole&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:44:13
	// */
	//Page<SysRole> findRolePage(Pageable pageable, RoleQuery roleQuery);
	//
	///**
	// * 查询所有角色列表
	// *
	// * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysRole&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:45:23
	// */
	//List<SysRole> findAllRoles();
	//
	///**
	// * 根据角色id更新资源信息(角色分配资源)
	// *
	// * @param roleResourceDTO roleResourceDTO
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:45:35
	// */
	//Boolean saveRoleResources(RoleResourceDTO roleResourceDTO);
	//
	///**
	// * 根据code列表获取角色信息
	// *
	// * @param codes codes
	// * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysRole&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:45:41
	// */
	//List<SysRole> findRoleByCodes(Set<String> codes);
}
