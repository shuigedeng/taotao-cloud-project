package com.taotao.cloud.sys.biz.modules.redis.dtos;

import com.taotao.cloud.sys.biz.modules.serializer.service.Serializer;
import lombok.Data;
import redis.clients.jedis.resps.Tuple;

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

    public ZSetTuple(Tuple tuple, Serializer valueSerializer, ClassLoader classloader) throws IOException, ClassNotFoundException {
        byte[] bytes = tuple.getBinaryElement();
        score = tuple.getScore();
        value = valueSerializer.deserialize(bytes, classloader);
    }
}
