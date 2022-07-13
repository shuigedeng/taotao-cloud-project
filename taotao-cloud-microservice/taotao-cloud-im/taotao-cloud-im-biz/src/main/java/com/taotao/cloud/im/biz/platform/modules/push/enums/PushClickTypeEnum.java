package com.taotao.cloud.im.biz.platform.modules.push.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 推送点击类型枚举
 */
@Getter
public enum PushClickTypeEnum {

    /**
     * 打开应用内特定页面
     */
    INTENT("intent", "打开应用内特定页面"),
    /**
     * 打开网页地址
     */
    URL("url", "打开网页地址"),
    /**
     * 自定义消息内容启动应用
     */
    PAYLOAD("payload", "自定义消息内容启动应用"),
    /**
     * 自定义消息内容不启动应用
     */
    PAYLOAD_CUSTOM("payload_custom", "自定义消息内容不启动应用"),
    /**
     * 打开应用首页
     */
    START_APP("startapp", "打开应用首页"),
    /**
     * 纯通知，无后续动作
     */
    NONE("none", "纯通知，无后续动作"),
    ;

    @JsonValue
    private String code;
    private String info;

    PushClickTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
