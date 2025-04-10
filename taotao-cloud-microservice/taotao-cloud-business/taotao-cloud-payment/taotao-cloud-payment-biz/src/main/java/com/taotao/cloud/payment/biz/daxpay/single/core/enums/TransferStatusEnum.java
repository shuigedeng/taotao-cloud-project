package com.taotao.cloud.payment.biz.daxpay.single.core.enums;

import lombok.*;
import lombok.Getter;

/**
 * 转账状态
 * 字典: transfer_status
 * @author xxm
 * @since 2024/6/11
 */
@Getter
@AllArgsConstructor
public enum TransferStatusEnum {

    /** 转账中 */
    PROGRESS("progress"),
    /** 转账成功 */
    SUCCESS("success"),
    /** 转账关闭 */
    CLOSE("close"),
    /** 转账失败 */
    FAIL("fail"),
    ;

    private final String code;

}
