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
public enum PushNoticeTypeEnum {

    /** 帖子_小红点 */
    TOPIC_RED("TOPIC_RED", "帖子_小红点"),
    /** 帖子_回复 */
    TOPIC_REPLY("TOPIC_REPLY", "帖子_回复"),
    /** 好友_申请 */
    FRIEND_APPLY("FRIEND_APPLY", "好友_申请"),
    ;

    @EnumValue
    @JsonValue
    private String code;

    private String info;

    PushNoticeTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }
}
