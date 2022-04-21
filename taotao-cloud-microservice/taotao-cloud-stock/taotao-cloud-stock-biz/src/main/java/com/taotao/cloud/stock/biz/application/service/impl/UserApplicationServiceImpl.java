package com.taotao.cloud.stock.biz.application.service.impl;

import com.xtoon.cloud.common.core.util.TenantContext;
import com.xtoon.cloud.sys.application.UserApplicationService;
import com.xtoon.cloud.sys.application.assembler.UserDTOAssembler;
import com.xtoon.cloud.sys.application.command.PasswordCommand;
import com.xtoon.cloud.sys.application.command.UserCommand;
import com.xtoon.cloud.sys.domain.model.role.RoleId;
import com.xtoon.cloud.sys.domain.model.tenant.TenantId;
import com.xtoon.cloud.sys.domain.model.tenant.TenantRepository;
import com.xtoon.cloud.sys.domain.model.user.*;
import com.xtoon.cloud.sys.domain.specification.UserUpdateSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户应用服务实现类
 *
 * @author shuigedeng
 * @date 2021-02-09
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
        User user = userFactory.createUser(new Mobile(userCommand.getMobile()), new Email(userCommand.getEmail()), Password.create(Password.DEFAULT),
                new UserName(userCommand.getUserName()), roleIdList, new TenantId(TenantContext.getTenantId()));
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
