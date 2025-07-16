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

package com.taotao.cloud.mq.common.resp;

import com.taotao.cloud.mq.common.dto.RespCode;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqException extends RuntimeException implements RespCode {

    private final RespCode respCode;

    public MqException(RespCode respCode) {
        this.respCode = respCode;
    }

    public MqException(String message, RespCode respCode) {
        super(message);
        this.respCode = respCode;
    }

    public MqException(String message, Throwable cause, RespCode respCode) {
        super(message, cause);
        this.respCode = respCode;
    }

    public MqException(Throwable cause, RespCode respCode) {
        super(cause);
        this.respCode = respCode;
    }

    public MqException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace,
            RespCode respCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.respCode = respCode;
    }

    @Override
    public String getCode() {
        return this.respCode.getCode();
    }

    @Override
    public String getMsg() {
        return this.respCode.getMsg();
    }
}
