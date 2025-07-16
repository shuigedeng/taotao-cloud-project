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

import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.mq.common.rpc.RpcMessageDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class DelimiterUtil {

    private DelimiterUtil() {}

    /**
     * 分隔符
     */
    public static final String DELIMITER = "~!@#$%^&*";

    /**
     * 长度
     * <p>
     * ps: 这个长度是必须的，避免把缓冲区打爆
     */
    public static final int LENGTH = 65535;

    /**
     * 分隔符 buffer
     *
     * @since 2024.05
     */
    public static final ByteBuf DELIMITER_BUF = Unpooled.copiedBuffer(DELIMITER.getBytes());

    /**
     * 获取对应的字节缓存
     *
     * @param text 文本
     * @return 结果
     * @since 2024.05
     */
    public static ByteBuf getByteBuf(String text) {
        return Unpooled.copiedBuffer(text.getBytes());
    }

    /**
     * 获取消息
     *
     * @param rpcMessageDto 消息体
     * @return 结果
     * @since 2024.05
     */
    public static ByteBuf getMessageDelimiterBuffer(RpcMessageDto rpcMessageDto) {
        String json = JSON.toJSONString(rpcMessageDto);
        String jsonDelimiter = json + DELIMITER;

        return Unpooled.copiedBuffer(jsonDelimiter.getBytes());
    }
}
