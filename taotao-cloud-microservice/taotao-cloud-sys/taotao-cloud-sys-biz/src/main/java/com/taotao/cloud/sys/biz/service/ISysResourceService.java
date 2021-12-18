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

import com.taotao.cloud.sys.api.bo.resource.ResourceBO;
import com.taotao.cloud.sys.api.bo.resource.ResourceQueryBO;
import com.taotao.cloud.sys.api.vo.resource.ResourceQueryVO;
import com.taotao.cloud.sys.api.vo.resource.ResourceTreeVO;
import com.taotao.cloud.sys.biz.entity.Resource;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * ISysResourceService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:38:19
 */
public interface ISysResourceService extends BaseSuperService<Resource, Long> {

	/**
	 * 查询所有资源列表
	 *
	 * @return {@link List&lt;com.taotao.cloud.sys.biz.entity.SysResource&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:39:01
	 */
	List<ResourceBO> findAllResources();

	List<ResourceQueryBO> findAllById(Long id);

	/**
	 * 根据角色id列表获取角色列表
	 *
	 * @param roleIds roleIds
	 * @return {@link List&lt;com.taotao.cloud.sys.biz.entity.SysResource&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:39:07
	 */
	List<ResourceBO> findResourceByRoleIds(Set<Long> roleIds);

	/**
	 * 根据角色cde列表获取角色列表
	 *
	 * @param codes codes
	 * @return {@link List&lt;com.taotao.cloud.sys.biz.entity.SysResource&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:39:14
	 */
	List<ResourceBO> findResourceByCodes(Set<String> codes);

	/**
	 * 根据parentId获取角色列表
	 *
	 * @param parentId parentId
	 * @return {@link List&lt;com.taotao.cloud.sys.biz.entity.SysResource&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:39:19
	 */
	List<ResourceBO> findResourceByParentId(Long parentId);


	/**
	 * 根据id列表查询资源信息
	 *
	 * @param idList idList
	 * @return {@link List&lt;com.taotao.cloud.sys.biz.entity.SysResource&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:39:48
	 */
	List<ResourceBO> findResourceByIdList(List<Long> idList);

	/**
	 * 获取树形菜单集合 1.false-非懒加载，查询全部 " + "2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0
	 *
	 * @param lazy     lazy
	 * @param parentId parentId
	 * @return {@link List&lt;com.taotao.cloud.sys.api.vo.resource.ResourceTree&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:39:29
	 */
	List<ResourceTreeVO> findResourceTree(boolean lazy, Long parentId);

	/**
	 * 获取当前用户树形菜单列表
	 *
	 * @param resourceVOList resourceVOList
	 * @param parentId       parentId
	 * @return {@link List&lt;com.taotao.cloud.sys.api.vo.resource.ResourceTree&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:39:41
	 */
	List<ResourceTreeVO> findCurrentUserResourceTree(List<ResourceQueryVO> resourceVOList,
		Long parentId);



	Future<Boolean> testAsync();

	Boolean testSeata();
}
