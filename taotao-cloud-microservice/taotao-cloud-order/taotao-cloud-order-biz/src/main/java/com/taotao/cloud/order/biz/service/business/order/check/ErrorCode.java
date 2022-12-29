package com.taotao.cloud.order.biz.service.business.order.check;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /**
     * 公共
     */
    SUCCESS("0000", "操作成功"),
    FAIL("0001", "失败"),
    ERROR("0002", "异常"),
    PARAM_NULL_ERROR("0003", "参数为空"),
    PARAM_SKU_NULL_ERROR("0004", "SKU参数为空"),
    PARAM_PRICE_NULL_ERROR("0005", "价格参数为空"),
    PARAM_STOCK_NULL_ERROR("0006", "库存参数为空"),
    PARAM_PRICE_ILLEGAL_ERROR("0007", "不合法的价格参数"),
    PARAM_STOCK_ILLEGAL_ERROR("0008", "不合法的库存参数"),
    ;

    private String code;

    private String desc;

    ErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
