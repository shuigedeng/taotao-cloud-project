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

package com.taotao.cloud.rpc.common.common.constant.enums;

/**
 * 调用方式枚举
 * （1）调用方式，是一种非常固定的模式。所以使用枚举代替常量。
 * （2）在 api 中使用常量，避免二者产生依赖。
 * @author shuigedeng
 * @since 0.1.0
 */
public enum CallTypeEnum {

    /**
     * 单向调用：不关心调用的结果
     * @since 0.1.0
     */
    ONE_WAY(1),

    /**
     * 同步调用：最常用的调用方式，关心结果
     * @since 0.1.0
     */
    SYNC(2),

    /**
     * 异步调用：性能更高的调用方式，异步获取结果
     * @since 0.1.0
     */
    ASYNC(3),

    /**
     * 回调方式：通过 callback 处理结果信息
     * @since 0.1.0
     */
    CALLBACK(4),
    ;

    private final int code;

    CallTypeEnum(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    @Override
    public String toString() {
        return "CallTypeEnum{" + "code=" + code + '}';
    }
}
