package com.taotao.cloud.member.api.enums;

/**
 * 会员提现到哪里 枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:37:17
 */

public enum MemberWithdrawalDestinationEnum {
    /**
     * 提现目的地
     */
    WECHAT("微信账户"),
    WALLET("余额账户");

    private String description;

    MemberWithdrawalDestinationEnum(String str) {
        this.description = str;

    }

    public String description() {
        return description;
    }
}
