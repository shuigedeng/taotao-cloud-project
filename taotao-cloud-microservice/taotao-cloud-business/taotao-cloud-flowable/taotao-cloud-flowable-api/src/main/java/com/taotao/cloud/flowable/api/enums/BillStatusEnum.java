package com.taotao.cloud.flowable.api.enums;

/**
 * 结算单状态
 *
 *
 */
public enum BillStatusEnum {

    /**
     * "已出账"
     */
    OUT("已出账"),
    /**
     * "已核对"
     */
    CHECK("已核对"),
    /**
     * "已完成"
     */
    COMPLETE("已完成");
    private final String description;

    BillStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
