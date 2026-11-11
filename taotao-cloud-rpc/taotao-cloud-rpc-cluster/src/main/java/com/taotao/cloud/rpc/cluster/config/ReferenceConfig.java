package com.taotao.cloud.rpc.cluster.config;

import com.taotao.cloud.rpc.cluster.StaticDirectory;
import com.taotao.cloud.rpc.common.api.JdkProxyFactory;
import com.taotao.cloud.rpc.common.api.ProxyFactory;
import com.taotao.cloud.rpc.core.cluster.Directory;
import com.taotao.cloud.rpc.core.cluster.FailoverCluster;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.registry.RegistryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

// import com.taotao.cloud.rpc.registry.ZConfigRegistry; // 暂时注释 (依赖 ttc-config-* 未发布)

/**
 * 服务引用配置
 * 用于配置和获取远程服务代理
 */
public class ReferenceConfig<T> {

    private final Logger log = LogManager.getLogger(this.getClass());

    // ========== 服务基本信息 ==========
    /**
     * 是否已销毁
     */
    private final AtomicBoolean destroyed = new AtomicBoolean(false);
    /**
     * 服务接口类
     */
    private Class<T> interfaceClass;

    // ========== 版本与分组 ==========
    /**
     * 服务接口名
     */
    private String interfaceName;
    /**
     * 服务版本
     */
    private String version = "1.0.0";

    // ========== 调用参数 ==========
    /**
     * 服务分组
     */
    private String group = "";
    /**
     * 超时时间（毫秒）
     */
    private int timeout = 3000;

    // ========== 集群与负载均衡 ==========
    /**
     * 重试次数
     */
    private int retries = 2;
    /**
     * 负载均衡策略
     */
    private String loadbalance = "random";

    // ========== 注册中心 ==========
    /**
     * 集群容错策略
     */
    private String cluster = "failover";

    /**
     * 直连服务地址列表（绕过注册中心）
     * 例如 "ttcrpc://192.168.1.10:20880" 或多个以分号分隔
     */
    private String url;

    // ========== 内部状态 ==========
    /**
     * 注册中心地址
     */
    private String registry = "127.0.0.1:8084";
    /**
     * 是否已初始化
     */
    private volatile boolean initialized = false;
    /**
     * 服务代理
     */
    private volatile T ref;

    // ========== 内部组件 ==========

    /**
     * 注册中心服务
     */
    private RegistryService registryService;

    /**
     * 集群 Invoker
     */
    private Invoker<T> clusterInvoker;
    private boolean async;
    private boolean oneway;

    // ========== 公共方法 ==========

    /**
     * 获取服务代理
     */
    public synchronized T get() {
        if (destroyed.get()) {
            throw new IllegalStateException("ReferenceConfig has been destroyed");
        }
        if (ref == null) {
            init();
        }
        return ref;
    }

    /**
     * 销毁
     */
    public synchronized void destroy() {
        if (destroyed.compareAndSet(false, true)) {
            if (clusterInvoker != null) {
                try {
                    clusterInvoker.destroy();
                } catch (Exception e) {
                    log.error("Failed to destroy cluster invoker", e);
                }
            }
            if (registryService != null) {
                try {
                    registryService.destroy();
                } catch (Exception e) {
                    log.error("Failed to destroy registry service", e);
                }
            }
            ref = null;
            initialized = false;
            log.info("ReferenceConfig destroyed: {}", interfaceName);
        }
    }

    // ========== 私有方法 ==========

    private void init() {
        if (initialized) {
            return;
        }

        // 检查配置
        checkConfig();

        // 连接注册中心
        connectRegistry();

        // 创建服务代理
        createProxy();

        initialized = true;
        log.info("ReferenceConfig initialized: {}", interfaceName);
    }

    private void checkConfig() {
        if (interfaceClass == null && (interfaceName == null || interfaceName.isEmpty())) {
            throw new IllegalStateException("interfaceClass or interfaceName is required");
        }

        // 解析接口类
        if (interfaceClass == null) {
            try {
                interfaceClass = (Class<T>) Class.forName(interfaceName);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Interface class not found: " + interfaceName, e);
            }
        }

        if (!interfaceClass.isInterface()) {
            throw new IllegalStateException("Interface class must be an interface: " + interfaceClass.getName());
        }

        if (interfaceName == null || interfaceName.isEmpty()) {
            interfaceName = interfaceClass.getName();
        }
    }

    private void connectRegistry() {
        if (url != null && !url.isEmpty()) {
            log.info("Reference using direct URL mode (bypass registry): {}", url);
            return;
        }
        if (registry == null || registry.isEmpty()) {
            throw new IllegalStateException("Registry address is required (or set direct url)");
        }

        // ZConfigRegistry 暂时禁用 (依赖 ttc-config-* 未发布,后续 Central 上架后再加)
        // registryService = new ZConfigRegistry(registry);
        log.warn("ZConfigRegistry 适配暂时禁用,仅保留直连模式");
        log.info("Connected to registry: {}", registry);
    }

    private void createProxy() {
        // 创建目录
        Directory<T> directory = createDirectory();

        // 创建集群 Invoker
        FailoverCluster cluster = new FailoverCluster();
        clusterInvoker = cluster.join(directory);

        // 创建代理
        ProxyFactory proxyFactory = new JdkProxyFactory();
        ref = proxyFactory.getProxy(clusterInvoker);

        log.info("Service proxy created: {}", interfaceName);
    }

    private Directory<T> createDirectory() {
        URL consumerUrl = new URL();
        consumerUrl.setProtocol("consumer");
        consumerUrl.setHost(getLocalHost());
        consumerUrl.setPort(0);
        consumerUrl.setServiceInterface(interfaceName);
        consumerUrl.setGroup(group);
        consumerUrl.setVersion(version);
        consumerUrl.addParameter("timeout", String.valueOf(timeout));
        consumerUrl.addParameter("retries", String.valueOf(retries));
        consumerUrl.addParameter("loadbalance", loadbalance);
        consumerUrl.addParameter("cluster", cluster);
        consumerUrl.addParameter("side", "consumer");

        // 直连模式：跳过注册中心
        if (url != null && !url.isEmpty()) {
            return new StaticDirectory<>(interfaceClass, consumerUrl, parseDirectUrls());
        }

        return new RegistryDirectory<>(interfaceClass, consumerUrl, registryService);
    }

    /**
     * 解析直连 URL 列表（以分号分隔），如：
     * "ttcrpc://192.168.1.10:20880;ttcrpc://192.168.1.11:20880"
     */
    private List<Invoker<T>> parseDirectUrls() {
        List<Invoker<T>> invokers = new ArrayList<>();
        if (url == null || url.isEmpty()) {
            return invokers;
        }
        String[] urls = url.split(";");
        for (String u : urls) {
            if (u == null || u.trim().isEmpty()) continue;
            URL providerUrl = URL.valueOf(u.trim());
            providerUrl.setServiceInterface(interfaceName);
            providerUrl.setGroup(group);
            providerUrl.setVersion(version);
            providerUrl.addParameter("timeout", String.valueOf(timeout));
            providerUrl.addParameter("side", "provider");

            // 直连 Invoker：通过 Netty 客户端发起 RPC 调用
            final URL finalUrl = providerUrl;
            Invoker<T> invoker = new Invoker<T>() {
                @Override
                public Class<T> getInterface() { return interfaceClass; }
                @Override
                public Result invoke(Invocation invocation) throws Throwable {
                    // 解析实际地址
                    String host = finalUrl.getHost();
                    int port = finalUrl.getPort();
                    log.info("[DirectMode] Invoking {} -> {}:{}", invocation.getMethodName(), host, port);
                    // 通过 Netty 客户端调用
                    com.taotao.cloud.rpc.remoting.RpcClient client =
                            com.taotao.cloud.rpc.remoting.RpcClientHolder.get(host, port);
                    return client.invoke(invocation, finalUrl);
                }
                @Override
                public URL getUrl() { return finalUrl; }
                @Override
                public boolean isAvailable() { return true; }
                @Override
                public void destroy() {}
            };
            invokers.add(invoker);
        }
        return invokers;
    }

    private String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }

    public Logger getLog() {
        return log;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public AtomicBoolean getDestroyed() {
        return destroyed;
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public RegistryService getRegistryService() {
        return registryService;
    }

    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    public Invoker<T> getClusterInvoker() {
        return clusterInvoker;
    }

    public void setClusterInvoker(Invoker<T> clusterInvoker) {
        this.clusterInvoker = clusterInvoker;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isOneway() {
        return oneway;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }
}
