package com.taotao.cloud.lock.kylin.enums;

/**
 * 锁类型
 */
public enum LockType {
    /**
     * 重入锁
     */
    REENTRANT,
    /**
     * 读锁
     */
    READ,
    /**
     * 写锁
     */
    WRITE,
    /**
     * 联锁
     */
    MULTI,
    /**
     * 红锁，仅redisson
     */
    RED,
    /**
     * 公平锁，仅redisson
     */
    FAIR,
    /**
     * 不可重入锁，仅zk
     */
    SEMAPHORE,
    ;
}
