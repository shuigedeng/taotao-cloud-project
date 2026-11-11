package com.taotao.cloud.rpc.serialization;

import com.alibaba.nacos.api.utils.json.JsonUtils;
import com.taotao.cloud.rpc.serialization.Serialization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

import static com.alibaba.nacos.api.utils.json.JsonUtils.toJson;

/**
 * JSON 序列化
 * <p>
 * 基于 ttc-util-core 的 JsonUtil 实现，跨语言、人类可读。
 */
public class JsonSerialization implements Serialization {

    private static final Logger log = LogManager.getLogger(JsonSerialization.class);

    @Override
    public byte getContentTypeId() {
        return com.taotao.cloud.rpc.common.ProtocolConstants.SERIALIZE_JSON;
    }

    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) return new byte[0];
        try {
            String json = toJson(obj);
            return json.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("JSON serialize failed: {}", e.getMessage(), e);
            throw com.taotao.cloud.rpc.common.RpcException.serialization(
                    "JSON serialize failed: " + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) return null;
        try {
            String json = new String(bytes, StandardCharsets.UTF_8);
            return (T) JsonUtils.toObj(json, clazz);
        } catch (Exception e) {
            log.error("JSON deserialize failed: {}", e.getMessage(), e);
            throw com.taotao.cloud.rpc.common.RpcException.serialization(
                    "JSON deserialize failed: " + e.getMessage(), e);
        }
    }
}
