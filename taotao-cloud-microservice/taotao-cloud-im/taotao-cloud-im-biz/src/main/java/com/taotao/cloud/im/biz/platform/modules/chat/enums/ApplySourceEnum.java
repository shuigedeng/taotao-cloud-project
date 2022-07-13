package com.taotao.cloud.im.biz.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 好友来源
 */
@Getter
public enum ApplySourceEnum {

    /**
     * 扫一扫
     */
    SCAN("1", "扫一扫"),
    /**
     * 名片
     */
    CARD("2", "名片"),
    /**
     * 微聊号
     */
    CHAT_NO("3", "微聊号"),
    /**
     * 手机号
     */
    PHONE("4", "手机号"),
    /**
     * 摇一摇
     */
    SHAKE("5", "摇一摇"),
    /**
     * 系统
     */
    SYS("6", "系统"),
    /**
     * 群聊
     */
    GROUP("7", "群聊"),
    /**
     * 附近的人
     */
    NEAR("8", "附近的人"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    ApplySourceEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
