package com.taotao.cloud.stock.biz.domain.user.model.factory;

import com.taotao.cloud.stock.biz.domain.model.user.Account;
import com.taotao.cloud.stock.biz.domain.model.user.Email;
import com.taotao.cloud.stock.biz.domain.model.user.Mobile;
import com.taotao.cloud.stock.biz.domain.model.user.Password;
import com.taotao.cloud.stock.biz.domain.model.user.User;
import com.taotao.cloud.stock.biz.domain.model.user.UserName;
import com.taotao.cloud.stock.biz.domain.model.user.UserRepository;
import com.taotao.cloud.stock.biz.domain.user.model.entity.Account;
import com.taotao.cloud.stock.biz.domain.user.model.entity.User;
import com.taotao.cloud.stock.biz.domain.user.model.vo.Email;
import com.taotao.cloud.stock.biz.domain.user.model.vo.Mobile;
import com.taotao.cloud.stock.biz.domain.user.model.vo.Password;
import com.taotao.cloud.stock.biz.domain.user.model.vo.UserName;
import com.taotao.cloud.stock.biz.domain.user.repository.UserRepository;
import com.xtoon.cloud.sys.domain.model.role.RoleId;
import com.xtoon.cloud.sys.domain.model.tenant.TenantId;

import java.util.List;

/**
 * 用户工厂
 *
 * @author shuigedeng
 * @date 2021-02-24
 */
public class UserFactory {

    private UserRepository userRepository;

    public UserFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public com.taotao.cloud.stock.biz.domain.model.user.User createUser(Mobile mobile, Email email, Password password, UserName userName, List<RoleId> roleIdList, TenantId currentTenantId) {
        List<com.taotao.cloud.stock.biz.domain.model.user.User> users = userRepository.find(mobile);
        com.taotao.cloud.stock.biz.domain.model.user.Account account;
        if (users != null && !users.isEmpty()) {
            for (com.taotao.cloud.stock.biz.domain.model.user.User user : users) {
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
