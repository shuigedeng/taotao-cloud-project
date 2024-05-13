package com.taotao.cloud.rpc.client.client.support.fail.impl;

import com.taotao.cloud.rpc.client.client.support.fail.FailStrategy;
import com.taotao.cloud.rpc.client.client.support.fail.enums.FailTypeEnum;

/**
 * 快速失败策略工厂
 *
 * @author shuigedeng
 * @since 0.1.1
 */
public final class FailStrategyFactory {

    private FailStrategyFactory() {
    }

    /**
     * 失败策略
     *
     * @param failTypeEnum 失败策略枚举
     * @return 失败策略实现
     * @since 0.1.1
     */
    public static FailStrategy failStrategy(final FailTypeEnum failTypeEnum) {
        switch (failTypeEnum) {
            case FAIL_FAST:
                return new FailFastStrategy();
            case FAIL_OVER:
                return new FailOverStrategy();
            default:
                throw new UnsupportedOperationException("not support fail type " + failTypeEnum);
        }
    }

}
