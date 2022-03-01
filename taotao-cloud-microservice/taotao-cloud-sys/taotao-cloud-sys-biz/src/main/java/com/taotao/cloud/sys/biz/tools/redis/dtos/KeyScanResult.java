package com.taotao.cloud.sys.biz.tools.redis.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import redis.clients.jedis.HostAndPort;

import java.util.ArrayList;
import java.util.List;

@Data
public class KeyScanResult {
    private List<KeyResult> keys = new ArrayList<>();
    private String nodeId;
    private String cursor;
    // 标志本节点是否完成
    @JsonIgnore
    private boolean finish;
    // 标志所有节点是否完成
    private boolean done;

    public KeyScanResult() {
    }

    public KeyScanResult(List<KeyResult> keys, String cursor, String nodeId) {
        this.keys = keys;
        this.cursor = cursor;
        this.nodeId = nodeId;
    }

    @Data
    public static class KeyResult{
        private String key;
        private String type;
        private Long ttl;
        private Long pttl;
        private long length;
        private int slot;

        public KeyResult() {
        }

        public KeyResult(String key, String type, Long ttl, Long pttl, long length) {
            this.key = key;
            this.type = type;
            this.ttl = ttl;
            this.pttl = pttl;
            this.length = length;
        }

        public KeyResult(String key, String type, Long ttl, Long pttl, long length, int slot) {
            this.key = key;
            this.type = type;
            this.ttl = ttl;
            this.pttl = pttl;
            this.length = length;
            this.slot = slot;
        }
    }
}
