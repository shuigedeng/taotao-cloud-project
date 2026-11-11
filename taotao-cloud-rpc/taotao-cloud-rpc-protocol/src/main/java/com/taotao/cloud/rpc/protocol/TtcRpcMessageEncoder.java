package com.taotao.cloud.rpc.protocol;

import com.taotao.cloud.rpc.common.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Map;

/**
 * ttc-RPC 消息编码器
 * <p>
 * 将 TtcRpcMessage 序列化为字节流。结构参见 {@link TtcRpcMessageDecoder}。
 */
public class TtcRpcMessageEncoder extends MessageToByteEncoder<TtcRpcMessage> {

    private static final Logger log = LogManager.getLogger(TtcRpcMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, TtcRpcMessage msg, ByteBuf out) throws Exception {
        // 魔数
        out.writeInt(ProtocolConstants.MAGIC_NUMBER);
        // 版本 + 消息类型 + 序列化ID + 压缩
        out.writeByte(msg.getVersion());
        out.writeByte(msg.getMessageType());
        out.writeByte(msg.getSerializationId());
        out.writeByte(msg.getCompression());
        // 状态码
        out.writeShort(msg.getStatus());
        // 请求ID
        out.writeLong(msg.getRequestId());

        // 先占位 body length / header length
        int bodyLengthIndex = out.writerIndex();
        out.writeInt(0); // body length 占位
        int headerLengthIndex = out.writerIndex();
        out.writeShort(0); // header length 占位
        out.writeShort(0); // reserved

        // 写 attachments
        byte[] headerBytes = writeAttachments(msg.getAttachments());
        out.writeBytes(headerBytes);

        // 写 body
        byte[] body = msg.getBody() == null ? new byte[0] : msg.getBody();
        out.writeBytes(body);

        // 回填长度
        int writerIndex = out.writerIndex();
        out.writerIndex(bodyLengthIndex);
        out.writeInt(body.length);
        out.writerIndex(headerLengthIndex);
        out.writeShort((short) headerBytes.length);
        out.writerIndex(writerIndex);

        if (log.isDebugEnabled()) {
            log.debug("Encoded ttc-RPC message: type={}, reqId={}, bodyLen={}",
                    msg.getMessageType(), msg.getRequestId(), body.length);
        }
    }

    /**
     * 序列化 attachments
     */
    private static byte[] writeAttachments(Map<String, String> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            for (Map.Entry<String, String> entry : attachments.entrySet()) {
                byte[] keyBytes = entry.getKey().getBytes("UTF-8");
                byte[] valBytes = entry.getValue() == null ? new byte[0] : entry.getValue().getBytes("UTF-8");
                dos.writeShort(keyBytes.length);
                dos.write(keyBytes);
                dos.writeShort(valBytes.length);
                dos.write(valBytes);
            }
        } catch (Exception e) {
            log.warn("Failed to write attachments: {}", e.getMessage());
        }
        return baos.toByteArray();
    }
}
