package com.taotao.cloud.ccsr.core.utils;

import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataDeleteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataWriteRequest;
import com.taotao.cloud.ccsr.core.remote.RpcServer;
import com.taotao.cloud.ccsr.core.remote.grpc.GrpcService;
import com.taotao.cloud.ccsr.core.serializer.Serializer;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;

import java.util.Arrays;
import java.util.List;

public class ProtoMessageUtils {

    public static Message parse(Serializer serializer, byte[] bytes) {
        List<Class<? extends Message>> messages = List.of(
                MetadataReadRequest.class,
                MetadataWriteRequest.class,
                MetadataDeleteRequest.class
        );

        for (Class<? extends Message> message : messages) {
            try {
                return serializer.deserialize(bytes, message);
            } catch (Exception ignored) {}
        }

        throw new IllegalArgumentException("Message解析失败，bytes: " + Arrays.toString(bytes));
    }

}
