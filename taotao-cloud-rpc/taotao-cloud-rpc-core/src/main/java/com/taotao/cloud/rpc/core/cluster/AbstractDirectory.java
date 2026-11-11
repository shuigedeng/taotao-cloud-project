package com.taotao.cloud.rpc.core.cluster;

import com.taotao.cloud.rpc.core.cluster.Directory;
import com.taotao.cloud.rpc.core.cluster.Router;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 目录抽象实现
 */
public abstract class AbstractDirectory<T> implements Directory<T> {

    // 服务接口类
    protected final Class<T> serviceType;
    // URL
    protected final URL url;
    // 消费者 URL
    protected final URL consumerUrl;
    // 配置项缓存
    protected final Map<String, String> queryMap;
    private final Logger log = LogManager.getLogger(this.getClass());
    // Invoker 列表
    protected volatile List<Invoker<T>> invokers = new CopyOnWriteArrayList<>();
    // 路由链
    protected volatile List<Router> routers = new CopyOnWriteArrayList<>();
    // 是否已销毁
    protected volatile boolean destroyed = false;

    public AbstractDirectory(Class<T> serviceType, URL url) {
        this(serviceType, url, url);
    }

    public AbstractDirectory(Class<T> serviceType, URL url, URL consumerUrl) {
        this.serviceType = serviceType;
        this.url = url;
        this.consumerUrl = consumerUrl;
        this.queryMap = new ConcurrentHashMap<>(url.getParameters());
    }

    @Override
    public Class<T> getInterface() {
        return serviceType;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    public URL getConsumerUrl() {
        return consumerUrl;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public synchronized void destroy() {
        if (destroyed) {
            return;
        }
        destroyed = true;

        // 销毁所有 invoker
        for (Invoker<T> invoker : invokers) {
            try {
                invoker.destroy();
            } catch (Exception e) {
                log.error("Failed to destroy invoker: {}", invoker, e);
            }
        }
        invokers.clear();
        routers.clear();

        log.info("Directory destroyed: {}", serviceType.getName());
    }

    @Override
    public List<Invoker<T>> list() {
        if (destroyed) {
            return Collections.emptyList();
        }
        return doList();
    }

    /**
     * 子类实现列表获取
     */
    protected abstract List<Invoker<T>> doList();

    /**
     * 设置 invoker 列表
     */
    public void setInvokers(List<Invoker<T>> invokers) {
        this.invokers = invokers != null ? new CopyOnWriteArrayList<>(invokers) : new CopyOnWriteArrayList<>();
    }

    /**
     * 添加 invoker
     */
    public void addInvoker(Invoker<T> invoker) {
        if (invoker != null && !invokers.contains(invoker)) {
            invokers.add(invoker);
        }
    }

    /**
     * 移除 invoker
     */
    public void removeInvoker(Invoker<T> invoker) {
        invokers.remove(invoker);
    }

    /**
     * 添加路由
     */
    public void addRouter(Router router) {
        if (router != null && !routers.contains(router)) {
            routers.add(router);
        }
    }

    /**
     * 移除路由
     */
    public void removeRouter(Router router) {
        routers.remove(router);
    }

    /**
     * 应用路由过滤
     */
    protected List<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        List<Invoker<T>> result = invokers;
        for (Router router : routers) {
            result = router.route(result, url, invocation);
            if (result == null || result.isEmpty()) {
                return Collections.emptyList();
            }
        }
        return result;
    }
}
