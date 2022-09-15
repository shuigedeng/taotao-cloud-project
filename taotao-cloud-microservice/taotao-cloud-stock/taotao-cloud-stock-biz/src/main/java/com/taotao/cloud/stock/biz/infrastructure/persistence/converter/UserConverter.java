package com.taotao.cloud.stock.biz.infrastructure.persistence.converter;


import java.util.List;

/**
 * 用户Converter
 *
 * @author shuigedeng
 * @date 2021-02-10
 */
public class UserConverter {

    public static User toUser(SysUserDO sysUserDO, Account account, List<RoleId> roleIdList) {
        if (sysUserDO == null) {
            return null;
        }
        User user = new User(new UserId(sysUserDO.getId()), new UserName(sysUserDO.getUserName()), StatusEnum.getStatusEnum(sysUserDO.getStatus()), account, new TenantId(sysUserDO.getTenantId()), roleIdList);
        return user;
    }

    public static SysUserDO fromUser(User user, String accountId) {
        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setId(user.getUserId() == null ? null : user.getUserId().getId());
        sysUserDO.setUserName(user.getUserName() == null ? null : user.getUserName().getName());
        sysUserDO.setStatus(user.getStatus() == null ? null : user.getStatus().getValue());
        sysUserDO.setAccountId(accountId);
        return sysUserDO;
    }

    public static SysAccountDO getSysAccountDO(User user) {
        SysAccountDO sysAccountDO = new SysAccountDO();
        Account account = user.getAccount();
        if (account == null) {
            return null;
        }
        sysAccountDO.setId(account.getAccountId() == null ? null : account.getAccountId().getId());
        sysAccountDO.setEmail(account.getEmail() == null ? null : account.getEmail().getEmail());
        sysAccountDO.setMobile(account.getMobile() == null ? null : account.getMobile().getMobile());
        sysAccountDO.setPassword(account.getPassword() == null ? null : account.getPassword().getPassword());
        return sysAccountDO;
    }

    public static Account toAccount(SysAccountDO sysAccountDO) {
        if (sysAccountDO == null) {
            return null;
        }
        Mobile mobile = null;
        if (sysAccountDO.getMobile() != null) {
            mobile = new Mobile(sysAccountDO.getMobile());
        }
        Email email = null;
        if (sysAccountDO.getEmail() != null) {
            email = new Email(sysAccountDO.getEmail());
        }
        Password password = null;
        if (sysAccountDO.getPassword() != null) {
            password = new Password(sysAccountDO.getPassword());
        }

        Account account = new Account(new AccountId(sysAccountDO.getId()), mobile, email, password);
        return account;
    }
}
