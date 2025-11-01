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
import com.taotao.cloud.sys.biz.mapper.IRoleResourceMapper;
import com.taotao.cloud.sys.biz.model.entity.system.QRoleResource;
import com.taotao.cloud.sys.biz.model.entity.system.RoleResource;
import com.taotao.cloud.sys.biz.repository.RoleResourceRepository;
import com.taotao.cloud.sys.biz.repository.IRoleResourceRepository;
import com.taotao.cloud.sys.biz.service.business.IRoleResourceService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import java.util.List;
import java.util.Set;
import lombok.*;

import org.springframework.stereotype.Service;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/21 09:48
 */
@Service
@AllArgsConstructor
public class RoleResourceServiceImpl
        extends BaseSuperServiceImpl<
                 RoleResource, Long,IRoleResourceMapper, RoleResourceRepository, IRoleResourceRepository>
        implements IRoleResourceService {

    private static final QRoleResource ROLE_RESOURCE = QRoleResource.roleResource;

    @Override
    public Boolean saveRoleMenu(Long roleId, Set<Long> menuIds) {
        BooleanExpression expression = ROLE_RESOURCE.roleId.eq(roleId);
        List<RoleResource> roleResources = cr().fetch(expression);
        if (CollUtil.isNotEmpty(roleResources)) {
            cr().deleteAll(roleResources);
        }

        // 批量添加数据
        // List<RoleMenu> collect = menuIds.stream()
        //	.map(resourceId -> RoleMenu.builder()
        //		.roleId(roleId)
        //		.resourceId(resourceId)
        //		.build())
        //	.toList();
        // cr().saveAll(collect);

        return true;
    }
}
