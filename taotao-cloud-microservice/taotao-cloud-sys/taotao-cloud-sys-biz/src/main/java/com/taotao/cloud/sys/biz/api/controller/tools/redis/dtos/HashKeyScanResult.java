package com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos;

import lombok.Data;

import java.util.*;

@Data
public class HashKeyScanResult {
    private String key;
    private List<String> fields = new ArrayList<>();
    private Map<String,Object> data = new HashMap<>();
    private String cursor;
    private boolean finish;

    public HashKeyScanResult(String key) {
        this.key = key;
    }

    public HashKeyScanResult(String key,List<String> fields, String cursor) {
        this.key = key;
        this.fields = fields;
        this.cursor = cursor;
    }
}
