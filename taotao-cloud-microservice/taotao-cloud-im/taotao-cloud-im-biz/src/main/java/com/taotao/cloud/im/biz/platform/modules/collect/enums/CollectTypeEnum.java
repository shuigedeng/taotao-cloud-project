package com.taotao.cloud.im.biz.platform.modules.collect.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 收藏类型
 */
@Getter
public enum CollectTypeEnum {

    /**
     * 文字/表情
     */
    TEXT("TEXT", "文字/表情"),
    /**
     * 图片/拍照
     */
    IMAGE("IMAGE", "图片/拍照"),
    /**
     * 声音
     */
    VOICE("VOICE", "声音"),
    /**
     * 视频
     */
    VIDEO("VIDEO", "视频"),
    /**
     * 位置
     */
    LOCATION("LOCATION", "位置"),
    /**
     * 名片
     */
    CARD("CARD", "名片"),
    /**
     * 文件
     */
    FILE("FILE", "文件"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    CollectTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
