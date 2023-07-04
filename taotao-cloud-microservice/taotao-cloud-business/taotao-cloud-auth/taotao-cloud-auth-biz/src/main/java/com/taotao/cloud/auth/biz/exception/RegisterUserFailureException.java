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

/**
 * 注册用户失败
 */
public class RegisterUserFailureException extends AbstractResponseJsonAuthenticationException {

    private static final long serialVersionUID = 9180897671726519378L;

    public RegisterUserFailureException(ErrorCodeEnum errorCodeEnum, Throwable t, String userId) {
        super(errorCodeEnum, t, null, userId);
    }

    public RegisterUserFailureException(ErrorCodeEnum errorCodeEnum, String userId) {
        super(errorCodeEnum, null, userId);
    }
}
