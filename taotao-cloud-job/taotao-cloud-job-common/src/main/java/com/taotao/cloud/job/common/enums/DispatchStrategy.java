package com.taotao.cloud.job.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DispatchStrategy
 *
 * @author shuigedeng
 * @since 2021/2/22
 */
@Getter
@AllArgsConstructor
public enum DispatchStrategy {

    /**
     * 健康度优先
     */
    HEALTH_FIRST(1),
    /**
     * 随机
     */
    RANDOM(2);


    private final int v;

    public static DispatchStrategy of(Integer v) {
        if (v == null) {
            return HEALTH_FIRST;
        }
        for (DispatchStrategy ds : values()) {
            if (v.equals(ds.v)) {
                return ds;
            }
        }
        throw new IllegalArgumentException("unknown DispatchStrategy of " + v);
    }
}
