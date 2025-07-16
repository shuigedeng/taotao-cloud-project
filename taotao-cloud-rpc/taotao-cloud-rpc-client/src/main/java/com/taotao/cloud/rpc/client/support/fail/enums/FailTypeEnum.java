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

package com.taotao.cloud.rpc.client.support.fail.enums;

/**
 * 失败类型枚举
 * @author shuigedeng
 * @since 0.1.1
 */
public enum FailTypeEnum {
    /**
     * 快速失败
     * @since 0.1.1
     */
    FAIL_FAST(1),
    /**
     * 失败重试
     * 选择另外一个 channel 进行重试
     * @since 0.1.1
     */
    FAIL_OVER(2),
    /**
     * 失败之后不进行报错，直接返回
     * @since 0.1.1
     */
    FAIL_SAFE(3),
    ;

    private final int code;

    FailTypeEnum(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    @Override
    public String toString() {
        return "FailTypeEnum{" + "code=" + code + '}';
    }
}
