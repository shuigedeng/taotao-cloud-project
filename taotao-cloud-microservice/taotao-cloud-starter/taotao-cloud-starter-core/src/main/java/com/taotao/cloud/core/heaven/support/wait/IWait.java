package com.taotao.cloud.core.heaven.support.wait;

import java.util.concurrent.TimeUnit;

/**
 * 等待接口
 */
@Deprecated
public interface IWait {

    /**
     * 等待时间
     * @param time 时间
     * @param timeUnit 单位
     */
    void waits(final long time, final TimeUnit timeUnit);

}
