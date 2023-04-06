/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.im.biz.platform.modules.collect.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/** 收藏类型 */
@Getter
public enum CollectTypeEnum {

    /** 文字/表情 */
    TEXT("TEXT", "文字/表情"),
    /** 图片/拍照 */
    IMAGE("IMAGE", "图片/拍照"),
    /** 声音 */
    VOICE("VOICE", "声音"),
    /** 视频 */
    VIDEO("VIDEO", "视频"),
    /** 位置 */
    LOCATION("LOCATION", "位置"),
    /** 名片 */
    CARD("CARD", "名片"),
    /** 文件 */
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
