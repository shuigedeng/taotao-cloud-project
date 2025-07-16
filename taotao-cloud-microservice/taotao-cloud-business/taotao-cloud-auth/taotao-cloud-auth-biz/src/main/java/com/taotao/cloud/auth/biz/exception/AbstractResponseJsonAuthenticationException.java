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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * 继承此类后, 如果异常被 {@link SimpleUrlAuthenticationFailureHandler} 处理 Response 会返回 Json 数据
 */
public abstract class AbstractResponseJsonAuthenticationException extends AuthenticationException {

    private static final long serialVersionUID = 2661098918363948470L;

    @Getter protected ErrorCodeEnum errorCodeEnum;

    @Getter protected Object data;

    /**
     * 可以是用户名, userId, sessionId 等表示用户唯一的属性
     */
    @Getter protected String uid;

    public AbstractResponseJsonAuthenticationException(
            ErrorCodeEnum errorCodeEnum, Throwable t, Object data, String uid) {
        super(errorCodeEnum.getMsg(), t);
        this.errorCodeEnum = errorCodeEnum;
        this.data = data;
        this.uid = uid;
    }

    public AbstractResponseJsonAuthenticationException(
            ErrorCodeEnum errorCodeEnum, Object data, String uid) {
        super(errorCodeEnum.getMsg());
        this.errorCodeEnum = errorCodeEnum;
        this.data = data;
        this.uid = uid;
    }
}
