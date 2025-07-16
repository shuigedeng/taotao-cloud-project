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

package com.taotao.cloud.rpc.registry.domain.message;

import com.taotao.cloud.rpc.common.common.rpc.domain.BaseRpc;

/**
 * 注册消息体
 * @author shuigedeng
 * @since 2024.06
 */
public interface NotifyMessage extends BaseRpc {

    /**
     * 头信息
     * @return 头信息
     * @since 2024.06
     */
    NotifyMessageHeader header();

    /**
     * 消息信息体
     * @return 消息信息体
     * @since 2024.06
     */
    Object body();
}
