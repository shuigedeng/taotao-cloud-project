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

/**
 * <p>超出最大数量限制 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:25:11
 */
public class MaximumLimitExceededException extends RuntimeException {

    /**
     * 最大限制超过异常
     *
     * @return
     * @since 2023-07-10 17:25:11
     */
    public MaximumLimitExceededException() {
        super();
    }

    /**
     * 最大限制超过异常
     *
     * @param message 消息
     * @return
     * @since 2023-07-10 17:25:11
     */
    public MaximumLimitExceededException(String message) {
        super(message);
    }

    /**
     * 最大限制超过异常
     *
     * @param message 消息
     * @param cause   原因
     * @return
     * @since 2023-07-10 17:25:12
     */
    public MaximumLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 最大限制超过异常
     *
     * @param cause 原因
     * @return
     * @since 2023-07-10 17:25:12
     */
    public MaximumLimitExceededException(Throwable cause) {
        super(cause);
    }

    /**
     * 最大限制超过异常
     *
     * @param message            消息
     * @param cause              原因
     * @param enableSuppression  启用抑制
     * @param writableStackTrace 可写堆栈跟踪
     * @return
     * @since 2023-07-10 17:25:12
     */
    protected MaximumLimitExceededException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
