//package com.taotao.cloud.rpc.serialization;
//
//import com.taotao.cloud.rpc.common.RpcException;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//
///**
// * Protobuf 序列化
// * <p>
// * 仅支持 {@link com.google.protobuf.MessageLite} 及其子类。
// * 性能最高、IDL 驱动、跨语言。
// */
//public class ProtobufSerialization implements Serialization {
//
//    private static final Logger log = LogManager.getLogger(ProtobufSerialization.class);
//
//    @Override
//    public byte getContentTypeId() {
//        return com.taotao.cloud.rpc.common.ProtocolConstants.SERIALIZE_PROTOBUF;
//    }
//
//    @Override
//    public byte[] serialize(Object obj) {
//        if (obj == null) return new byte[0];
//        if (!(obj instanceof com.google.protobuf.MessageLite)) {
//            throw RpcException.serialization(
//                    "ProtobufSerialization only supports com.google.protobuf.MessageLite, got " + obj.getClass());
//        }
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//            ((com.google.protobuf.MessageLite) obj).writeTo(baos);
//            return baos.toByteArray();
//        } catch (Exception e) {
//            log.error("Protobuf serialize failed: {}", e.getMessage(), e);
//            throw RpcException.serialization("Protobuf serialize failed: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
//        if (bytes == null || bytes.length == 0) return null;
//        if (!com.google.protobuf.MessageLite.class.isAssignableFrom(clazz)) {
//            throw RpcException.serialization(
//                    "ProtobufSerialization only supports com.google.protobuf.MessageLite, got " + clazz);
//        }
//        try {
//            com.google.protobuf.MessageLite defaultInstance = (com.google.protobuf.MessageLite)
//                    clazz.getMethod("getDefaultInstance").invoke(null);
//            return (T) defaultInstance.newBuilderForType()
//                    .mergeFrom(new ByteArrayInputStream(bytes))
//                    .build();
//        } catch (Exception e) {
//            log.error("Protobuf deserialize failed: {}", e.getMessage(), e);
//            throw RpcException.serialization("Protobuf deserialize failed: " + e.getMessage(), e);
//        }
//    }
//}
