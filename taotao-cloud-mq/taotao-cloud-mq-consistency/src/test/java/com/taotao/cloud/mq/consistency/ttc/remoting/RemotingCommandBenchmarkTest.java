package com.taotao.cloud.mq.consistency.ttc.remoting;

import com.taotao.cloud.mq.consistency.ttc.common.testsupport.BenchResult;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.ByteBuffer;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RemotingCommand 编解码性能测试。
 */
public class RemotingCommandBenchmarkTest {

    @TempDir
    Path tempDir;

    @Test
    void testEncodeDecodeHeaderOnly() {
        RemotingCommand cmd = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        cmd.setRemark("benchmark");

        BenchResult encode = BenchResult.runBest3("RemotingCommand", "encode-header-only", 100_000, 5_000,
                () -> cmd.encode());
        encode.printToConsole();
        encode.writeToFile(tempDir.resolve("benchmark.txt"));
        encode.assertBelowThreshold(10.0, "RemotingCommand encode header");

        ByteBuffer encoded = cmd.encode();
        BenchResult decode = BenchResult.runBest3("RemotingCommand", "decode-header-only", 100_000, 5_000,
                () -> {
                    encoded.rewind();
                    RemotingCommand.decode(encoded);
                });
        decode.printToConsole();
        decode.writeToFile(tempDir.resolve("benchmark.txt"));
        decode.assertBelowThreshold(10.0, "RemotingCommand decode header");
    }

    @Test
    void testEncodeDecodeWithBody() {
        RemotingCommand cmd = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        cmd.setBody(new byte[1024]);
        cmd.addExtField("topic", "PerfTopic");
        cmd.addExtField("queueId", "0");

        BenchResult encode = BenchResult.runBest3("RemotingCommand", "encode-with-1KB-body", 50_000, 3_000,
                () -> cmd.encode());
        encode.printToConsole();
        encode.writeToFile(tempDir.resolve("benchmark.txt"));

        ByteBuffer encoded = cmd.encode();
        BenchResult decode = BenchResult.runBest3("RemotingCommand", "decode-with-1KB-body", 50_000, 3_000,
                () -> {
                    encoded.rewind();
                    RemotingCommand.decode(encoded);
                });
        decode.printToConsole();
        decode.writeToFile(tempDir.resolve("benchmark.txt"));
        decode.assertBelowThreshold(20.0, "RemotingCommand decode 1KB body");
    }

    @Test
    void testNettyEncoderPerformance() {
        com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyEncoder encoder = new com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyEncoder();
        RemotingCommand cmd = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        cmd.setBody(new byte[256]);
        cmd.addExtField("topic", "T");

        io.netty.channel.embedded.EmbeddedChannel ch = new io.netty.channel.embedded.EmbeddedChannel(encoder);

        BenchResult bench = BenchResult.runBest3("NettyEncoder", "encode-256B", 100_000, 5_000,
                () -> ch.writeOutbound(cmd));
        bench.printToConsole();
        bench.writeToFile(tempDir.resolve("benchmark.txt"));
        bench.assertBelowThreshold(10.0, "NettyEncoder 256B");
        assertNotNull(ch);
    }
}
