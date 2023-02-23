package com.taotao.cloud.im.biz.platform.modules.topic.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 帖子回复类型枚举
 */
@Getter
public enum TopicReplyTypeEnum {

    /**
     * 帖子
     */
    TOPIC("1", "帖子"),
    /**
     * 用户
     */
    USER("2", "用户"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String name;

    TopicReplyTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
