package com.taotao.cloud.stock.biz.domain.model.user;

import com.xtoon.cloud.common.core.domain.Entity;

/**
 * 账号
 *
 * @author haoxin
 * @date 2021-02-21
 **/
public class Account implements Entity<Account> {

    /**
     * accountId
     */
    private AccountId accountId;

    /**
     * 手机号
     */
    private Mobile mobile;

    /**
     * 邮箱
     */
    private Email email;

    /**
     * 密码
     */
    private Password password;


    public Account(AccountId accountId, Mobile mobile, Email email, Password password) {
        this.accountId = accountId;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
    }

    public Account(Mobile mobile, String password) {
        this.mobile = mobile;
        this.password = Password.create(password);
    }

    public Account(Mobile mobile, Email email, Password password) {
        this.mobile = mobile;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean sameIdentityAs(Account other) {
        return other != null && accountId.sameValueAs(other.accountId);
    }

    /**
     * 密码是否正确
     *
     * @param passwordStr
     * @return
     */
    public boolean checkPassword(String passwordStr) {
        return password != null && this.password.sameValueAs(Password.create(passwordStr));
    }

    /**
     * 修改密码
     *
     * @param oldPasswordStr
     * @param newPasswordStr
     * @return
     */
    public void changePassword(String oldPasswordStr, String newPasswordStr) {
        if (!checkPassword(oldPasswordStr)) {
            throw new RuntimeException("原密码不正确");
        }
        this.password = Password.create(newPasswordStr);
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public Mobile getMobile() {
        return mobile;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }
}
