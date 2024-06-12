package com.taotao.cloud.rpc.common.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * rpc解码<br>
 *
 * @author shuigedeng
 * @version v1.0.0
 */
public class RpcDecoder extends ByteToMessageDecoder {

    public final int READABLE_BYTES = 4;

    private Class<?> clazz;

    public RpcDecoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() <= READABLE_BYTES) {
            return;
        }

        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength <= 0) {
            ctx.close();
        }

        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
        }

        byte[] bytes = new byte[dataLength];
        in.readBytes(bytes);
//        Object obj = SerializationUtil.deserialize(bytes, clazz);
//        out.add(obj);
    }
}
