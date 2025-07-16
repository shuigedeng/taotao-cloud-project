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

package com.taotao.cloud.rpc.core.codec;

import com.taotao.cloud.rpc.common.enums.PackageType;
import com.taotao.cloud.rpc.common.exception.UnrecognizedException;
import com.taotao.cloud.rpc.common.protocol.RpcRequest;
import com.taotao.cloud.rpc.common.protocol.RpcResponse;
import com.taotao.cloud.rpc.common.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 解码器（入站 handler）
 */
@Slf4j
public class CommonDecoder extends ReplayingDecoder {

    // 对象头的魔术: cafe babe 表示 class 类型的文件
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
            throws UnrecognizedException {
        int magic = in.readInt(); // 读取 4字节 魔数
        if (magic != MAGIC_NUMBER) {
            log.error("Unrecognized protocol package: {}", Integer.toHexString(magic));
            throw new UnrecognizedException("Unrecognized protocol package error");
        }

        int packageCode = in.readInt(); // 读取 4 字节 协议包类型
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            log.error("Unrecognized data package: {}", packageCode);
            throw new UnrecognizedException("Unrecognized data package error");
        }
        int serializerCode = in.readInt(); // 读取 4 字节 序列化 类型
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null) {
            log.error("Unrecognized deserializer : {}", serializerCode);
            throw new UnrecognizedException("Unrecognized deserializer error");
        }
        int length = in.readInt(); // 读取 4 字节 数据长度
        byte[] bytes = new byte[length];
        log.debug("decode object length [{}]", length);
        in.readBytes(bytes);
        // 自定义 反序列化器 对 二进制 反序列化 为 实例
        log.debug(
                "serializer [{}] deserialize [{}]",
                serializer.getClass().getName(),
                packageClass.getName());
        Object obj = serializer.deserialize(bytes, packageClass);
        // 接着 传给 下一个 处理器 NettyServerHandler
        out.add(obj);
    }
}
