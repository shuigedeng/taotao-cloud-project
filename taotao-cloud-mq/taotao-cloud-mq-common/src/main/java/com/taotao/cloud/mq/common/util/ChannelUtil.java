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

package com.taotao.cloud.mq.common.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * channel 工具类
 *
 * @author shuigedeng
 * @since 2024.05
 */
public class ChannelUtil {

    private ChannelUtil() {}

    /**
     * 获取 channel 标识
     *
     * @param channel 管道
     * @return 结果
     * @since 2024.05
     */
    public static String getChannelId(Channel channel) {
        return channel.id().asLongText();
    }

    /**
     * 获取 channel 标识
     *
     * @param ctx 管道
     * @return 结果
     * @since 2024.05
     */
    public static String getChannelId(ChannelHandlerContext ctx) {
        return getChannelId(ctx.channel());
    }
}
