package com.taotao.cloud.mq.remoting.netty;

import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Netty 编码器
 * 将 RemotingCommand 编码为 ByteBuf
 */
public class NettyEncoder extends MessageToByteEncoder<RemotingCommand> {

    // 长度占位符（4字节）
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    @Override
    protected void encode(ChannelHandlerContext ctx, RemotingCommand cmd, ByteBuf out) throws Exception {
        try {
            // 记录起始位置用于回填总长度
            int beginIndex = out.writerIndex();

            // 1. 总长度占位（最后回填）
            out.writeBytes(LENGTH_PLACEHOLDER);

            // 2. 序列化类型
            out.writeByte(cmd.getSerializeTypeCurrentRPC());

            // 3. 扩展头长度和数据
            byte[] headerData = cmd.encodeHeader();
            // 用 int 写 headerLength (避免 short 上限 32767 截断大型 header)
            out.writeInt(headerData.length);
            out.writeBytes(headerData);

            // 4. 消息体
            byte[] body = cmd.getBody();
            if (body != null) {
                out.writeInt(body.length);
                out.writeBytes(body);
            } else {
                out.writeInt(0);
            }

            // 回填总长度
            int endIndex = out.writerIndex();
            out.setInt(beginIndex, endIndex - beginIndex - 4);
        } catch (Exception e) {
            // 记录编码异常
            throw e;
        }
    }
}
