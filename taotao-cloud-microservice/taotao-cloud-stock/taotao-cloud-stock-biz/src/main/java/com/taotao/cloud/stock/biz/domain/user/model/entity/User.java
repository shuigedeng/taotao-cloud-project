package com.taotao.cloud.stock.biz.domain.user.model.entity;

import com.taotao.cloud.stock.biz.domain.model.user.Account;
import com.taotao.cloud.stock.biz.domain.model.user.UserId;
import com.taotao.cloud.stock.biz.domain.model.user.UserName;
import com.taotao.cloud.stock.biz.domain.user.model.vo.UserId;
import com.taotao.cloud.stock.biz.domain.user.model.vo.UserName;
import com.xtoon.cloud.common.core.domain.Entity;
import com.xtoon.cloud.common.core.domain.StatusEnum;
import com.xtoon.cloud.sys.domain.model.role.RoleId;
import com.xtoon.cloud.sys.domain.model.tenant.TenantId;

import java.util.List;

/**
 * 用户
 *
 * @author shuigedeng
 * @date 2021-02-02
 **/
public class User implements Entity<User> {

    /**
     * UserId
     */
    private UserId userId;

    /**
     * 用户名
     */
    private UserName userName;

    /**
     * 状态
     */
    private StatusEnum status;

    /**
     * 账号
     */
    private Account account;

    /**
     * 当前租户
     */
    private TenantId tenantId;

    /**
     * 角色Id列表
     */
    private List<RoleId> roleIds;


    public User(UserId userId, UserName userName, StatusEnum status, Account account, TenantId tenantId, List<RoleId> roleIds) {
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

    /**
     * 禁用
     */
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
