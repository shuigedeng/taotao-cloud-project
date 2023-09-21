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
import java.util.List;

/**
 * 用户工厂
 *
 * @author shuigedeng
 * @since 2021-02-24
 */
public class UserFactory {

    private UserRepository userRepository;

    public UserFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public com.taotao.cloud.stock.biz.domain.model.user.User createUser(
            Mobile mobile,
            Email email,
            Password password,
            UserName userName,
            List<RoleId> roleIdList,
            TenantId currentTenantId) {
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
