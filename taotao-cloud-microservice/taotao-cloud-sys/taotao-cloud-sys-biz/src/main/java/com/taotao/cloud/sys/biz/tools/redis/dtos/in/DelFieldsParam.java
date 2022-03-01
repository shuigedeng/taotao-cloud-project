package com.taotao.cloud.sys.biz.tools.redis.dtos.in;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DelFieldsParam {
    /**
     * 连接参数
     */
    private ConnParam connParam;
    /**
     * hash key
     */
    private String key;
    /**
     * 删除的字段列表
     */
    private List<String> fields = new ArrayList<>();
    /**
     * 序列化参数
     */
    private SerializerParam serializerParam;
}
