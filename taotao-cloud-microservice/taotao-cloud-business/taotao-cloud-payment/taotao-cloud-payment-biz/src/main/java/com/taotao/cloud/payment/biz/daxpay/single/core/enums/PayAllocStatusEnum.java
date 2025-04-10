package com.taotao.cloud.payment.biz.daxpay.single.core.enums;

import lombok.*;
import lombok.Getter;

/**
 * 支付订单的分账状态
 * 字典表：pay_alloc_status
 * @author xxm
 * @since 2024/4/16
 */
@Getter
@AllArgsConstructor
public enum PayAllocStatusEnum {
    WAITING("waiting", "待分账"),
    ALLOCATION("allocation", "已分账"),
    /** 部分通道不支持分账 */
    IGNORE("ignore", "忽略分账"),
    ;

    private final String code;
    private final String name;
}
