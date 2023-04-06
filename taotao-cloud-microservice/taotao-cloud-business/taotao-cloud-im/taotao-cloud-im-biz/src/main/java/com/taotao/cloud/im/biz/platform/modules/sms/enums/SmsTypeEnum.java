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

package com.taotao.cloud.im.biz.platform.modules.sms.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/** 短信类型枚举 */
@Getter
public enum SmsTypeEnum {

    /** 注册 */
    REGISTERED("1", "chat:code:reg:", 5),
    /** 登录 */
    LOGIN("2", "chat:code:login:", 5),
    /** 忘记密码 */
    FORGET("3", "chat:code:forget:", 5),
    ;

    @EnumValue
    @JsonValue
    private String code;

    private String prefix;
    private Integer timeout;

    SmsTypeEnum(String code, String prefix, Integer timeout) {
        this.code = code;
        this.prefix = prefix;
        this.timeout = timeout;
    }
}
