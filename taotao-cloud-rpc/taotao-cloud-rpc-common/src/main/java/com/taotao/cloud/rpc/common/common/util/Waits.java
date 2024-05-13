package com.github.houbb.rpc.common.util;

import com.github.houbb.heaven.annotation.CommonEager;
import com.github.houbb.heaven.response.exception.CommonRuntimeException;

import java.util.concurrent.TimeUnit;

/**
 * <p> project: rpc-Waits </p>
 * <p> create on 2019/10/30 21:16 </p>
 *
 * @author Administrator
 * @since 0.1.3
 */
@CommonEager
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
            throw new CommonRuntimeException(e);
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
