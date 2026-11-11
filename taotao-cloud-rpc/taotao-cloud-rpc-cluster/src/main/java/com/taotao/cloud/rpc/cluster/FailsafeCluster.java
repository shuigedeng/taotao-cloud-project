package com.taotao.cloud.rpc.cluster;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 失败安全集群
 * <p>
 * 失败时吞掉异常，返回空结果。常用于日志、审计等非关键场景。
 */
public class FailsafeCluster implements Cluster {

    public static final String NAME = "failsafe";

    private static final Logger log = LogManager.getLogger(FailsafeCluster.class);

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public <T> Invoker<T> join(Directory<T> directory) {
        return new FailsafeClusterInvoker<>(directory);
    }

    private static class FailsafeClusterInvoker<T> implements Invoker<T> {
        private final Directory<T> directory;

        FailsafeClusterInvoker(Directory<T> directory) {
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
            return !directory.isDestroyed();
        }

        @Override
        public void destroy() {
            directory.destroy();
        }

        @Override
        public Result invoke(Invocation invocation) {
            List<Invoker<T>> invokers = directory.list();
            if (invokers == null || invokers.isEmpty()) {
                log.warn("Failsafe: no provider, returning null for {}", getInterface().getName());
                return new Result.RpcResult((Object) null);
            }
            try {
                return invokers.get(0).invoke(invocation);
            } catch (Throwable e) {
                log.warn("Failsafe: invoke failed but ignored: {}", e.getMessage());
                return new Result.RpcResult((Object) null);
            }
        }
    }
}
