package com.taotao.cloud.rpc.core.cluster;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 失败重试集群
 * 当调用失败时，会重试其他 invoker，默认重试 2 次
 */
public class FailoverCluster implements Cluster {

    public static final String NAME = "failover";

    // 默认重试次数
    private static final int DEFAULT_RETRIES = 2;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public <T> Invoker<T> join(Directory<T> directory) {
        return new FailoverClusterInvoker<>(directory);
    }

    /**
     * Failover 集群 Invoker
     */
    private static class FailoverClusterInvoker<T> implements Invoker<T> {

        private final Logger log = LogManager.getLogger(this.getClass());

        private final Directory<T> directory;

        FailoverClusterInvoker(Directory<T> directory) {
            this.directory = directory;
        }

        @Override
        public Class<T> getInterface() {
            return directory.getInterface();
        }

        @Override
        public URL getUrl() {
            return directory.getUrl();
        }

        @Override
        public boolean isAvailable() {
            return !directory.isDestroyed() && !directory.list().isEmpty();
        }

        @Override
        public void destroy() {
            directory.destroy();
        }

        @Override
        public Result invoke(Invocation invocation) throws Throwable {
            // 获取重试次数
            int retries = getRetries();

            // 获取所有可用的 invokers
            List<Invoker<T>> invokers = directory.list();
            checkInvokers(invokers, invocation);

            // 记录最后调用的 invoker 索引
            int lastIndex = 0;

            for (int i = 0; i <= retries; i++) {
                // 每次重试都重新获取 invokers（可能已变更）
                if (i > 0) {
                    invokers = directory.list();
                    checkInvokers(invokers, invocation);
                }

                try {
                    // 选择 invoker（重试时尝试不同的 invoker）
                    Invoker<T> invoker = select(invokers, lastIndex, invocation);
                    Result result = invoker.invoke(invocation);
                    return result;
                } catch (Throwable e) {
                    // 记录失败的 invoker
                    lastIndex = (lastIndex + 1) % invokers.size();

                    // 如果是最后一次重试，抛出异常
                    if (i >= retries) {
                        throw e;
                    }

                    log.warn("Invoke failed, retry {}/{}, error: {}", i + 1, retries, e.getMessage());
                }
            }

            throw new IllegalStateException("Should never reach here");
        }

        private int getRetries() {
            URL url = directory.getUrl();
            String retriesStr = url.getParameter("retries");
            if (retriesStr != null) {
                try {
                    return Integer.parseInt(retriesStr);
                } catch (NumberFormatException e) {
                    log.warn("Invalid retries value: {}, using default: {}", retriesStr, DEFAULT_RETRIES);
                }
            }
            return DEFAULT_RETRIES;
        }

        private void checkInvokers(List<Invoker<T>> invokers, Invocation invocation) {
            if (invokers == null || invokers.isEmpty()) {
                throw new IllegalStateException(
                        "No provider available for service " + directory.getInterface().getName()
                                + " from registry " + directory.getUrl()
                                + " on the consumer " + getHostName()
                                + ", please check if the service is registered.");
            }
        }

        private Invoker<T> select(List<Invoker<T>> invokers, int excludeIndex, Invocation invocation) {
            if (invokers.size() == 1) {
                return invokers.get(0);
            }

            // 简单选择：如果 excludeIndex 有效，选择下一个
            int selectIndex;
            if (excludeIndex >= 0 && excludeIndex < invokers.size() - 1) {
                selectIndex = excludeIndex + 1;
            } else {
                selectIndex = 0;
            }
            return invokers.get(selectIndex);
        }

        private String getHostName() {
            try {
                return java.net.InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
                return "localhost";
            }
        }
    }
}
