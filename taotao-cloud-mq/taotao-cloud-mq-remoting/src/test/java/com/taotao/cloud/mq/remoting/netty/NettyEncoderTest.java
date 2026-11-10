package com.taotao.cloud.mq.remoting.netty;

import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NettyEncoder 单元测试
 */
public class NettyEncoderTest {

    private NettyEncoder encoder;

    @Mock
    private ChannelHandlerContext ctx;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        encoder = new NettyEncoder();
    }

    @Test
    public void testEncodeWithSimpleCommand() throws Exception {
        // Create a simple command
        RemotingCommand command = RemotingCommand.createRequestCommand(100);
        command.setRemark("Test remark");

        ByteBuf out = Unpooled.buffer();

        // Encode the command
        encoder.encode(ctx, command, out);

        // Verify the output is not empty
        assertTrue(out.readableBytes() > 0);

        // Verify the length field is set correctly
        int frameLength = out.readInt();
        assertTrue(frameLength > 0);
        // After reading the 4-byte length field, the remaining bytes should equal the stored frame length
        assertEquals(frameLength, out.readableBytes());

        out.release();
    }

    @Test
    public void testEncodeWithBody() throws Exception {
        // Create a command with body
        RemotingCommand command = RemotingCommand.createRequestCommand(200);
        byte[] body = "Test body content".getBytes();
        command.setBody(body);

        ByteBuf out = Unpooled.buffer();

        // Encode the command
        encoder.encode(ctx, command, out);

        // Verify the output
        assertTrue(out.readableBytes() > 0);

        int frameLength = out.readInt();
        assertTrue(frameLength > 0);

        out.release();
    }

    @Test
    public void testEncodeWithExtFields() throws Exception {
        // Create a command with ext fields
        RemotingCommand command = RemotingCommand.createRequestCommand(300);
        java.util.HashMap<String, String> extFields = new java.util.HashMap<>();
        extFields.put("key1", "value1");
        extFields.put("key2", "value2");
        command.setExtFields(extFields);

        ByteBuf out = Unpooled.buffer();

        // Encode the command
        encoder.encode(ctx, command, out);

        // Verify the output
        assertTrue(out.readableBytes() > 0);

        out.release();
    }

    @Test
    public void testMultipleEncodes() throws Exception {
        ByteBuf out = Unpooled.buffer();

        // Encode multiple commands
        for (int i = 0; i < 5; i++) {
            RemotingCommand command = RemotingCommand.createRequestCommand(100 + i);
            command.setRemark("Command " + i);
            encoder.encode(ctx, command, out);
        }

        // Verify all commands are encoded
        assertTrue(out.readableBytes() > 0);

        out.release();
    }

    @Test
    public void testEncodeResponseCommand() throws Exception {
        // Create a response command
        RemotingCommand command = RemotingCommand.createResponseCommand(0, "Success");
        command.markResponseType();

        ByteBuf out = Unpooled.buffer();

        // Encode the command
        encoder.encode(ctx, command, out);

        // Verify the output
        assertTrue(out.readableBytes() > 0);

        out.release();
    }
}
