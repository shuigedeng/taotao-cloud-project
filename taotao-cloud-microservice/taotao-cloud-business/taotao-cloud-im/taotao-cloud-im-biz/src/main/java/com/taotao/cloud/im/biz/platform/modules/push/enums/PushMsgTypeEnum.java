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

package com.taotao.cloud.im.biz.platform.modules.push.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/** 消息类型枚举 */
@Getter
public enum PushMsgTypeEnum {

    /** 通知 */
    ALERT("ALERT", "通知"),
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
    /** 收藏 */
    COLLECTION("COLLECTION", "收藏"),
    /** 名片 */
    CARD("CARD", "名片"),
    /** 文件 */
    FILE("FILE", "文件"),
    /** 实时语音开始 */
    TRTC_VOICE_START("TRTC_VOICE_START", "实时语音开始"),
    /** 实时语音结束 */
    TRTC_VOICE_END("TRTC_VOICE_END", "实时语音结束"),
    /** 实时视频开始 */
    TRTC_VIDEO_START("TRTC_VIDEO_START", "实时视频开始"),
    /** 实时视频结束 */
    TRTC_VIDEO_END("TRTC_VIDEO_END", "实时视频结束"),
    ;

    @EnumValue
    @JsonValue
    private String code;

    private String info;

    PushMsgTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }
}
