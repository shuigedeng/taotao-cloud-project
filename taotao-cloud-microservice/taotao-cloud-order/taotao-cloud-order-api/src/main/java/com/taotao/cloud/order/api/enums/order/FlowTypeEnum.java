package com.taotao.cloud.order.api.enums.order;


/**
 * 流水类型枚举
 *
 * 
 * @since 2020/11/17 7:25 下午
 */
public enum FlowTypeEnum {

    /**
     * 流水类型
     */
    PAY("支付"),
    REFUND("退款");

    private final String description;

    FlowTypeEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
