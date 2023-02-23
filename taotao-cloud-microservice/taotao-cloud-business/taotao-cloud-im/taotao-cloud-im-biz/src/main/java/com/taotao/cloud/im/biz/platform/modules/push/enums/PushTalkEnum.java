package com.taotao.cloud.im.biz.platform.modules.push.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 消息聊天枚举
 */
@Getter
public enum PushTalkEnum {

    /**
     * 单聊
     */
    SINGLE("SINGLE", "单聊"),
    /**
     * 群聊
     */
    GROUP("GROUP", "群聊"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    PushTalkEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
