package com.taotao.cloud.common.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
 
import java.io.IOException;
 
/**
 * 针对数组的处理
 * @author dry
 * @since 2022-04-30 16:01:00
 */
public class NullArrayJsonSerializer extends JsonSerializer {
    /**声明为单例模式*/
    public static final NullArrayJsonSerializer INSTANCE=new NullArrayJsonSerializer();
 
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        //这里不需要判断value的值,因为只要能进到这个方法里面,就证明值是null
        gen.writeStartArray();
        gen.writeEndArray();
    }
}
