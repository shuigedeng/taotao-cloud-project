package com.taotao.cloud.member.api.enums;

/**
 * 发票类型
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:37:15
 */
public enum MemberReceiptEnum {

    /**
     * 发票类型
     */
    ELECTRONIC_INVOICE("电子发票"),
    ORDINARY_INVOICE("普通发票");

    private final String description;

    MemberReceiptEnum(String str) {
        this.description = str;
    }

    public String description() {
        return description;
    }

}
