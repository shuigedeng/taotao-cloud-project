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

package com.taotao.cloud.stock.biz.application.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色应用服务实现类
 *
 * @author shuigedeng
 * @since 2021-02-18
 */
@Service
public class RoleApplicationServiceImpl implements RoleApplicationService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(RoleCommand roleCommand) {
        Role role = RoleDTOAssembler.toRole(roleCommand);
        RoleCreateSpecification roleCreateSpecification = new RoleCreateSpecification(roleRepository);
        roleCreateSpecification.isSatisfiedBy(role);
        roleRepository.store(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<String> ids) {
        RoleUpdateSpecification roleUpdateSpecification = new RoleUpdateSpecification();
        List<RoleId> roleIds = new ArrayList<>();
        ids.forEach(id -> {
            Role role = roleRepository.find(new RoleId(id));
            roleUpdateSpecification.isSatisfiedBy(role);
            roleIds.add(new RoleId(id));
        });
        roleRepository.remove(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String id) {
        Role role = roleRepository.find(new RoleId(id));
        RoleUpdateSpecification roleUpdateSpecification = new RoleUpdateSpecification();
        roleUpdateSpecification.isSatisfiedBy(role);
        role.disable();
        roleRepository.store(role);
    }
}
