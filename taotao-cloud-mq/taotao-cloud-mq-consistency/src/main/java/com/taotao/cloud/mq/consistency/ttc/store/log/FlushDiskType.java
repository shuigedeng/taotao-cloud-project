package com.taotao.cloud.mq.consistency.ttc.store.log;

/**
 * 刷盘类型枚举
 */
public enum FlushDiskType {
    SYNC_FLUSH,  // 同步刷盘
    ASYNC_FLUSH  // 异步刷盘
}
