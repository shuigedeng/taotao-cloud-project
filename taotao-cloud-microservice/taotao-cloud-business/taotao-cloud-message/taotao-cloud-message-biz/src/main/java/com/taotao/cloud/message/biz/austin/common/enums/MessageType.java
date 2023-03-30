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

package com.taotao.cloud.message.biz.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 发送的消息类型
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum MessageType {

    /** 通知类消息 */
    NOTICE(10, "通知类消息", "notice"),
    /** 营销类消息 */
    MARKETING(20, "营销类消息", "marketing"),
    /** 验证码消息 */
    AUTH_CODE(30, "验证码消息", "auth_code");

    /** 编码值 */
    private final Integer code;

    /** 描述 */
    private final String description;

    /** 英文标识 */
    private final String codeEn;

    /**
     * 通过code获取enum
     *
     * @param code
     * @return
     */
    public static MessageType getEnumByCode(Integer code) {
        MessageType[] values = values();
        for (MessageType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
