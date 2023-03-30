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
 * 屏蔽类型
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum ShieldType {

    /** 模板设置为夜间不屏蔽 */
    NIGHT_NO_SHIELD(10, "夜间不屏蔽"),
    /** 模板设置为夜间屏蔽 -- 凌晨接受到的消息会过滤掉 */
    NIGHT_SHIELD(20, "夜间屏蔽"),
    /** 模板设置为夜间屏蔽(次日早上9点发送) -- 凌晨接受到的消息会次日发送 */
    NIGHT_SHIELD_BUT_NEXT_DAY_SEND(30, "夜间屏蔽(次日早上9点发送)");

    private final Integer code;
    private final String description;
}
