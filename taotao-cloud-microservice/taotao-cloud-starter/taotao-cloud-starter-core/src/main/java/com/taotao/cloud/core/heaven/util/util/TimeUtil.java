package com.taotao.cloud.core.heaven.util.util;


import com.taotao.cloud.core.heaven.response.exception.CommonRuntimeException;

/**
 * 时间工具类
 * @see DateUtil 时间工具类
 */
@Deprecated
public final class TimeUtil {

    private TimeUtil(){}

    /**
     * 当前线程主动沉睡
     * @param pauseMills 暂定的毫秒数
     * @since 0.1.103
     */
    public static void sleep(final long pauseMills) {
        if(pauseMills <= 0) {
            return;
        }

        try {
            Thread.sleep(pauseMills);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CommonRuntimeException(e);
        }
    }

}
