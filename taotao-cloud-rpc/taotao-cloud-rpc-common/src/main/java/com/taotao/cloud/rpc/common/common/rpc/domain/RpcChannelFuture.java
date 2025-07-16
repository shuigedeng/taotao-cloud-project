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

package com.taotao.cloud.rpc.common.common.rpc.domain;

import com.taotao.cloud.rpc.common.common.api.Destroyable;
import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import io.netty.channel.ChannelFuture;

/**
 * <p> rpc channel future 接口</p>
 * @since 2024.06
 */
public interface RpcChannelFuture extends IServer {

    /**
     * channel future 信息
     * @return ChannelFuture
     * @since 2024.06
     */
    ChannelFuture channelFuture();

    /**
     * 对应的地址信息
     * @return 地址信息
     * @since 2024.06
     */
    RpcAddress address();

    /**
     * 可销毁的对象
     * @return 可销毁的信息
     * @since 0.1.3
     */
    Destroyable destroyable();
}
