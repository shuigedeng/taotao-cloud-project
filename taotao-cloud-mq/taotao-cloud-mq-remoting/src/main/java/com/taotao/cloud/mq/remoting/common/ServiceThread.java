package com.taotao.cloud.mq.remoting.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务线程基类
 * 提供标准的线程生命周期管理
 */
public abstract class ServiceThread implements Runnable {

    protected static final Logger log = LoggerFactory.getLogger(ServiceThread.class);

    // 线程停止标志
    protected final AtomicBoolean stopped = new AtomicBoolean(false);

    // 线程是否已启动
    protected final AtomicBoolean started = new AtomicBoolean(false);

    // 线程实例
    protected Thread thread;

    // 线程名称
    protected String threadName;

    // 是否守护线程
    protected boolean daemon = false;

    public ServiceThread() {
    }

    public ServiceThread(String threadName) {
        this.threadName = threadName;
    }

    /**
     * 启动服务线程
     */
    public void start() {
        if (started.compareAndSet(false, true)) {
            stopped.set(false);
            thread = new Thread(this, getServiceName());
            thread.setDaemon(daemon);
            thread.start();
            log.info("Service thread started: {}", getServiceName());
        }
    }

    /**
     * 停止服务线程
     */
    public void shutdown() {
        if (started.compareAndSet(true, false)) {
            stopped.set(true);
            if (thread != null) {
                thread.interrupt();
                try {
                    thread.join(5000);
                    if (thread.isAlive()) {
                        log.warn("Service thread did not stop gracefully: {}", getServiceName());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("Interrupted while waiting for service thread to stop: {}", getServiceName());
                }
            }
            log.info("Service thread stopped: {}", getServiceName());
        }
    }

    /**
     * 优雅停止（等待处理完成）
     */
    public void shutdownGracefully(long timeout, TimeUnit unit) {
        if (started.compareAndSet(true, false)) {
            stopped.set(true);
            if (thread != null) {
                thread.interrupt();
                try {
                    thread.join(unit.toMillis(timeout));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * 检查是否已停止
     */
    public boolean isStopped() {
        return stopped.get();
    }

    /**
     * 检查是否已启动
     */
    public boolean isStarted() {
        return started.get();
    }

    /**
     * 等待线程结束
     */
    public void join() throws InterruptedException {
        if (thread != null) {
            thread.join();
        }
    }

    /**
     * 等待线程结束（带超时）
     */
    public void join(long millis) throws InterruptedException {
        if (thread != null) {
            thread.join(millis);
        }
    }

    /**
     * 获取服务名称
     */
    public abstract String getServiceName();

    /**
     * 设置守护线程
     */
    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    /**
     * 获取线程名称
     */
    public String getThreadName() {
        if (threadName != null) {
            return threadName;
        }
        return getServiceName();
    }

    @Override
    public String toString() {
        return "ServiceThread{" +
                "stopped=" + stopped +
                ", started=" + started +
                ", threadName='" + threadName + '\'' +
                ", daemon=" + daemon +
                '}';
    }
}
