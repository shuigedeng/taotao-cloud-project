package com.taotao.cloud.rpc.protocol;

import com.taotao.cloud.rpc.common.ProtocolConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * ttc-RPC 协议消息
 * <p>
 * 24 字节定长消息头 + 可选 attachments（Key-Value 序列） + 业务 body
 * <p>
 * 详细定义参见：
 * <ul>
 *   <li>https://yuque.com/yuku123/ct73pg/287_dubbo</li>
 * </ul>
 */
public class TtcRpcMessage {

    /** 魔数 */
    private int magic = ProtocolConstants.MAGIC_NUMBER;
    /** 版本 */
    private byte version = ProtocolConstants.VERSION;
    /** 消息类型 */
    private byte messageType;
    /** 序列化器 ID */
    private byte serializationId = ProtocolConstants.SERIALIZE_HESSIAN2;
    /** 压缩方式 */
    private byte compression = ProtocolConstants.COMPRESS_NONE;
    /** 状态码 */
    private short status = ProtocolConstants.STATUS_OK;
    /** 请求 ID */
    private long requestId;
    /** 消息体长度 */
    private int bodyLength;
    /** 头部 KV 区长度（attachments 字节数） */
    private short headerLength;
    /** 业务 body（已序列化） */
    private byte[] body;
    /** 业务对象（反序列化后） */
    private Object bodyObject;
    /** 附件（Header KV 解码结果） */
    private Map<String, String> attachments = new HashMap<>();

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public byte getSerializationId() {
        return serializationId;
    }

    public void setSerializationId(byte serializationId) {
        this.serializationId = serializationId;
    }

    public byte getCompression() {
        return compression;
    }

    public void setCompression(byte compression) {
        this.compression = compression;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public short getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(short headerLength) {
        this.headerLength = headerLength;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
        if (body != null) {
            this.bodyLength = body.length;
        }
    }

    public Object getBodyObject() {
        return bodyObject;
    }

    public void setBodyObject(Object bodyObject) {
        this.bodyObject = bodyObject;
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments == null ? new HashMap<>() : attachments;
    }

    public void addAttachment(String key, String value) {
        if (this.attachments == null) {
            this.attachments = new HashMap<>();
        }
        this.attachments.put(key, value);
    }

    public String getAttachment(String key) {
        return this.attachments == null ? null : this.attachments.get(key);
    }

    @Override
    public String toString() {
        return "TtcRpcMessage{" +
                "magic=" + Integer.toHexString(magic) +
                ", version=" + version +
                ", msgType=" + messageType +
                ", serId=" + serializationId +
                ", status=" + status +
                ", requestId=" + requestId +
                ", bodyLength=" + bodyLength +
                ", headerLength=" + headerLength +
                ", attachments=" + attachments +
                '}';
    }
}
