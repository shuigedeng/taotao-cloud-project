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
package com.taotao.cloud.sys.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.sys.biz.entity.system.QRoleMenu;
import com.taotao.cloud.sys.biz.model.entity.system.RoleMenu;
import com.taotao.cloud.sys.biz.mapper.IRoleMenuMapper;
import com.taotao.cloud.sys.biz.repository.inf.IRoleMenuRepository;
import com.taotao.cloud.sys.biz.repository.cls.RoleMenuRepository;
import com.taotao.cloud.sys.biz.service.IRoleMenuService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/21 09:48
 */
@Service
public class RoleMenuServiceImpl extends
	BaseSuperServiceImpl<IRoleMenuMapper, RoleMenu, RoleMenuRepository, IRoleMenuRepository, Long>
	implements IRoleMenuService {

	private final static QRoleMenu ROLE_MENU = QRoleMenu.roleMenu;

	@Override
	public Boolean saveRoleMenu(Long roleId, Set<Long> menuIds) {
		BooleanExpression expression = ROLE_MENU.roleId.eq(roleId);
		List<RoleMenu> roleMenus = cr().fetch(expression);
		if (CollUtil.isNotEmpty(roleMenus)) {
			cr().deleteAll(roleMenus);
		}

		// 批量添加数据
		//List<RoleMenu> collect = menuIds.stream()
		//	.map(resourceId -> RoleMenu.builder()
		//		.roleId(roleId)
		//		.resourceId(resourceId)
		//		.build())
		//	.collect(Collectors.toList());
		//cr().saveAll(collect);

		return true;
	}
}
