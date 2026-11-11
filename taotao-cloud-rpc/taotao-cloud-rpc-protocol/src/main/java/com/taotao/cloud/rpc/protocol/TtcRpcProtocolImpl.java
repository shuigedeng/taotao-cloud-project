package com.taotao.cloud.rpc.protocol;

import com.taotao.cloud.rpc.common.api.Exporter;
import com.taotao.cloud.rpc.common.api.Protocol;
import com.taotao.cloud.rpc.common.ProtocolConstants;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.remoting.RpcClient;

/**
 * ttc-RPC Protocol SPI 实现
 * <p>
 * 这是 ttc-RPC 私有二进制协议的实现。
 * <p>
 * 该实现核心是协调 {@link TtcRpcMessageEncoder}/{@link TtcRpcMessageDecoder}
 * 与 Netty 服务端/客户端，组装完整的 RPC 调用链路。
 */
public class TtcRpcProtocolImpl implements Protocol {

    @Override
    public int getDefaultPort() {
        return ProtocolConstants.DEFAULT_PORT;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) {
        URL url = invoker.getUrl();
        // 简化实现：实际生产中这里会启动 NettyServer 并绑定端口
        // 当前阶段委托给现有的 remoting 层的 RpcServer
        return new TtcRpcExporter<>(invoker, url);
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url, URL consumerUrl) {
        // 简化实现：实际生产中这里会创建 NettyClient 连接
        return new TtcRpcInvoker<>(type, url, consumerUrl);
    }

    @Override
    public void destroy() {
        // 清理所有连接
    }

    /**
     * ttc-RPC 服务端暴露器
     */
    public static class TtcRpcExporter<T> implements Exporter<T> {
        private final Invoker<T> invoker;
        private final URL url;
        private volatile boolean unexported = false;

        public TtcRpcExporter(Invoker<T> invoker, URL url) {
            this.invoker = invoker;
            this.url = url;
        }

        @Override
        public Invoker<T> getInvoker() {
            return invoker;
        }

        @Override
        public void unexport() {
            unexported = true;
        }

        public URL getUrl() {
            return url;
        }

        public boolean isUnexported() {
            return unexported;
        }
    }

    /**
     * ttc-RPC 客户端 Invoker
     * <p>
     * 消费链路委托给 {@link RpcClient}：首次调用时按 provider URL 的 host:port 建立连接，
     * 之后复用同一条连接。连接建立失败按调用失败处理（返回 error result），
     * 不在 {@code refer()} 阶段抛出。
     */
    public static class TtcRpcInvoker<T> implements Invoker<T> {
        private final Class<T> type;
        private final URL url;
        private final URL consumerUrl;
        private final Object clientLock = new Object();
        private volatile RpcClient client;

        public TtcRpcInvoker(Class<T> type, URL url, URL consumerUrl) {
            this.type = type;
            this.url = url;
            this.consumerUrl = consumerUrl;
        }

        @Override
        public Class<T> getInterface() {
            return type;
        }

        private RpcClient client() {
            RpcClient c = client;
            if (c == null) {
                synchronized (clientLock) {
                    c = client;
                    if (c == null) {
                        c = new RpcClient(url.getHost(), url.getPort());
                        client = c;
                    }
                }
            }
            return c;
        }

        @Override
        public com.taotao.cloud.rpc.common.invoke.Result invoke(com.taotao.cloud.rpc.common.invoke.Invocation invocation) {
            try {
                return client().invoke(invocation, url);
            } catch (Throwable t) {
                return com.taotao.cloud.rpc.common.invoke.Result.error(
                        t instanceof com.taotao.cloud.rpc.common.RpcException
                                ? (com.taotao.cloud.rpc.common.RpcException) t
                                : com.taotao.cloud.rpc.common.RpcException.network(
                                        "Failed to invoke " + invocation.getMethodName()
                                                + " on " + url.getHost() + ":" + url.getPort(), t));
            }
        }

        @Override
        public URL getUrl() {
            return url;
        }

        @Override
        public boolean isAvailable() {
            RpcClient c = client;
            return c != null && c.isConnected();
        }

        @Override
        public void destroy() {
            synchronized (clientLock) {
                RpcClient c = client;
                client = null;
                if (c != null) {
                    c.close();
                }
            }
        }

        public URL getConsumerUrl() {
            return consumerUrl;
        }
    }
}
