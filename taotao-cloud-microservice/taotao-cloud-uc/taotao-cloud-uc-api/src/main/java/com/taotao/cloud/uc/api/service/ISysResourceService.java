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
 * ISysResourceService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:38:19
 */
public interface ISysResourceService<T extends SuperEntity<I>, I extends Serializable> extends
	BaseSuperService<T, I> {

	///**
	// * 添加资源
	// *
	// * @param resource resource
	// * @return {@link SysResource }
	// * @author shuigedeng
	// * @since 2021-10-09 20:38:25
	// */
	//SysResource saveResource(SysResource resource);
	//
	///**
	// * 根据id删除资源
	// *
	// * @param id id
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:38:33
	// */
	//Boolean deleteResource(Long id);
	//
	///**
	// * 修改资源
	// *
	// * @param sysResource sysResource
	// * @return {@link SysResource }
	// * @author shuigedeng
	// * @since 2021-10-09 20:38:40
	// */
	//SysResource updateResource(SysResource sysResource);
	//
	///**
	// * 根据id获取资源信息
	// *
	// * @param id id
	// * @return {@link SysResource }
	// * @author shuigedeng
	// * @since 2021-10-09 20:38:46
	// */
	//SysResource findResourceById(Long id);
	//
	///**
	// * 分页查询资源集合
	// *
	// * @param pageable      pageable
	// * @param resourceQuery resourceQuery
	// * @return {@link Page&lt;com.taotao.cloud.uc.biz.entity.SysResource&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:38:52
	// */
	//Page<SysResource> findResourcePage(Pageable pageable, ResourceQuery resourceQuery);
	//
	///**
	// * 查询所有资源列表
	// *
	// * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysResource&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:39:01
	// */
	//List<SysResource> findAllResources();
	//
	///**
	// * 根据角色id列表获取角色列表
	// *
	// * @param roleIds roleIds
	// * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysResource&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:39:07
	// */
	//List<SysResource> findResourceByRoleIds(Set<Long> roleIds);
	//
	///**
	// * 根据角色cde列表获取角色列表
	// *
	// * @param codes codes
	// * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysResource&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:39:14
	// */
	//List<SysResource> findResourceByCodes(Set<String> codes);
	//
	///**
	// * 根据parentId获取角色列表
	// *
	// * @param parentId parentId
	// * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysResource&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:39:19
	// */
	//List<SysResource> findResourceByParentId(Long parentId);
	//
	///**
	// * 获取树形菜单集合 1.false-非懒加载，查询全部 " + "2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0
	// *
	// * @param lazy     lazy
	// * @param parentId parentId
	// * @return {@link List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceTree&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:39:29
	// */
	//List<ResourceTree> findResourceTree(boolean lazy, Long parentId);
	//
	///**
	// * 获取当前用户树形菜单列表
	// *
	// * @param resourceVOList resourceVOList
	// * @param parentId       parentId
	// * @return {@link List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceTree&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:39:41
	// */
	//List<ResourceTree> findCurrentUserResourceTree(List<ResourceVO> resourceVOList, Long parentId);
	//
	///**
	// * 根据id列表查询资源信息
	// *
	// * @param idList idList
	// * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysResource&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 20:39:48
	// */
	//List<SysResource> findResourceByIdList(List<Long> idList);
	//
	///**
	// * 根据名称获取资源信息
	// *
	// * @param name name
	// * @return {@link SysResource }
	// * @author shuigedeng
	// * @since 2021-10-09 20:39:57
	// */
	//SysResource findResourceByName(String name);
	//
	///**
	// * 根据id查询资源是否存在
	// *
	// * @param id id
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:40:03
	// */
	//Boolean existsById(Long id);
	//
	///**
	// * 根据名称查询资源是否存在
	// *
	// * @param name name
	// * @return {@link Boolean }
	// * @author shuigedeng
	// * @since 2021-10-09 20:40:13
	// */
	//Boolean existsByName(String name);
	//
	//Future<Boolean> testAsync();
	//
	//Boolean testSeata();
}
