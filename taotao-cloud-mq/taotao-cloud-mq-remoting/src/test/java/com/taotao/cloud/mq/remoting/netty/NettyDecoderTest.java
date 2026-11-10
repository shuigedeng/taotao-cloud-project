package com.taotao.cloud.mq.remoting.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NettyDecoder 单元测试
 */
public class NettyDecoderTest {

    @Test
    public void testConstructor() {
        // 测试构建
        NettyDecoder decoder = new NettyDecoder();
        assertNotNull(decoder);
    }

    @Test
    public void testDecodeWithSmallData() {
        // 测试解码小数据
        EmbeddedChannel channel = new EmbeddedChannel(new NettyDecoder());

        // 创建一个简单的 ByteBuf
        ByteBuf buf = Unpooled.buffer(4);
        buf.writeInt(12345);

        // 写入 channel
        channel.writeInbound(buf);

        // 清理
        channel.finish();
    }

    @Test
    public void testDecodeWithEmptyData() {
        // 测试空数据
        EmbeddedChannel channel = new EmbeddedChannel(new NettyDecoder());

        ByteBuf buf = Unpooled.buffer(0);
        channel.writeInbound(buf);

        channel.finish();
    }

    @Test
    public void testDecoderProperties() {
        // 测试解码器属性
        NettyDecoder decoder = new NettyDecoder();
        assertNotNull(decoder);

        // 验证继承关系
        assertTrue(decoder instanceof io.netty.handler.codec.LengthFieldBasedFrameDecoder);
    }
}
