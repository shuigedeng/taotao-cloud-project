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

package com.taotao.cloud.message.api.enums;

/** 消息状态枚举 */
public enum MessageStatusEnum {

    // 未读消息
    UN_READY("未读消息"),
    // 已读消息
    ALREADY_READY("已读消息"),
    // 回收站
    ALREADY_REMOVE("回收站");

    private final String description;

    MessageStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
