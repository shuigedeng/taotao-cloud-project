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
 * 用户应用服务实现类
 *
 * @author shuigedeng
 * @since 2021-02-09
 */
@Service
public class UserApplicationServiceImpl implements UserApplicationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserCommand userCommand) {
        List<RoleId> roleIdList = new ArrayList<>();
        if (userCommand.getRoleIdList() != null) {
            userCommand.getRoleIdList().forEach(roleId -> {
                roleIdList.add(new RoleId(roleId));
            });
        }
        UserFactory userFactory = new UserFactory(userRepository);
        User user = userFactory.createUser(
                new Mobile(userCommand.getMobile()),
                new Email(userCommand.getEmail()),
                Password.create(Password.DEFAULT),
                new UserName(userCommand.getUserName()),
                roleIdList,
                new TenantId(TenantContext.getTenantId()));
        userRepository.store(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserCommand userCommand) {
        userRepository.store(UserDTOAssembler.toUser(userCommand));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<String> ids) {
        List<UserId> userIds = new ArrayList<>();
        ids.forEach(id -> {
            userIds.add(new UserId(id));
        });
        UserUpdateSpecification userUpdateSpecification = new UserUpdateSpecification(tenantRepository);
        for (UserId userId : userIds) {
            User user = userRepository.find(userId);
            userUpdateSpecification.isSatisfiedBy(user);
        }
        userRepository.remove(userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String id) {
        User user = userRepository.find(new UserId(id));
        UserUpdateSpecification userUpdateSpecification = new UserUpdateSpecification(tenantRepository);
        userUpdateSpecification.isSatisfiedBy(user);
        user.disable();
        userRepository.store(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(PasswordCommand passwordCommand) {
        User user = userRepository.find(new UserId(passwordCommand.getUserId()));
        user.changePassword(passwordCommand.getPassword(), passwordCommand.getNewPassword());
        userRepository.store(user);
    }
}
