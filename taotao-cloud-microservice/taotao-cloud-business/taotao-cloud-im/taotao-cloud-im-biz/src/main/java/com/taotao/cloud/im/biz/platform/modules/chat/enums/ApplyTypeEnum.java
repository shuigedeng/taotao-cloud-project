package com.taotao.cloud.im.biz.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 申请类型
 */
@Getter
public enum ApplyTypeEnum {

    /**
     * 好友
     */
    FRIEND("1", "好友"),
    /**
     * 群组
     */
    GROUP("2", "群组"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    ApplyTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
