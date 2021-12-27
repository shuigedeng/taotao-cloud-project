package com.taotao.cloud.stock.biz.domain.model.user;

import com.xtoon.cloud.sys.domain.model.role.RoleId;
import com.xtoon.cloud.sys.domain.model.tenant.TenantId;

import java.util.List;

/**
 * 用户工厂
 *
 * @author haoxin
 * @date 2021-02-24
 **/
public class UserFactory {

    private UserRepository userRepository;

    public UserFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(Mobile mobile, Email email, Password password, UserName userName, List<RoleId> roleIdList, TenantId currentTenantId) {
        List<User> users = userRepository.find(mobile);
        Account account;
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                if (user.getTenantId().sameValueAs(currentTenantId)) {
                    throw new RuntimeException("租户内账号已存在");
                }
            }
            account = users.get(0).getAccount();
        } else {
            account = new Account(mobile, email, password);
        }
        if (roleIdList == null || roleIdList.isEmpty()) {
            throw new RuntimeException("角色未分配");
        }
        return new User(userName, account, roleIdList);
    }

}
