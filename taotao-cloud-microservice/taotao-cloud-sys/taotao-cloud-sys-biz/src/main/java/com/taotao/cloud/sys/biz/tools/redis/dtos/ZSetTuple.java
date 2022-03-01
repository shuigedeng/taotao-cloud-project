package com.taotao.cloud.sys.biz.tools.redis.dtos;

import com.sanri.tools.modules.serializer.service.Serializer;
import lombok.Data;
import redis.clients.jedis.Tuple;

import java.io.IOException;

@Data
public class ZSetTuple {
    private Object value;
    private double score;

    public ZSetTuple() {
    }

    public ZSetTuple(Object value, double score) {
        this.value = value;
        this.score = score;
    }

    public ZSetTuple(Tuple tuple, Serializer valueSerializer,ClassLoader classloader) throws IOException, ClassNotFoundException {
        byte[] bytes = tuple.getBinaryElement();
        score = tuple.getScore();
        value = valueSerializer.deserialize(bytes, classloader);
    }
}
