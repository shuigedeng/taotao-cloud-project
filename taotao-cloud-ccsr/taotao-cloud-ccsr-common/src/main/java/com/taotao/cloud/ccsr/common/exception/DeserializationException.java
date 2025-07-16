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

package com.taotao.cloud.ccsr.common.exception;

import java.io.Serial;

/**
 * @date 2025/3/14 22:44
 */
public class DeserializationException extends RuntimeException {

    @Serial private static final long serialVersionUID = -6123954847415409614L;

    private static final String MSG_FOR_SPECIFIED_CLASS =
            "Ccsr deserialize for class [%s] failed. ";

    private static final String ERROR_MSG_FOR_SPECIFIED_CLASS =
            "Ccsr deserialize for class [%s] failed, cause error[%s]. ";

    private Class<?> targetClass;

    public DeserializationException(final Throwable e) {
        super(e);
    }

    public DeserializationException(final String message) {
        super(message);
    }

    public DeserializationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public DeserializationException(Class<?> targetClass, Throwable throwable) {
        super(
                String.format(
                        ERROR_MSG_FOR_SPECIFIED_CLASS,
                        targetClass.getName(),
                        throwable.getMessage()),
                throwable);
        this.targetClass = targetClass;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }
}
