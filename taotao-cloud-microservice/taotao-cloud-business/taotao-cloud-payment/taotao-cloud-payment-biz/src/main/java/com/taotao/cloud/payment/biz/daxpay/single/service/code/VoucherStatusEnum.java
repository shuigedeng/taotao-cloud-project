package com.taotao.cloud.payment.biz.daxpay.single.service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 储值卡状态
 * @author xxm
 * @since 2024/2/17
 */
@Getter
@AllArgsConstructor
public enum VoucherStatusEnum {

    NORMAL("normal", "正常"),
    FORBIDDEN("forbidden", "禁用");

    private final String code;
    private final String name;
}
