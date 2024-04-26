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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.exception;

/**
 * <p>接入预操作失败错误 </p>
 *
 *
 * @since : 2022/1/26 11:10
 */
public class AccessPreProcessFailedException extends RuntimeException {

    public AccessPreProcessFailedException() {}

    public AccessPreProcessFailedException(String message) {
        super(message);
    }

    public AccessPreProcessFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessPreProcessFailedException(Throwable cause) {
        super(cause);
    }

    public AccessPreProcessFailedException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
