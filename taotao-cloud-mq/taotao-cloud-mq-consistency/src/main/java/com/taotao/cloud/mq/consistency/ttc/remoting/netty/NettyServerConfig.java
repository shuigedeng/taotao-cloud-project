package com.taotao.cloud.mq.consistency.ttc.remoting.netty;

/**
 * Netty 服务端配置
 */
public class NettyServerConfig {

    // 监听端口
    private int listenPort = 8888;

    // Boss线程数（通常1个）
    private int serverBossThreads = 1;

    // Worker线程数（默认CPU核数 * 2）
    private int serverWorkerThreads = 8;

    // Selector线程数
    private int serverSelectorThreads = 3;

    // 回调执行线程数 (默认 4，避免 0 触发 ThreadPoolExecutor IllegalArgumentException)
    private int serverCallbackExecutorThreads = 4;

    // 异步信号量值
    private int serverAsyncSemaphoreValue = 64;

    // 单向发送信号量值
    private int serverOnewaySemaphoreValue = 256;

    // Socket发送缓冲区大小
    private int serverSocketSndBufSize = 65535;

    // Socket接收缓冲区大小
    private int serverSocketRcvBufSize = 65535;

    // Socket backlog
    private int serverSocketBacklog = 1024;

    // 通道最大空闲时间（秒）
    private int serverChannelMaxIdleTimeSeconds = 120;

    // 是否使用Epoll（Linux）
    private boolean useEpollNativeSelector = false;

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getServerBossThreads() {
        return serverBossThreads;
    }

    public void setServerBossThreads(int serverBossThreads) {
        this.serverBossThreads = serverBossThreads;
    }

    public int getServerWorkerThreads() {
        return serverWorkerThreads;
    }

    public void setServerWorkerThreads(int serverWorkerThreads) {
        this.serverWorkerThreads = serverWorkerThreads;
    }

    public int getServerSelectorThreads() {
        return serverSelectorThreads;
    }

    public void setServerSelectorThreads(int serverSelectorThreads) {
        this.serverSelectorThreads = serverSelectorThreads;
    }

    public int getServerCallbackExecutorThreads() {
        return serverCallbackExecutorThreads;
    }

    public void setServerCallbackExecutorThreads(int serverCallbackExecutorThreads) {
        this.serverCallbackExecutorThreads = serverCallbackExecutorThreads;
    }

    public int getServerAsyncSemaphoreValue() {
        return serverAsyncSemaphoreValue;
    }

    public void setServerAsyncSemaphoreValue(int serverAsyncSemaphoreValue) {
        this.serverAsyncSemaphoreValue = serverAsyncSemaphoreValue;
    }

    public int getServerOnewaySemaphoreValue() {
        return serverOnewaySemaphoreValue;
    }

    public void setServerOnewaySemaphoreValue(int serverOnewaySemaphoreValue) {
        this.serverOnewaySemaphoreValue = serverOnewaySemaphoreValue;
    }

    public int getServerSocketSndBufSize() {
        return serverSocketSndBufSize;
    }

    public void setServerSocketSndBufSize(int serverSocketSndBufSize) {
        this.serverSocketSndBufSize = serverSocketSndBufSize;
    }

    public int getServerSocketRcvBufSize() {
        return serverSocketRcvBufSize;
    }

    public void setServerSocketRcvBufSize(int serverSocketRcvBufSize) {
        this.serverSocketRcvBufSize = serverSocketRcvBufSize;
    }

    public int getServerSocketBacklog() {
        return serverSocketBacklog;
    }

    public void setServerSocketBacklog(int serverSocketBacklog) {
        this.serverSocketBacklog = serverSocketBacklog;
    }

    public int getServerChannelMaxIdleTimeSeconds() {
        return serverChannelMaxIdleTimeSeconds;
    }

    public void setServerChannelMaxIdleTimeSeconds(int serverChannelMaxIdleTimeSeconds) {
        this.serverChannelMaxIdleTimeSeconds = serverChannelMaxIdleTimeSeconds;
    }

    public boolean isUseEpollNativeSelector() {
        return useEpollNativeSelector;
    }

    public void setUseEpollNativeSelector(boolean useEpollNativeSelector) {
        this.useEpollNativeSelector = useEpollNativeSelector;
    }

    @Override
    public String toString() {
        return "NettyServerConfig{" +
                "listenPort=" + listenPort +
                ", serverBossThreads=" + serverBossThreads +
                ", serverWorkerThreads=" + serverWorkerThreads +
                ", serverSelectorThreads=" + serverSelectorThreads +
                ", serverCallbackExecutorThreads=" + serverCallbackExecutorThreads +
                ", serverAsyncSemaphoreValue=" + serverAsyncSemaphoreValue +
                ", serverOnewaySemaphoreValue=" + serverOnewaySemaphoreValue +
                ", serverSocketSndBufSize=" + serverSocketSndBufSize +
                ", serverSocketRcvBufSize=" + serverSocketRcvBufSize +
                ", serverSocketBacklog=" + serverSocketBacklog +
                ", serverChannelMaxIdleTimeSeconds=" + serverChannelMaxIdleTimeSeconds +
                ", useEpollNativeSelector=" + useEpollNativeSelector +
                '}';
    }
}
