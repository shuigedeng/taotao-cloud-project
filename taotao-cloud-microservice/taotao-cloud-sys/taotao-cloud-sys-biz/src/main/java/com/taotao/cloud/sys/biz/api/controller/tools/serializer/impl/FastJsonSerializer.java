package com.taotao.cloud.sys.biz.api.controller.tools.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.taotao.cloud.sys.biz.api.controller.tools.serializer.SerializerConstants;
import org.springframework.stereotype.Component;

@Component
public class FastJsonSerializer extends StringSerializer {
    @Override
    public byte[] serialize(Object data){
        String jsonString = JSON.toJSONString(data);
        return super.serialize(jsonString);
    }

    @Override
    public Object deserialize(byte[] bytes,ClassLoader classLoader)  {
        String deserialize = String.valueOf(super.deserialize(bytes, classLoader));
        return JSON.parseObject(deserialize);
    }

    @Override
    public String name() {
        return SerializerConstants.FASTJSON;
    }
}