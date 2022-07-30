package com.taotao.cloud.common.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
 
import java.io.IOException;
 
/**
 * 针对map的处理
 * @author dry
 * @since 2022-04-30 16:01:00
 */
public class NullMapJsonSerializer extends JsonSerializer {
    /**声明为单例模式*/
    public static final NullMapJsonSerializer INSTANCE=new NullMapJsonSerializer();
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeEndObject();
    }
}
