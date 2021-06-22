package com.taotao.cloud.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.taotao.cloud.common.enums.BaseEnum;
import java.io.IOException;

/**
 * 继承了BaseEnum接口的枚举值，将会统一按照以下格式序列化
 * {
 * "code": "XX",
 * "desc": "xxx"
 * }
 *
 * @author zuihou
 * @date 2020/5/4 下午6:45
 */
public class EnumSerializer extends StdSerializer<BaseEnum> {
    public static final EnumSerializer INSTANCE = new EnumSerializer();
    public static final String ALL_ENUM_KEY_FIELD = "code";
    public static final String ALL_ENUM_DESC_FIELD = "desc";

    public EnumSerializer() {
        super(BaseEnum.class);
    }

    @Override
    public void serialize(BaseEnum distance, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        generator.writeStartObject();
        generator.writeFieldName(ALL_ENUM_KEY_FIELD);
        generator.writeString(distance.getCode().toString());
        generator.writeFieldName(ALL_ENUM_DESC_FIELD);
        generator.writeEndObject();
    }
}
