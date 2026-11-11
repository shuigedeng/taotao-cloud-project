package com.taotao.cloud.rpc.protocol;

import com.taotao.cloud.rpc.common.ProtocolConstants;
import com.taotao.cloud.rpc.common.RpcException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.List;

/**
 * ttc-RPC 消息解码器
 * <p>
 * 协议格式：4B 魔数 | 1B 版本 | 1B 消息类型 | 1B 序列化ID | 1B 压缩 | 2B 状态码
 *         | 8B 请求ID | 4B Body长度 | 2B Header长度 | 2B 保留
 *         | N1B Header KV | N2B Body
 * <p>
 * 使用前需配合 {@code LengthFieldBasedFrameDecoder} 解决半包问题，
 * 这里只做协议头解析。
 */
public class TtcRpcMessageDecoder extends ByteToMessageDecoder {

    private static final Logger log = LogManager.getLogger(TtcRpcMessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 至少需要 24 字节头
        if (in.readableBytes() < ProtocolConstants.HEADER_LENGTH) {
            return;
        }
        in.markReaderIndex();

        // 校验魔数
        int magic = in.readInt();
        if (magic != ProtocolConstants.MAGIC_NUMBER) {
            in.resetReaderIndex();
            throw RpcException.protocol("Invalid magic number: 0x" + Integer.toHexString(magic));
        }

        TtcRpcMessage message = new TtcRpcMessage();
        message.setMagic(magic);
        message.setVersion(in.readByte());
        message.setMessageType(in.readByte());
        message.setSerializationId(in.readByte());
        message.setCompression(in.readByte());
        message.setStatus(in.readShort());
        message.setRequestId(in.readLong());
        message.setBodyLength(in.readInt());
        message.setHeaderLength(in.readShort());
        in.readShort(); // reserved

        // 读 attachments
        if (message.getHeaderLength() > 0) {
            if (in.readableBytes() < message.getHeaderLength()) {
                in.resetReaderIndex();
                return;
            }
            byte[] headerBytes = new byte[message.getHeaderLength()];
            in.readBytes(headerBytes);
            message.setAttachments(readAttachments(headerBytes));
        }

        // 读 body
        if (message.getBodyLength() > 0) {
            if (in.readableBytes() < message.getBodyLength()) {
                in.resetReaderIndex();
                return;
            }
            byte[] bodyBytes = new byte[message.getBodyLength()];
            in.readBytes(bodyBytes);
            // TODO: 解压
            message.setBody(bodyBytes);
        }

        if (log.isDebugEnabled()) {
            log.debug("Decoded ttc-RPC message: type={}, reqId={}, bodyLen={}",
                    message.getMessageType(), message.getRequestId(), message.getBodyLength());
        }
        out.add(message);
    }

    /**
     * 从字节流读取 KV 附件
     * 格式：[short keyLen][byte[] key][short valLen][byte[] value]...
     */
    private static java.util.Map<String, String> readAttachments(byte[] bytes) {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes))) {
            while (dis.available() > 0) {
                short keyLen = dis.readShort();
                if (keyLen <= 0) break;
                byte[] keyBytes = new byte[keyLen];
                dis.readFully(keyBytes);
                short valLen = dis.readShort();
                byte[] valBytes = new byte[valLen];
                dis.readFully(valBytes);
                map.put(new String(keyBytes, "UTF-8"), new String(valBytes, "UTF-8"));
            }
        } catch (Exception e) {
            log.warn("Failed to read attachments: {}", e.getMessage());
        }
        return map;
    }
}
