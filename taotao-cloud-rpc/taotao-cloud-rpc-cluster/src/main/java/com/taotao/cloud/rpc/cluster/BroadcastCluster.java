package com.taotao.cloud.rpc.cluster;

import com.taotao.cloud.rpc.cluster.Cluster;
import com.taotao.cloud.rpc.cluster.Directory;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 广播集群
 * <p>
 * 依次调用所有 Invoker，返回最后一个结果。
 * 常用于本地缓存更新、广播通知等场景。
 */
public class BroadcastCluster implements Cluster {

    public static final String NAME = "broadcast";

    private static final Logger log = LogManager.getLogger(BroadcastCluster.class);

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public <T> Invoker<T> join(Directory<T> directory) {
        return new BroadcastClusterInvoker<>(directory);
    }

    private static class BroadcastClusterInvoker<T> implements Invoker<T> {
        private final Directory<T> directory;

        BroadcastClusterInvoker(Directory<T> directory) {
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
        public Result invoke(Invocation invocation) {
            List<Invoker<T>> invokers = directory.list();
            if (invokers == null || invokers.isEmpty()) {
                throw com.taotao.cloud.rpc.common.RpcException.noProvider(
                        "No provider for broadcast: " + getInterface().getName());
            }
            Result result = null;
            List<Throwable> errors = new ArrayList<>();
            for (Invoker<T> invoker : invokers) {
                try {
                    result = invoker.invoke(invocation);
                } catch (Throwable e) {
                    errors.add(e);
                    log.warn("Broadcast invoke {} failed: {}", invoker.getUrl(), e.getMessage());
                }
            }
            if (result == null && !errors.isEmpty()) {
                throw com.taotao.cloud.rpc.common.RpcException.biz(
                        "All broadcast invokers failed: " + errors.size() + " errors");
            }
            return result;
        }
    }
}
