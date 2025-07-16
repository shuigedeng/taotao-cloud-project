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

package com.taotao.cloud.mq.broker.resp;

import com.taotao.cloud.mq.common.dto.RespCode;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public enum MqBrokerRespCode implements RespCode {
    B_NOT_SUPPORT_METHOD("B00001", "暂时不支持的方法类型"),

    P_REGISTER_VALID_FAILED("BP0001", "生产者注册验证失败"),
    P_REGISTER_CHANNEL_NOT_VALID("BP0002", "生产者 channel 不合法"),

    C_REGISTER_VALID_FAILED("BC0001", "消费者注册验证失败"),
    C_REGISTER_CHANNEL_NOT_VALID("BC0002", "消费者 channel 不合法"),
    ;

    private final String code;
    private final String msg;

    MqBrokerRespCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
