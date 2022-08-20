package com.taotao.cloud.common.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * 针对数组的处理
 * @since 2022-04-30 16:01:00
 */
public class NullPrimitiveWrapperBooleanJsonSerializer extends JsonSerializer {
    /**声明为单例模式*/
    public static final NullPrimitiveWrapperBooleanJsonSerializer INSTANCE=new NullPrimitiveWrapperBooleanJsonSerializer();
 
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeBoolean(false);
    }
}
