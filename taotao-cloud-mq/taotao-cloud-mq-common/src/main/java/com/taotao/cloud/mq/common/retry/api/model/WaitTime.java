package com.taotao.cloud.mq.common.retry.api.model;

import java.util.concurrent.TimeUnit;

/**
 * 等待时间接口
 * @author shuigedeng
 * @since 0.0.1
 */
public interface WaitTime {

    /**
     * 等待时间
     * @return 时间
     */
    long time();

    /**
     * 等待时间单位
     * @return 时间单位
     */
    TimeUnit unit();

}
