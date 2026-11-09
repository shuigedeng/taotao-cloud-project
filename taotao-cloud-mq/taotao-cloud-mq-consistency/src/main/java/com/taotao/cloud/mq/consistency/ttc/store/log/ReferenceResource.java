package com.taotao.cloud.mq.consistency.ttc.store.log;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 引用资源基类
 * 提供引用计数功能，用于资源的生命周期管理
 */
public abstract class ReferenceResource {

    // 引用计数
    protected final AtomicLong refCount = new AtomicLong(1);

    // 是否可用
    protected volatile boolean available = true;

    // 是否清理完成
    protected volatile boolean cleanupOver = false;
    // 文件对象（用于获取最后修改时间）
    protected java.io.File file;
    // 第一次关闭时间戳
    private volatile long firstShutdownTimestamp = 0;

    /**
     * 持有引用
     */
    public synchronized boolean hold() {
        if (this.isAvailable()) {
            if (this.refCount.getAndIncrement() > 0) {
                return true;
            } else {
                this.refCount.getAndDecrement();
            }
        }
        return false;
    }

    /**
     * 释放引用
     */
    public void release() {
        long value = this.refCount.decrementAndGet();
        if (value > 0) {
            return;
        }

        synchronized (this) {
            this.cleanupOver = this.cleanup(value);
        }
    }

    /**
     * 清理资源（子类实现）
     */
    public abstract boolean cleanup(final long currentRef);

    /**
     * 是否可用
     */
    public boolean isAvailable() {
        return this.available;
    }

    /**
     * 关闭资源
     */
    public void shutdown(final long intervalForcibly) {
        if (this.available) {
            this.available = false;
            this.firstShutdownTimestamp = System.currentTimeMillis();
            this.release();
        } else if (this.getRefCount() > 0) {
            if ((System.currentTimeMillis() - this.firstShutdownTimestamp) >= intervalForcibly) {
                this.refCount.set(-1000 - this.getRefCount());
                this.release();
            }
        }
    }

    /**
     * 获取引用计数
     */
    public long getRefCount() {
        return this.refCount.get();
    }

    /**
     * 是否清理完成
     */
    public boolean isCleanupOver() {
        return this.refCount.get() <= 0 && this.cleanupOver;
    }

    /**
     * 获取最后修改时间
     */
    public long getLastModifiedTimestamp() {
        return file != null ? file.lastModified() : 0;
    }

    public void setFile(java.io.File file) {
        this.file = file;
    }
}
