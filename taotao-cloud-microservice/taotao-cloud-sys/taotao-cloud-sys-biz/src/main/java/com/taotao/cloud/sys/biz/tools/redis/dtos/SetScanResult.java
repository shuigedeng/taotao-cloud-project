package com.taotao.cloud.sys.biz.tools.redis.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SetScanResult {
    private List<Object> members = new ArrayList<>();
    private String cursor;
    private boolean finish;

    public SetScanResult(List<Object> members, String cursor) {
        this.members = members;
        this.cursor = cursor;
        finish = "0".equals(cursor);
    }
}
