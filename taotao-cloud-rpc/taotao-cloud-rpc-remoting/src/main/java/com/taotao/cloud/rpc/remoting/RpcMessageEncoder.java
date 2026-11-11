package com.taotao.cloud.rpc.remoting;

import com.taotao.cloud.rpc.remoting.RpcRequest;
import com.taotao.cloud.rpc.remoting.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * RPC 消息编码器
 * 将 RPC 请求/响应对象编码为字节流
 */
public class RpcMessageEncoder extends MessageToByteEncoder<Object> {

    // 魔数，用于识别 RPC 协议
    private static final byte[] MAGIC = new byte[]{'Z', 'R', 'P', 'C'};

    // 版本号
    private static final byte VERSION = 1;

    // 消息类型：请求
    private static final byte MSG_TYPE_REQUEST = 1;

    // 消息类型：响应
    private static final byte MSG_TYPE_RESPONSE = 2;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 写入魔数
        out.writeBytes(MAGIC);

        // 写入版本号
        out.writeByte(VERSION);

        // 判断消息类型
        byte msgType;
        byte[] data;

        if (msg instanceof RpcRequest) {
            msgType = MSG_TYPE_REQUEST;
            data = serialize(msg);
        } else if (msg instanceof RpcResponse) {
            msgType = MSG_TYPE_RESPONSE;
            data = serialize(msg);
        } else {
            throw new IllegalArgumentException("Unsupported message type: " + msg.getClass());
        }

        // 写入消息类型
        out.writeByte(msgType);

        // 写入数据长度
        out.writeInt(data.length);

        // 写入数据
        out.writeBytes(data);
    }

    /**
     * Java 原生序列化
     */
    private byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
        }
        return baos.toByteArray();
    }
}
