package com.taotao.cloud.mq.client.producer.constant;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public enum SendStatus {
    SUCCESS("SUCCESS", "发送成功"),
    FAILED("FAILED", "发送失败"),
    ;

    private final String code;
    private final String desc;

    SendStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
