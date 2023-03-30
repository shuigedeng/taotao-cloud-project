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
 * 短信状态信息
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum SmsStatus {

    /** 调用渠道接口发送成功 */
    SEND_SUCCESS(10, "调用渠道接口发送成功"),
    /** 用户收到短信(收到渠道短信回执，状态成功) */
    RECEIVE_SUCCESS(20, "用户收到短信(收到渠道短信回执，状态成功)"),
    /** 用户收不到短信(收到渠道短信回执，状态失败) */
    RECEIVE_FAIL(30, "用户收不到短信(收到渠道短信回执，状态失败)"),
    /** 调用渠道接口发送失败 */
    SEND_FAIL(40, "调用渠道接口发送失败");

    private final Integer code;
    private final String description;

    /**
     * 根据状态获取描述信息
     *
     * @param code
     * @return
     */
    public static String getDescriptionByStatus(Integer code) {
        for (SmsStatus value : SmsStatus.values()) {
            if (value.getCode().equals(code)) {
                return value.getDescription();
            }
        }
        return "";
    }
}
