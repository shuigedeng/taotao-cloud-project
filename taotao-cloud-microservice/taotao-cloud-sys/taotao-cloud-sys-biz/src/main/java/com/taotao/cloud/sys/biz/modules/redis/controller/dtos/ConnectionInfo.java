package com.taotao.cloud.sys.biz.modules.redis.controller.dtos;

import com.taotao.cloud.sys.biz.modules.redis.service.dtos.RedisNode;
import com.taotao.cloud.sys.biz.modules.redis.service.dtos.RedisRunMode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConnectionInfo {
    private RedisRunMode redisRunMode;
    private List<RedisNode> masterNodes = new ArrayList<>();

    public ConnectionInfo() {
    }

    public ConnectionInfo(RedisRunMode redisRunMode, List<RedisNode> masterNodes) {
        this.redisRunMode = redisRunMode;
        this.masterNodes = masterNodes;
    }
}
