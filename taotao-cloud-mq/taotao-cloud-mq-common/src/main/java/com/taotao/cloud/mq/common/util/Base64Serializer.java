package com.taotao.cloud.mq.common.util;


import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * byte[] 字段的 Base64 编解码器。
 * <p>
 * 挂给 ttc-util JsonUtil 的 {@code @JsonSerialize} / {@code @JsonDeserialize}：
 * Jackson 的 {@code writeValueAsBytes}/{@code readValue(byte[])} 一直是「base64 文本」这语义，
 * 而 ttc-util 的默认数组分支会把 byte[] 写成 {@code [1,2,3]} 数字数组、读回来时
 * {@code Array.set} 直接抛 {@code argument type mismatch}（实测）。协议层的老对端
 * 也都按 base64 读，所以这里显式把 byte[] 拉回 base64，帧格式保持逐字节兼容。
 */
public final class Base64Serializer  {

	public final class Base64ValueSerializer extends ValueSerializer {



		@Override
		public void serialize(Object value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
			if (value == null) {
				gen.writeString( "null");
			}
			gen.writeString( "\"" + Base64.getEncoder().encodeToString((byte[]) value) + "\"");
		}
	}

	public final class Base64ValueDeserializer extends ValueDeserializer {


		@Override
		public Object deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
			String value = p.getValueAsString();
			if (value == null || "null".equals(value)) {
				return null;
			}
			return Base64.getDecoder().decode(value.getBytes(StandardCharsets.US_ASCII));
		}

	}

}
