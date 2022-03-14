package com.taotao.cloud.member.api.enums;

/**
 * 预存款变动日志业务类型
 */

public enum DepositServiceTypeEnum {
    /**
     * 预存款变动日志业务类型枚举
     */
    WALLET_WITHDRAWAL("余额提现"),
    WALLET_PAY("余额支付"),
    WALLET_REFUND("余额退款"),
    WALLET_RECHARGE("余额充值"),
    WALLET_COMMISSION("佣金提成");

    private final String description;

    DepositServiceTypeEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
