package com.taotao.cloud.sys.biz.tools.redis.dtos.in;

import lombok.Data;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
public class ValueParam {
    /**
     * 连接参数
     */
    @Valid
    private ConnParam connParam;
    /**
     * key
     */
    private String key;
    /**
     * 是否读取所有信息
     */
    private boolean all;
    /**
     * key 扫描参数
     */
    private BaseKeyScanParam keyScanParam;
    /**
     * 范围查找参数
     */
    private RangeParam rangeParam;
    /**
     * 分数查找参数
     */
    private ScoreRangeParam scoreRangeParam;
    /**
     * 序列化参数
     */
    private SerializerParam serializerParam;

    @Data
    public static final class RangeParam {
        /**
         * 是否启用
         */
        private boolean enable;
        /**
         * start
         */
        private long start;
        /**
         * stop
         */
        private long stop;
    }

    @Data
    public static final class ScoreRangeParam {
        /**
         * 是否启用
         */
        private boolean enable;
        /**
         * 最小分数
         */
        private Double min;
        /**
         * 最大分数
         */
        private Double max;
    }
}
