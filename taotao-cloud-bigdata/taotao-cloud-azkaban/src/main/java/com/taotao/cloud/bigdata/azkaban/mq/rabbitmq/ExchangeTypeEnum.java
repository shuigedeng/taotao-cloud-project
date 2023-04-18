package com.taotao.cloud.bigdata.azkaban.mq.rabbitmq;

import lombok.Getter;

/**
 *  rabbitmq-交换机枚举
 */
public enum ExchangeTypeEnum {

    TOPIC("topic","主题-topic"),
    FANOUT("fanout","广播-fanout"),
    DIRECT("direct","点对点-direct"),
    DELAYED("x-delayed-message","延迟消息-delayed");

    @Getter
    private String value;
    @Getter
    private String desc;

    ExchangeTypeEnum(String value, String desc) {
        this.desc=desc;
        this.value = value;
    }

    /**
     *  获取topic
     * @param code
     * @return
     */
    public static String getTopic(Integer code) {
        for (ExchangeTypeEnum typeEnum : ExchangeTypeEnum.values()) {
            if (typeEnum.value.equals(code)) {
                return typeEnum.value;
            }
        }
        return null;
    }

}
