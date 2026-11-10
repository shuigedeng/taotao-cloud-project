package com.taotao.cloud.mq.remoting.netty;

import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Netty 解码器
 * 将 ByteBuf 解码为 RemotingCommand
 * <p>
 * 协议格式：
 * ┌──────────┬──────────────┬─────────────────────┐
 * │ 总长度   │ 序列化类型   │ 扩展头              │
 * │ (4B)     │ (1B)         │ (2B长度 + N)        │
 * ├──────────┼──────────────┼─────────────────────┤
 * │ 消息体长度│ 消息体       │                     │
 * │ (4B)     │ (N)          │                     │
 * └──────────┴──────────────┴─────────────────────┘
 */
public class NettyDecoder extends LengthFieldBasedFrameDecoder {

    // 最大帧长度 16MB
    private static final int FRAME_MAX_LENGTH = 16 * 1024 * 1024;

    // 协议中长度字段的偏移量（0）
    private static final int LENGTH_FIELD_OFFSET = 0;

    // 长度字段的长度（4字节）
    private static final int LENGTH_FIELD_LENGTH = 4;

    // 长度调整值（0，因为长度字段后面的数据长度就是总长度减去长度字段本身的长度）
    private static final int LENGTH_ADJUSTMENT = 0;

    // 跳过的字节数（4字节，即长度字段本身）
    private static final int INITIAL_BYTES_TO_STRIP = 4;

    public NettyDecoder() {
        super(FRAME_MAX_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH,
                LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (frame == null) {
                return null;
            }

            // 1. 读取序列化类型
            byte serializeType = frame.readByte();

            // 2. 读取头部长度 (int — 与 encoder 对齐, 避免 short 截断)
            int headerLength = frame.readInt();

            // 3. 读取头部数据
            byte[] headerData = new byte[headerLength];
            frame.readBytes(headerData);

            // 4. 解析头部
            RemotingCommand cmd = RemotingCommand.decodeHeader(headerData);
            cmd.setSerializeTypeCurrentRPC(serializeType);

            // 5. 读取消息体长度
            int bodyLength = frame.readInt();

            // 6. 读取消息体
            if (bodyLength > 0) {
                byte[] bodyData = new byte[bodyLength];
                frame.readBytes(bodyData);
                cmd.setBody(bodyData);
            }

            return cmd;

        } catch (Exception e) {
            // 记录解码异常
            throw e;
        } finally {
            if (frame != null) {
                frame.release();
            }
        }
    }
}
