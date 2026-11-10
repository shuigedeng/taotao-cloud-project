package com.taotao.cloud.mq.remoting.netty;

/**
 * Netty 客户端配置
 */
public class NettyClientConfig {

    // Worker线程数
    private int clientWorkerThreads = 4;

    // 回调执行线程数
    private int clientCallbackExecutorThreads = 4;

    // Selector线程数
    private int clientSelectorThreads = 1;

    // 异步信号量值
    private int clientAsyncSemaphoreValue = 65535;

    // 单向发送信号量值
    private int clientOnewaySemaphoreValue = 65535;

    // 连接超时时间（毫秒）
    private int connectTimeoutMillis = 3000;

    // 通道最大空闲时间（秒）
    private int clientChannelMaxIdleTimeSeconds = 120;

    // Socket发送缓冲区大小
    private int clientSocketSndBufSize = 65535;

    // Socket接收缓冲区大小
    private int clientSocketRcvBufSize = 65535;

    public int getClientWorkerThreads() {
        return clientWorkerThreads;
    }

    public void setClientWorkerThreads(int clientWorkerThreads) {
        this.clientWorkerThreads = clientWorkerThreads;
    }

    public int getClientCallbackExecutorThreads() {
        return clientCallbackExecutorThreads;
    }

    public void setClientCallbackExecutorThreads(int clientCallbackExecutorThreads) {
        this.clientCallbackExecutorThreads = clientCallbackExecutorThreads;
    }

    public int getClientSelectorThreads() {
        return clientSelectorThreads;
    }

    public void setClientSelectorThreads(int clientSelectorThreads) {
        this.clientSelectorThreads = clientSelectorThreads;
    }

    public int getClientAsyncSemaphoreValue() {
        return clientAsyncSemaphoreValue;
    }

    public void setClientAsyncSemaphoreValue(int clientAsyncSemaphoreValue) {
        this.clientAsyncSemaphoreValue = clientAsyncSemaphoreValue;
    }

    public int getClientOnewaySemaphoreValue() {
        return clientOnewaySemaphoreValue;
    }

    public void setClientOnewaySemaphoreValue(int clientOnewaySemaphoreValue) {
        this.clientOnewaySemaphoreValue = clientOnewaySemaphoreValue;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public int getClientChannelMaxIdleTimeSeconds() {
        return clientChannelMaxIdleTimeSeconds;
    }

    public void setClientChannelMaxIdleTimeSeconds(int clientChannelMaxIdleTimeSeconds) {
        this.clientChannelMaxIdleTimeSeconds = clientChannelMaxIdleTimeSeconds;
    }

    public int getClientSocketSndBufSize() {
        return clientSocketSndBufSize;
    }

    public void setClientSocketSndBufSize(int clientSocketSndBufSize) {
        this.clientSocketSndBufSize = clientSocketSndBufSize;
    }

    public int getClientSocketRcvBufSize() {
        return clientSocketRcvBufSize;
    }

    public void setClientSocketRcvBufSize(int clientSocketRcvBufSize) {
        this.clientSocketRcvBufSize = clientSocketRcvBufSize;
    }

    @Override
    public String toString() {
        return "NettyClientConfig{" +
                "clientWorkerThreads=" + clientWorkerThreads +
                ", clientCallbackExecutorThreads=" + clientCallbackExecutorThreads +
                ", clientSelectorThreads=" + clientSelectorThreads +
                ", clientAsyncSemaphoreValue=" + clientAsyncSemaphoreValue +
                ", clientOnewaySemaphoreValue=" + clientOnewaySemaphoreValue +
                ", connectTimeoutMillis=" + connectTimeoutMillis +
                ", clientChannelMaxIdleTimeSeconds=" + clientChannelMaxIdleTimeSeconds +
                ", clientSocketSndBufSize=" + clientSocketSndBufSize +
                ", clientSocketRcvBufSize=" + clientSocketRcvBufSize +
                '}';
    }
}
