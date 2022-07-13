package com.taotao.cloud.im.biz.platform.modules.push.enums;

import lombok.Getter;

/**
 * 推送消息类型
 */
@Getter
public enum PushBodyTypeEnum {

    /**
     * 普通消息
     */
    MSG("MSG", "普通消息"),
    /**
     * 通知消息
     */
    NOTICE("NOTICE", "通知消息"),
    /**
     * 大消息
     */
    BIG("BIG", "大消息"),
    ;

    private String code;
    private String info;

    PushBodyTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
