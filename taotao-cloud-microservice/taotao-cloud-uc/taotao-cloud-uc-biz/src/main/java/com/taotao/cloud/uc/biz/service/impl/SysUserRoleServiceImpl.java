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
package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.uc.api.service.ISysRoleService;
import com.taotao.cloud.uc.api.service.ISysUserRoleService;
import com.taotao.cloud.uc.biz.entity.SysRole;
import com.taotao.cloud.uc.biz.entity.SysUserRole;
import com.taotao.cloud.uc.biz.mapper.SysRoleMapper;
import com.taotao.cloud.uc.biz.mapper.SysUserRoleMapper;
import com.taotao.cloud.uc.biz.repository.SysRoleRepository;
import com.taotao.cloud.uc.biz.repository.SysUserRoleRepository;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/21 09:20
 */
@Service
public class SysUserRoleServiceImpl extends
	BaseSuperServiceImpl<SysUserRoleMapper, SysUserRole, SysUserRoleRepository, Long>
	implements ISysUserRoleService<SysUserRole, Long> {

	//private final SysUserRoleRepository userRoleRepository;
	//
	//public SysUserRoleServiceImpl(
	//	SysUserRoleRepository userRoleRepository) {
	//	this.userRoleRepository = userRoleRepository;
	//}
	//
	//private final static QSysUserRole SYS_USER_ROLE = QSysUserRole.sysUserRole;
	//
	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public Boolean saveUserRoles(Long userId, Set<Long> roleIds) {
	//    BooleanExpression expression = SYS_USER_ROLE.userId.eq(userId);
	//    List<SysUserRole> userRoles = userRoleRepository.fetch(expression);
	//    if (CollUtil.isNotEmpty(userRoles)) {
	//        // 删除数据
	//        userRoleRepository.deleteAll(userRoles);
	//    }
	//    // 批量添加数据
	//    List<SysUserRole> collect = roleIds.stream().map(roleId -> SysUserRole.builder()
	//            .userId(userId)
	//            .roleId(roleId)
	//            .build()).collect(Collectors.toList());
	//    userRoleRepository.saveAll(collect);
	//    return true;
	//}
}
