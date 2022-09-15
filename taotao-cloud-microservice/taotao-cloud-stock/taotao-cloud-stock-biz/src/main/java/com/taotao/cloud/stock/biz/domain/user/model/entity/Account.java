package com.taotao.cloud.stock.biz.domain.user.model.entity;

import com.taotao.cloud.stock.biz.domain.model.user.AccountId;
import com.taotao.cloud.stock.biz.domain.model.user.Email;
import com.taotao.cloud.stock.biz.domain.model.user.Mobile;
import com.taotao.cloud.stock.biz.domain.model.user.Password;
import com.taotao.cloud.stock.biz.domain.user.model.vo.AccountId;
import com.taotao.cloud.stock.biz.domain.user.model.vo.Email;
import com.taotao.cloud.stock.biz.domain.user.model.vo.Mobile;
import com.taotao.cloud.stock.biz.domain.user.model.vo.Password;

/**
 * 账号
 *
 * @author shuigedeng
 * @date 2021-02-21
 */
public class Account implements Entity<Account> {

    /**
     * accountId
     */
    private AccountId accountId;

    /**
     * 手机号
     */
    private com.taotao.cloud.stock.biz.domain.model.user.Mobile mobile;

    /**
     * 邮箱
     */
    private com.taotao.cloud.stock.biz.domain.model.user.Email email;

    /**
     * 密码
     */
    private Password password;


    public Account(AccountId accountId, com.taotao.cloud.stock.biz.domain.model.user.Mobile mobile, com.taotao.cloud.stock.biz.domain.model.user.Email email, Password password) {
        this.accountId = accountId;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
    }

    public Account(com.taotao.cloud.stock.biz.domain.model.user.Mobile mobile, String password) {
        this.mobile = mobile;
        this.password = Password.create(password);
    }

    public Account(
		    com.taotao.cloud.stock.biz.domain.model.user.Mobile mobile, com.taotao.cloud.stock.biz.domain.model.user.Email email, Password password) {
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
