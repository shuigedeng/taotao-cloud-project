package com.taotao.cloud.rpc.cluster.config;

import com.taotao.boot.common.utils.common.RandomUtils;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.core.cluster.AbstractDirectory;
import com.taotao.cloud.rpc.registry.NotifyListener;
import com.taotao.cloud.rpc.registry.RegistryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心目录实现
 * 从注册中心动态获取服务提供者列表
 */
public class RegistryDirectory<T> extends AbstractDirectory<T> implements NotifyListener {

    private final Logger log = LogManager.getLogger(this.getClass());

    private final RegistryService registryService;
    private final String serviceKey;

    // 方法名 -> Invoker 列表缓存（用于路由）
    private final Map<String, List<Invoker<T>>> methodInvokerMap = new ConcurrentHashMap<>();

    // URL 地址 -> Invoker 映射
    private final Map<String, Invoker<T>> urlInvokerMap = new ConcurrentHashMap<>();

    public RegistryDirectory(Class<T> serviceType, URL consumerUrl, RegistryService registryService) {
        super(serviceType, consumerUrl);
        this.registryService = registryService;
        this.serviceKey = consumerUrl.getServiceKey();

        // 订阅服务变更
        subscribe();
    }

    /**
     * 订阅服务
     */
    private void subscribe() {
        try {
            URL subscribeUrl = new URL();
            subscribeUrl.setServiceInterface(serviceType.getName());
            subscribeUrl.setGroup(url.getGroup());
            subscribeUrl.setVersion(url.getVersion());

            registryService.subscribe(subscribeUrl, this);
            log.info("Subscribed to service: {}", serviceType.getName());
        } catch (Exception e) {
            log.error("Failed to subscribe service: {}", serviceType.getName(), e);
        }
    }

    @Override
    protected List<Invoker<T>> doList() {
        // 返回所有可用的 invokers
        List<Invoker<T>> invokers = new ArrayList<>(urlInvokerMap.values());
        // 应用路由规则
        return route(invokers, url, null);
    }

    @Override
    public void notify(List<URL> urls) {
        if (urls == null || urls.isEmpty()) {
            return;
        }

        log.info("Received service list update, size: {}", urls.size());

        // 刷新 invokers
        refreshInvokers(urls);
    }

    @Override
    public void onServiceAdded(URL url) {
        log.info("Service instance added: {}", url.getAddress());
        // 重新查询完整列表
        List<URL> urls = registryService.lookup(url);
        notify(urls);
    }

    @Override
    public void onServiceRemoved(URL url) {
        log.info("Service instance removed: {}", url.getAddress());
        // 移除对应的 invoker
        String key = url.getAddress();
        Invoker<T> invoker = urlInvokerMap.remove(key);
        if (invoker != null) {
            try {
                invoker.destroy();
            } catch (Exception e) {
                log.error("Failed to destroy invoker: {}", invoker, e);
            }
        }
    }

    /**
     * 刷新 invokers
     */
    private synchronized void refreshInvokers(List<URL> urls) {
        // 新的 URL 地址集合
        java.util.Set<String> newUrls = new java.util.HashSet<>();
        for (URL url : urls) {
            newUrls.add(url.getAddress());
        }

        // 移除不再存在的 invoker
        java.util.Iterator<Map.Entry<String, Invoker<T>>> it = urlInvokerMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Invoker<T>> entry = it.next();
            if (!newUrls.contains(entry.getKey())) {
                try {
                    entry.getValue().destroy();
                } catch (Exception e) {
                    log.error("Failed to destroy invoker: {}", entry.getValue(), e);
                }
                it.remove();
            }
        }

        // 添加新的 invoker
        for (URL url : urls) {
            String key = url.getAddress();
            if (!urlInvokerMap.containsKey(key)) {
                Invoker<T> invoker = createInvoker(url);
                if (invoker != null) {
                    urlInvokerMap.put(key, invoker);
                }
            }
        }

        log.info("Invokers refreshed, total: {}", urlInvokerMap.size());
    }

    /**
     * 创建 Invoker
     */
    private Invoker<T> createInvoker(URL url) {
        try {
            return new DubboInvoker<>(serviceType, url, consumerUrl);
        } catch (Exception e) {
            log.error("Failed to create invoker for: {}", url, e);
            return null;
        }
    }

    /**
     * Dubbo Invoker 实现
     */
    private static class DubboInvoker<T> implements Invoker<T> {

        private final Class<T> type;
        private final URL url;
        private final URL consumerUrl;
        private final com.taotao.cloud.rpc.remoting.RpcClient rpcClient;
        private volatile boolean available = true;

        DubboInvoker(Class<T> type, URL url, URL consumerUrl) {
            this.type = type;
            this.url = url;
            this.consumerUrl = consumerUrl;
            this.rpcClient = new com.taotao.cloud.rpc.remoting.RpcClient(url.getHost(), url.getPort());
        }

        @Override
        public Class<T> getInterface() {
            return type;
        }

        @Override
        public URL getUrl() {
            return url;
        }

        @Override
        public boolean isAvailable() {
            return available && rpcClient.isConnected();
        }

        @Override
        public void destroy() {
            available = false;
            rpcClient.close();
        }

        @Override
        public com.taotao.cloud.rpc.common.invoke.Result invoke(com.taotao.cloud.rpc.common.invoke.Invocation invocation) throws Throwable {
            try {
                // 构建 RPC 请求
                com.taotao.cloud.rpc.remoting.RpcRequest request = new com.taotao.cloud.rpc.remoting.RpcRequest();
                request.setRequestId(RandomUtils.randomString(10));
                request.setInterfaceName(type.getName());
                request.setMethodName(invocation.getMethodName());
                request.setParameterTypes(invocation.getParameterTypes());
                request.setArguments(invocation.getArguments());
                request.setAttachments(invocation.getAttachments());

                // 发送请求
                com.taotao.cloud.rpc.remoting.RpcResponse response = rpcClient.sendRequest(request);

                // 处理响应
                if (response.getException() != null) {
                    throw response.getException();
                }
                return com.taotao.cloud.rpc.common.invoke.Result.success(response.getResult());
            } catch (Exception e) {
                available = false;
                throw e;
            }
        }
    }
}
