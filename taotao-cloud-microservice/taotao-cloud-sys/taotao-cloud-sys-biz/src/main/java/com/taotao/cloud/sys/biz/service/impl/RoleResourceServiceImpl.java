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
package com.taotao.cloud.sys.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.sys.biz.entity.QRoleResource;
import com.taotao.cloud.sys.biz.entity.RoleResource;
import com.taotao.cloud.sys.biz.mapper.IRoleResourceMapper;
import com.taotao.cloud.sys.biz.repository.inf.IRoleResourceRepository;
import com.taotao.cloud.sys.biz.repository.cls.RoleResourceRepository;
import com.taotao.cloud.sys.biz.service.IRoleResourceService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/21 09:48
 */
@Service
public class RoleResourceServiceImpl extends
	BaseSuperServiceImpl<IRoleResourceMapper, RoleResource, RoleResourceRepository, IRoleResourceRepository, Long>
	implements IRoleResourceService {

	private final static QRoleResource SYS_ROLE_RESOURCE = QRoleResource.sysRoleResource;

	@Override
	public Boolean saveRoleResource(Long roleId, Set<Long> resourceIds) {
		BooleanExpression expression = SYS_ROLE_RESOURCE.roleId.eq(roleId);
		List<RoleResource> roleResources = cr().fetch(expression);
		if (CollUtil.isNotEmpty(roleResources)) {
			cr().deleteAll(roleResources);
		}

		// 批量添加数据
		List<RoleResource> collect = resourceIds.stream()
			.map(resourceId -> RoleResource.builder()
				.roleId(roleId)
				.resourceId(resourceId)
				.build())
			.collect(Collectors.toList());
		cr().saveAll(collect);
		return true;
	}
}
