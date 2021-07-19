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

import cn.hutool.core.collection.CollUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.uc.biz.entity.QSysUserRole;
import com.taotao.cloud.uc.biz.entity.SysUserRole;
import com.taotao.cloud.uc.biz.repository.SysUserRoleRepository;
import com.taotao.cloud.uc.biz.service.ISysUserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shuigedeng
 * @since 2020/10/21 09:20
 * @version 1.0.0
 */
@AllArgsConstructor
@Service
public class SysUserRoleServiceImpl implements ISysUserRoleService {

    private final SysUserRoleRepository userRoleRepository;
    private final static QSysUserRole SYS_USER_ROLE = QSysUserRole.sysUserRole;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUserRoles(Long userId, Set<Long> roleIds) {
        BooleanExpression expression = SYS_USER_ROLE.userId.eq(userId);
        List<SysUserRole> userRoles = userRoleRepository.fetch(expression);
        if (CollUtil.isNotEmpty(userRoles)) {
            // 删除数据
            userRoleRepository.deleteAll(userRoles);
        }
        // 批量添加数据
        List<SysUserRole> collect = roleIds.stream().map(roleId -> SysUserRole.builder()
                .userId(userId)
                .roleId(roleId)
                .build()).collect(Collectors.toList());
        userRoleRepository.saveAll(collect);
        return true;
    }
}
