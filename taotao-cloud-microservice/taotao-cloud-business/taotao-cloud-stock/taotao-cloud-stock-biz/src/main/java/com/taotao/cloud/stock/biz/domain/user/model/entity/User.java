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

package com.taotao.cloud.stock.biz.domain.user.model.entity;

import com.taotao.cloud.stock.biz.domain.model.user.Account;
import com.taotao.cloud.stock.biz.domain.model.user.UserId;
import com.taotao.cloud.stock.biz.domain.model.user.UserName;
import com.taotao.cloud.stock.biz.domain.user.model.vo.UserId;
import com.taotao.cloud.stock.biz.domain.user.model.vo.UserName;
import java.util.List;

/**
 * 用户
 *
 * @author shuigedeng
 * @since 2021-02-02
 */
public class User implements Entity<User> {

    /** UserId */
    private UserId userId;

    /** 用户名 */
    private UserName userName;

    /** 状态 */
    private StatusEnum status;

    /** 账号 */
    private Account account;

    /** 当前租户 */
    private TenantId tenantId;

    /** 角色Id列表 */
    private List<RoleId> roleIds;

    public User(
            UserId userId,
            UserName userName,
            StatusEnum status,
            Account account,
            TenantId tenantId,
            List<RoleId> roleIds) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.account = account;
        this.tenantId = tenantId;
        this.roleIds = roleIds;
    }

    public User(UserName userName, Account account, List<RoleId> roleIds) {
        this.userName = userName;
        this.account = account;
        this.roleIds = roleIds;
        this.status = StatusEnum.ENABLE;
    }

    /**
     * 是否有效
     *
     * @return
     */
    public boolean isEnable() {
        return status == StatusEnum.ENABLE;
    }

    @Override
    public boolean sameIdentityAs(User other) {
        return other != null && userId.sameValueAs(other.userId);
    }

    /** 禁用 */
    public void disable() {
        StatusEnum status = this.status == StatusEnum.DISABLE ? StatusEnum.ENABLE : StatusEnum.DISABLE;
        this.status = status;
    }

    public void changePassword(String oldPasswordStr, String newPasswordStr) {
        account.changePassword(oldPasswordStr, newPasswordStr);
    }

    public UserId getUserId() {
        return userId;
    }

    public UserName getUserName() {
        return userName;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public Account getAccount() {
        return account;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public List<RoleId> getRoleIds() {
        return roleIds;
    }
}
