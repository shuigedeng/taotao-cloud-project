/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.sys.biz.service.business.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.sys.biz.mapper.IUserRelationMapper;
import com.taotao.cloud.sys.biz.model.entity.system.QUserRelation;
import com.taotao.cloud.sys.biz.model.entity.system.UserRelation;
import com.taotao.cloud.sys.biz.repository.UserRelationRepository;
import com.taotao.cloud.sys.biz.repository.IUserRelationRepository;
import com.taotao.cloud.sys.biz.service.business.IUserRelationService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/21 09:20
 */
@Service
public class UserRelationServiceImpl
	extends BaseSuperServiceImpl<
	 UserRelation, Long, IUserRelationMapper,UserRelationRepository, IUserRelationRepository>
	implements IUserRelationService {

	private static final QUserRelation USER_RELATION = QUserRelation.userRelation;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveUserRoles(Long userId, Set<Long> roleIds) {
		BooleanExpression expression = USER_RELATION.userId.eq(userId);
		List<UserRelation> userRoles = cr().fetch(expression);
		if (CollUtil.isNotEmpty(userRoles)) {
			cr().deleteAll(userRoles);
		}

		// 批量添加数据
		List<UserRelation> collect = roleIds
			.stream()
			.map(roleId ->
				UserRelation.builder().userId(userId).objectId(roleId).build())
			.toList();
		cr().saveAll(collect);
		return true;
	}
}
