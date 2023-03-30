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

package com.taotao.cloud.auth.biz.demo.core.exception;

import cn.herodotus.engine.assistant.core.definition.exception.HerodotusException;
import cn.herodotus.engine.assistant.core.domain.Feedback;
import cn.herodotus.engine.assistant.core.domain.Result;
import org.springframework.security.core.AuthenticationException;

/**
 * Description: 平台认证基础Exception
 *
 * @author : gengwei.zheng
 * @date : 2021/10/16 14:41
 */
public class PlatformAuthenticationException extends AuthenticationException
        implements HerodotusException {

    public PlatformAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PlatformAuthenticationException(String msg) {
        super(msg);
    }

    @Override
    public Feedback getFeedback() {
        return Feedback.ERROR;
    }

    @Override
    public Result<String> getResult() {
        Result<String> result = Result.failure();
        result.code(getFeedback().getCode());
        result.message(getFeedback().getMessage());
        result.status(getFeedback().getStatus());
        result.stackTrace(super.getStackTrace());
        result.detail(super.getMessage());
        return result;
    }
}
