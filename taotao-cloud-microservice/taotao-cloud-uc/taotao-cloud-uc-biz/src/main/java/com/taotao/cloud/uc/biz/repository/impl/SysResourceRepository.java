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
package com.taotao.cloud.uc.biz.repository.impl;

import com.taotao.cloud.uc.biz.entity.SysResource;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 * SysResourceRepository
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:42:15
 */
@Repository
public class SysResourceRepository extends BaseSuperRepository<SysResource, Long> {

	public SysResourceRepository(EntityManager em) {
		super(SysResource.class, em);
	}

	//private final static QSysResource SYS_RESOURCE = QSysResource.sysResource;
	//private final static QSysRoleResource SYS_ROLE_RESOURCE = QSysRoleResource.sysRoleResource;

	/**
	 * findResourceByRoleIds
	 *
	 * @param roleIds roleIds
	 * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysResource&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:42:23
	 */
	public List<SysResource> findResourceByRoleIds(Set<Long> roleIds) {
		//return jpaQueryFactory.select(SYS_RESOURCE)
		//	.from(SYS_RESOURCE)
		//	.innerJoin(SYS_ROLE_RESOURCE)
		//	.on(SYS_RESOURCE.id.eq(SYS_ROLE_RESOURCE.resourceId))
		//	.where(SYS_ROLE_RESOURCE.roleId.in(roleIds))
		//	.fetch();
		return null;
	}

	/**
	 * findResourceByParentId
	 *
	 * @param parentId parentId
	 * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysResource&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:42:27
	 */
	public List<SysResource> findResourceByParentId(Long parentId) {
		//return jpaQueryFactory.selectFrom(SYS_RESOURCE)
		//	.where(SYS_RESOURCE.parentId.eq(parentId))
		//	.fetch();
		return null;
	}

	/**
	 * selectIdList
	 *
	 * @param pidList pidList
	 * @return {@link List&lt;java.lang.Long&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:42:31
	 */
	public List<Long> selectIdList(List<Long> pidList) {
		//return jpaQueryFactory.select(SYS_RESOURCE.id)
		//	.from(SYS_RESOURCE)
		//	.where(SYS_RESOURCE.parentId.in(pidList))
		//	.fetch();
		return null;
	}

	/**
	 * findResourceByIdList
	 *
	 * @param idList idList
	 * @return {@link List&lt;com.taotao.cloud.uc.biz.entity.SysResource&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:42:35
	 */
	public List<SysResource> findResourceByIdList(List<Long> idList) {
		//return jpaQueryFactory
		//	.selectFrom(SYS_RESOURCE)
		//	.where(SYS_RESOURCE.id.in(idList))
		//	.fetch();
		return null;
	}

	/**
	 * findResourceByName
	 *
	 * @param name name
	 * @return {@link Optional&lt;com.taotao.cloud.uc.biz.entity.SysResource&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 20:42:39
	 */
	public Optional<SysResource> findResourceByName(String name) {
		//SysResource resource = jpaQueryFactory
		//	.selectFrom(SYS_RESOURCE)
		//	.where(SYS_RESOURCE.name.eq(name))
		//	.fetchOne();
		//return Optional.ofNullable(resource);
		return null;
	}
}
