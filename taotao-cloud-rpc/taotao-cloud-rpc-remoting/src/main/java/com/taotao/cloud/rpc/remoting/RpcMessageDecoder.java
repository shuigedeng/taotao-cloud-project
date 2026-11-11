package com.taotao.cloud.rpc.remoting;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * RPC 消息解码器
 * 将字节流解码为 RPC 请求/响应对象
 */
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    // 魔数
    private static final byte[] MAGIC = new byte[]{'Z', 'R', 'P', 'C'};

    // 消息类型：请求
    private static final byte MSG_TYPE_REQUEST = 1;

    // 消息类型：响应
    private static final byte MSG_TYPE_RESPONSE = 2;

    // 头部大小：魔数(4) + 版本(1) + 消息类型(1) + 数据长度(4) = 10
    private static final int HEADER_SIZE = 10;

    // 最大帧长度 10MB
    private static final int MAX_FRAME_LENGTH = 10 * 1024 * 1024;

    public RpcMessageDecoder() {
        super(MAX_FRAME_LENGTH, 6, 4, 0, 0, true);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 先检查是否有足够的字节读取头部
        if (in.readableBytes() < HEADER_SIZE) {
            return null;
        }

        // 标记当前读位置
        in.markReaderIndex();

        // 读取魔数
        byte[] magic = new byte[4];
        in.readBytes(magic);
        if (!isMagicValid(magic)) {
            // 魔数不匹配，关闭连接
            in.resetReaderIndex();
            throw new IllegalArgumentException("Invalid magic number");
        }

        // 读取版本号
        byte version = in.readByte();
        if (version != 1) {
            // 与魔数那一支对称：抛出去之前必须把读位置还回帧首，否则缓冲里剩下的字节是从
            // 帧中间开始的，下一次解码的起点就被这一次失败挪走了。
            in.resetReaderIndex();
            throw new IllegalArgumentException("Unsupported version: " + version);
        }

        // 读取消息类型
        byte msgType = in.readByte();

        // 读取数据长度
        int dataLength = in.readInt();
        if (dataLength < 0 || dataLength > MAX_FRAME_LENGTH) {
            throw new IllegalArgumentException("Invalid data length: " + dataLength);
        }

        // 检查是否有足够的数据
        if (in.readableBytes() < dataLength) {
            // 数据不完整，重置读位置
            in.resetReaderIndex();
            return null;
        }

        // 读取数据
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        // Java 原生序列化的流头是 AC ED 00 05：先按它判一次，好过把"这一帧压根不是
        // 序列化流"交给 ObjectInputStream 去抛一个 getMessage()==null 的 EOFException。
        if (dataLength < 4 || (data[0] & 0xFF) != 0xAC || (data[1] & 0xFF) != 0xED) {
            throw new IllegalArgumentException("Frame body is not a Java serialization stream: "
                    + "declaredLength=" + dataLength + ", bodyHead=" + hexHead(data));
        }

        // 反序列化
        Object obj;
        try {
            obj = deserialize(data);
        } catch (java.io.IOException e) {
            // 长度与内容对不上（截断、或声明得比对象短）时，ObjectInputStream 抛的是
            // EOFException/StreamCorruptedException，一个字的消息都没有；落进日志就只剩
            // 一个类名。补上"是哪一帧"再往外抛。
            throw new java.io.IOException("Failed to deserialize an RPC frame: declaredLength="
                    + dataLength + ", bodyHead=" + hexHead(data), e);
        }

        // 根据消息类型返回
        if (msgType == MSG_TYPE_REQUEST && obj instanceof RpcRequest) {
            return obj;
        } else if (msgType == MSG_TYPE_RESPONSE && obj instanceof RpcResponse) {
            return obj;
        } else {
            throw new IllegalArgumentException("Message type mismatch");
        }
    }

    /**
     * 验证魔数
     */
    private boolean isMagicValid(byte[] magic) {
        if (magic == null || magic.length != 4) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (magic[i] != MAGIC[i]) {
                return false;
            }
        }
        return true;
    }

    /** 帧的开头几个字节，出错时拿来认脸。 */
    private static String hexHead(byte[] data) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < data.length && i < 4; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(String.format("%02X", data[i] & 0xFF));
        }
        return sb.append(']').toString();
    }

    /**
     * Java 原生反序列化
     */
    private Object deserialize(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }
}
