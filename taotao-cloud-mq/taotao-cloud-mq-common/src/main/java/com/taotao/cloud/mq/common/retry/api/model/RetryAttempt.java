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

package com.taotao.cloud.mq.common.retry.api.model;

import java.util.List;

/**
 * 重试信息接口
 * @author shuigedeng
 * @since 0.0.1
 */
public interface RetryAttempt<R> {

    /**
     * 获取方法执行的结果
     * @return 执行的结果
     */
    R result();

    /**
     * 当前尝试次数
     * @return 次数
     */
    int attempt();

    /**
     * 异常信息
     * @return 异常信息
     */
    Throwable cause();

    /**
     * 消耗时间
     * @return 消耗时间
     */
    AttemptTime time();

    /**
     * 重试的历史信息
     * @return 重试的历史列表
     */
    List<RetryAttempt<R>> history();

    /**
     * 请求参数
     * @return 请求参数
     * @since 0.1.0
     */
    Object[] params();
}
