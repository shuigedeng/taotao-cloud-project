package com.taotao.cloud.im.biz.platform.modules.topic.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 帖子类型枚举
 */
@Getter
public enum TopicTypeEnum {

    /**
     * 文字/表情
     */
    TEXT("TEXT", "文字/表情"),
    /**
     * 图片/拍照
     */
    IMAGE("IMAGE", "图片/拍照"),
    /**
     * 视频
     */
    VIDEO("VIDEO", "视频"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String name;

    TopicTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
