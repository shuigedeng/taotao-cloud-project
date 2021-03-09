package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class MySerializer extends JsonSerializer<Double> {
    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 将Double salary的值 四舍五入
        String number = BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        // 输出 四舍五入后的值
        gen.writeNumber(number);
    }
}
