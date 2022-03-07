package com.taotao.cloud.core.heaven.util.time;


/**
 * 时间工具类
 * （1）便于后期拓展，可以使用统一的时钟服务。
 */
public interface Time {

    /**
     * 获取当前时间
     * @return 当前时间
     * @since 0.0.6
     */
    long time();

}
