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

package com.taotao.cloud.cache.api;

/**
 * 慢日志监听器上下文
 *
 * @author shuigedeng
 * @since 2024.06
 */
public interface ICacheSlowListenerContext {

    /**
     * 方法名称
     * @return 方法名称
     * @since 2024.06
     */
    String methodName();

    /**
     * 参数信息
     * @return 参数列表
     * @since 2024.06
     */
    Object[] params();

    /**
     * 方法结果
     * @return 方法结果
     * @since 2024.06
     */
    Object result();

    /**
     * 开始时间
     * @return 时间
     * @since 2024.06
     */
    long startTimeMills();

    /**
     * 结束时间
     * @return 结束时间
     * @since 2024.06
     */
    long endTimeMills();

    /**
     * 消耗时间
     * @return 耗时
     * @since 2024.06
     */
    long costTimeMills();
}
