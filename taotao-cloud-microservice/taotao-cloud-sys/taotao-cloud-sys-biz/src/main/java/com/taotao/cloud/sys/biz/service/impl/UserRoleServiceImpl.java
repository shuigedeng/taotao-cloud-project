/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
import com.taotao.cloud.sys.biz.entity.system.QUserRole;
import com.taotao.cloud.sys.biz.entity.system.UserRole;
import com.taotao.cloud.sys.biz.mapper.IUserRoleMapper;
import com.taotao.cloud.sys.biz.repository.cls.UserRoleRepository;
import com.taotao.cloud.sys.biz.repository.inf.IUserRoleRepository;
import com.taotao.cloud.sys.biz.service.IUserRoleService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/21 09:20
 */
@Service
public class UserRoleServiceImpl extends
	BaseSuperServiceImpl<IUserRoleMapper, UserRole, UserRoleRepository, IUserRoleRepository, Long>
	implements IUserRoleService {

	private final static QUserRole USER_ROLE = QUserRole.userRole;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveUserRoles(Long userId, Set<Long> roleIds) {
		BooleanExpression expression = USER_ROLE.userId.eq(userId);
		List<UserRole> userRoles = cr().fetch(expression);
		if (CollUtil.isNotEmpty(userRoles)) {
			cr().deleteAll(userRoles);
		}

		// 批量添加数据
		List<UserRole> collect = roleIds.stream()
			.map(roleId -> UserRole.builder()
				.userId(userId)
				.roleId(roleId)
				.build()
			)
			.collect(Collectors.toList());
		cr().saveAll(collect);
		return true;
	}
}
