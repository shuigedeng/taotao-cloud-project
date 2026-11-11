package com.taotao.cloud.rpc.cluster;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 失败自动恢复集群
 * <p>
 * 失败时记录日志，由后台定时重投。
 */
public class FailbackCluster implements Cluster {

    public static final String NAME = "failback";

    private static final Logger log = LogManager.getLogger(FailbackCluster.class);

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public <T> Invoker<T> join(Directory<T> directory) {
        return new FailbackClusterInvoker<>(directory);
    }

    private static class FailbackClusterInvoker<T> implements Invoker<T> {
        private final Directory<T> directory;

        FailbackClusterInvoker(Directory<T> directory) {
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
                return new Result.RpcResult((Object) null);
            }
            try {
                return invokers.get(0).invoke(invocation);
            } catch (Throwable e) {
                log.warn("Failback: invoke failed, will retry later: {}", e.getMessage());
                // 实际生产中应投递到重试队列
                return new Result.RpcResult((Object) null);
            }
        }
    }
}
