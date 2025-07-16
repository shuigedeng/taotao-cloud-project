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

package com.taotao.cloud.auth.biz.exception;

import com.taotao.cloud.auth.biz.uaa.enums.ErrorCodeEnum;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * 第三方授权登录异常
 */
public class Auth2Exception extends AuthenticationException {

    @Getter private final ErrorCodeEnum errorCodeEnum;

    @Getter private final Object data;

    public Auth2Exception(ErrorCodeEnum errorCodeEnum, Object data) {
        super(errorCodeEnum.getMsg());
        this.errorCodeEnum = errorCodeEnum;
        this.data = data;
    }

    public Auth2Exception(ErrorCodeEnum errorCodeEnum, Object data, Throwable cause) {
        super(errorCodeEnum.getMsg(), cause);
        this.errorCodeEnum = errorCodeEnum;
        this.data = data;
    }
}
