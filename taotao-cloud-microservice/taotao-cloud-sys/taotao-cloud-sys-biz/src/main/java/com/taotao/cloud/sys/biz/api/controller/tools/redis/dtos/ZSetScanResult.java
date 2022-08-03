package com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ZSetScanResult {
    private List<ZSetTuple> tuples = new ArrayList<>();
    private String cursor;
    private boolean finish;

    public ZSetScanResult(List<ZSetTuple> tuples, String cursor) {
        this.tuples = tuples;
        this.cursor = cursor;
        finish = "0".equals(cursor);
    }
}
