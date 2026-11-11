package com.taotao.cloud.rpc.cluster;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 快速失败集群
 * <p>
 * 失败立即抛异常，不重试。
 */
public class FailfastCluster implements Cluster {

    public static final String NAME = "failfast";

    private static final Logger log = LogManager.getLogger(FailfastCluster.class);

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public <T> Invoker<T> join(Directory<T> directory) {
        return new FailfastClusterInvoker<>(directory);
    }

    private static class FailfastClusterInvoker<T> implements Invoker<T> {
        private final Directory<T> directory;

        FailfastClusterInvoker(Directory<T> directory) {
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
            List<Invoker<T>> invokers = directory.list();
            if (invokers == null || invokers.isEmpty()) {
                throw com.taotao.cloud.rpc.common.RpcException.noProvider(
                        "No provider available for " + getInterface().getName());
            }
            // 直接选一个调用
            Invoker<T> invoker = invokers.get(0);
            try {
                return invoker.invoke(invocation);
            } catch (Throwable e) {
                log.warn("Failfast invoke failed: {}", e.getMessage());
                throw e;
            }
        }
    }
}
