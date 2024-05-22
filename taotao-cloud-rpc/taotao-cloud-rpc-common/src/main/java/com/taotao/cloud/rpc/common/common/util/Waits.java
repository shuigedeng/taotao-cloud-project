package com.taotao.cloud.rpc.common.common.util;


import java.util.concurrent.TimeUnit;

/**
 * @since 0.1.3
 */
//@CommonEager
public final class Waits {

    /**
     * 等待指定的时间
     * @param time 时间
     * @param timeUnit 单位
     * @since 0.1.3
     */
    public static void waits(final long time, final TimeUnit timeUnit) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException e) {
//            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 等待指定的时间 s
     * @param time 时间
     * @since 0.1.3
     */
    public static void waits(final long time) {
        waits(time, TimeUnit.SECONDS);
    }

}
