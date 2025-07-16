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

package com.taotao.cloud.mq.common.support.status;

/**
 * 状态管理
 * @since 2024.05
 */
public interface IStatusManager {

    /**
     * 获取状态编码
     * @return 状态编码
     * @since 2024.05
     */
    boolean status();

    /**
     * 设置状态编码
     * @param status 编码
     * @return this
     * @since 2024.05
     */
    IStatusManager status(final boolean status);

    /**
     * 初始化失败
     * @return 初始化失败
     * @since 2024.05
     */
    boolean initFailed();

    /**
     * 设置初始化失败
     * @param failed 编码
     * @return this
     * @since 2024.05
     */
    IStatusManager initFailed(final boolean failed);
}
