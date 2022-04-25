package com.taotao.cloud.member.api.enums;

/**
 * 发送类型
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:37:39
 */
public enum SendTypeEnum {

    /**
     * 消息类型
     */
    ALL("全部"),
    SELECT("指定会员");

    private String description;

    SendTypeEnum(String str) {
        this.description = str;

    }

    public String description() {
        return description;
    }

}
