package com.taotao.cloud.sys.biz.modules.serializer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class SerializerChoseService {
    private Map<String,Serializer> serializerMap = new HashMap<>();

    public SerializerChoseService() {
    }

    @Autowired(required = false)
    public SerializerChoseService(List<Serializer> serializers){
        for (Serializer serializer : serializers) {
            serializerMap.put(serializer.name(),serializer);
        }
    }

    /**
     * 可用的序列化工具列表
     * @return
     */
    public Set<String> serializers(){
        return serializerMap.keySet();
    }

    /**
     * 获取一个序列化工具
     * @param serializerName
     * @return
     */
    public Serializer choseSerializer(String serializerName) {
        Serializer serializer = serializerMap.get(serializerName);
        if (serializer == null){
            log.warn("不支持的序列化工具: {},将默认使用 string 序列化",serializerName);
            return serializerMap.get("string");
        }
        return serializer;
    }
}
