package com.taotao.cloud.rpc.cluster;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.core.cluster.Directory;

import java.util.Collections;
import java.util.List;

/**
 * 直连模式 Directory
 * 持有固定的 Invoker 列表，不与注册中心交互
 */
public class StaticDirectory<T> implements Directory<T> {

    private final Class<T> interfaceClass;
    private final URL consumerUrl;
    private volatile List<Invoker<T>> invokers;
    private volatile boolean destroyed = false;

    public StaticDirectory(Class<T> interfaceClass, URL consumerUrl, List<Invoker<T>> invokers) {
        this.interfaceClass = interfaceClass;
        this.consumerUrl = consumerUrl;
        this.invokers = invokers != null ? invokers : Collections.emptyList();
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClass;
    }

    @Override
    public List<Invoker<T>> list() {
        return invokers;
    }

    @Override
    public URL getUrl() {
        return consumerUrl;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void destroy() {
        destroyed = true;
        for (Invoker<T> invoker : invokers) {
            try {
                invoker.destroy();
            } catch (Exception ignored) {}
        }
        invokers = Collections.emptyList();
    }

    public void setInvokers(List<Invoker<T>> invokers) {
        this.invokers = invokers != null ? invokers : Collections.emptyList();
    }
}
