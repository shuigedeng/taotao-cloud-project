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

package com.taotao.cloud.wechat.api.mp.enums.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 公众号消息自动回复的匹配模式 */
@Getter
@AllArgsConstructor
public enum MpAutoReplyMatchEnum {
    ALL(1, "完全匹配"),
    LIKE(2, "半匹配"),
    ;

    /** 匹配 */
    private final Integer match;
    /** 匹配的名字 */
    private final String name;
}
