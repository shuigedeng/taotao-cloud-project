package com.taotao.cloud.sys.biz.modules.redis.dtos.in;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DelKeysParam {
    /**
     * 连接信息
     */
    private ConnParam connParam;
    /**
     * 需要删除的 key 列表
     */
    private List<String> keys = new ArrayList<>();
    /**
     * 序列化参数
     */
    private SerializerParam serializerParam;
}
