package com.taotao.cloud.rpc.cluster.config;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.registry.RegistryService;
import com.taotao.cloud.rpc.remoting.RpcServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务配置
 * 用于配置和导出 RPC 服务
 */
public class ServiceConfig<T> {

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
    /**
     * 服务接口名
     */
    private String interfaceName;

    // ========== 版本与分组 ==========
    /**
     * 服务实现类
     */
    private T ref;
    /**
     * 服务版本
     */
    private String version = "1.0.0";

    // ========== 服务参数 ==========
    /**
     * 服务分组
     */
    private String group = "";
    /**
     * 服务权重
     */
    private int weight = 100;
    /**
     * 延迟导出时间（毫秒）
     */
    private int delay = 0;
    /**
     * 超时时间（毫秒）
     */
    private int timeout = 3000;
    /**
     * 重试次数
     */
    private int retries = 2;

    // ========== 集群与负载均衡 ==========
    /**
     * 线程池大小
     */
    private int threads = 200;
    /**
     * 负载均衡策略
     */
    private String loadbalance = "random";

    // ========== 协议与网络 ==========
    /**
     * 集群容错策略
     */
    private String cluster = "failover";
    /**
     * 协议名称
     */
    private String protocol = "ttc-rpc";
    /**
     * 服务端口号
     */
    private int port = 20880;

    // ========== 注册中心 ==========
    /**
     * 服务主机地址
     */
    private String host;

    // ========== 内部状态 ==========
    /**
     * 注册中心地址
     */
    private String registry;
    /**
     * 是否已导出
     */
    private volatile boolean exported = false;

    // ========== 内部组件 ==========
    /**
     * 服务 URL
     */
    private URL serviceUrl;

    /**
     * RPC 服务器
     */
    private RpcServer rpcServer;

    /**
     * 注册中心服务
     */
    private RegistryService registryService;

    /**
     * 最近一次注册失败的记录；未失败过为 null。
     *
     * <p>注册失败只写日志时，调用方看到的是 {@code export()} 正常返回、
     * {@code isExported()} 为 true —— 服务能连直连地址，但服务发现完全没生效，
     * 且进程里没有任何一格能回答"注册到底成没成"。
     * 这里把失败连同原因存下来，供 {@link #getLastRegistrationFailure()} 查询。
     */
    private volatile String lastRegistrationFailure;

    // ========== 公共方法 ==========

    /**
     * 导出服务
     */
    public synchronized void export() {
        if (exported) {
            return;
        }
        if (destroyed.get()) {
            throw new IllegalStateException("ServiceConfig has been destroyed");
        }

        // 延迟导出
        if (delay > 0) {
            Thread delayThread = new Thread(() -> {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                doExport();
            });
            delayThread.setDaemon(true);
            delayThread.start();
        } else {
            doExport();
        }
    }

    /**
     * 取消导出
     */
    public synchronized void unexport() {
        if (!exported) {
            return;
        }
        if (destroyed.compareAndSet(false, true)) {
            doUnexport();
        }
    }

    // ========== 私有方法 ==========

    private void doExport() {
        if (exported) {
            return;
        }

        // 检查配置
        checkConfig();

        // 构建服务 URL
        buildServiceUrl();

        // 启动 RPC 服务器
        startRpcServer();

        // 注册到注册中心
        registerToRegistry();

        exported = true;
        log.info("Exported service: {}", serviceUrl.getServiceKey());
    }

    private void doUnexport() {
        if (registryService != null && serviceUrl != null) {
            try {
                registryService.unregister(serviceUrl);
            } catch (Exception e) {
                log.error("Failed to unregister service: {}", serviceUrl, e);
            }
        }

        if (rpcServer != null) {
            try {
                rpcServer.stop();
            } catch (Exception e) {
                log.error("Failed to stop RPC server", e);
            }
        }

        exported = false;
        log.info("Unexported service: {}", interfaceName);
    }

    private void checkConfig() {
        if (interfaceClass == null && (interfaceName == null || interfaceName.isEmpty())) {
            throw new IllegalStateException("interfaceClass or interfaceName is required");
        }

        if (ref == null) {
            throw new IllegalStateException("ref is required");
        }

        // 解析接口类
        if (interfaceClass == null) {
            try {
                interfaceClass = (Class<T>) Class.forName(interfaceName);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Interface class not found: " + interfaceName, e);
            }
        }

        if (!interfaceClass.isInstance(ref)) {
            throw new IllegalStateException("ref is not an instance of " + interfaceClass.getName());
        }

        if (interfaceName == null || interfaceName.isEmpty()) {
            interfaceName = interfaceClass.getName();
        }
    }

    private void buildServiceUrl() {
        serviceUrl = new URL();
        serviceUrl.setProtocol(protocol);
        serviceUrl.setHost(host != null ? host : getLocalHost());
        serviceUrl.setPort(port);
        serviceUrl.setServiceInterface(interfaceName);
        serviceUrl.setGroup(group);
        serviceUrl.setVersion(version);

        // 添加参数
        serviceUrl.addParameter("weight", String.valueOf(weight));
        serviceUrl.addParameter("timeout", String.valueOf(timeout));
        serviceUrl.addParameter("retries", String.valueOf(retries));
        serviceUrl.addParameter("threads", String.valueOf(threads));
        serviceUrl.addParameter("loadbalance", loadbalance);
        serviceUrl.addParameter("cluster", cluster);
        serviceUrl.addParameter("side", "provider");
    }

    private void startRpcServer() {
        // setRpcServer() 注入过的就复用；无条件 new 会让同一端口的第二个服务
        // 去重复 bind，也把容器里已经在跑的那台服务器丢掉。
        if (rpcServer == null) {
            rpcServer = new RpcServer(port);
        }
        rpcServer.registerService(interfaceClass, ref);

        // 异步启动服务器
        new Thread(() -> {
            try {
                rpcServer.start();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("RPC server interrupted", e);
            }
        }, "TtcRpcServer-" + port).start();

        // 等待服务器启动
        int waitCount = 0;
        while (!rpcServer.isStarted() && waitCount < 50) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            waitCount++;
        }

        // 发布出去的地址必须是内核真正分配到的端口：绑 0 时 `serviceUrl` 里还写着 0，
        // 注册中心收了这条地址谁就连不上。配置了固定端口时两者相等，行为不变。
        if (rpcServer.isStarted()) {
            serviceUrl.setPort(rpcServer.getPort());
        }

        log.info("RPC server started on port {}", rpcServer.isStarted() ? rpcServer.getPort() : port);
    }

    private void registerToRegistry() {
        if (registry == null || registry.isEmpty()) {
            return;
        }

        // registryService 没有自初始化路径（ZConfigRegistry 适配仍是注释状态），
        // 只有外部显式 setRegistryService 才会有值。没注入就往下走必然 NPE，
        // 而 NPE 会被下面的 catch 吞掉、只留一行日志 —— 事先判掉，让失败有确切原因。
        if (registryService == null) {
            lastRegistrationFailure = "RegistryService is not configured for " + registry;
            log.error("Failed to register service to registry: {} ({})",
                    registry, lastRegistrationFailure);
            return;
        }

        try {
            registryService.register(serviceUrl);
            lastRegistrationFailure = null;
            log.info("Service registered to registry: {}", registry);
        } catch (Exception e) {
            // 注册失败不影响服务启动，只是没有服务发现能力；
            // 但"不影响启动"不等于"调用方可以不知道" —— 记下来供 getLastRegistrationFailure() 查。
            lastRegistrationFailure = e.getClass().getName() + ": " + e.getMessage();
            log.error("Failed to register service to registry: {}", registry, e);
        }
    }

    private String getLocalHost() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
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

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
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

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public boolean isExported() {
        return exported;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }

    public AtomicBoolean getDestroyed() {
        return destroyed;
    }

    public URL getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(URL serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public RpcServer getRpcServer() {
        return rpcServer;
    }

    public void setRpcServer(RpcServer rpcServer) {
        this.rpcServer = rpcServer;
    }

    public RegistryService getRegistryService() {
        return registryService;
    }

    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    /**
     * 最近一次注册失败的描述；从未失败过（或配置了 registry 且注册成功）为 null。
     *
     * <p>没有这一格时，注册失败只存在于日志里：{@code export()} 正常返回、
     * {@code isExported()} 为 true，调用方无从判断服务发现到底生效没有。
     */
    public String getLastRegistrationFailure() {
        return lastRegistrationFailure;
    }
}
