package com.taotao.cloud.rpc.spring.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ttc-RPC 配置属性（顶层）
 */
@ConfigurationProperties(prefix = "ttc.rpc")
public class TtcRpcProperties {

    private boolean enabled = true;
    private Application application = new Application();
    private Registry registry = new Registry();
    private Server server = new Server();
    private Consumer consumer = new Consumer();
    private Provider provider = new Provider();
    private Protocol protocol = new Protocol();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public static class Application {
        private String name = "ttc-rpc-app";
        private String version = "1.0.0";
        private String organization = "zifang";

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getOrganization() { return organization; }
        public void setOrganization(String organization) { this.organization = organization; }
    }

    public static class Registry {
        private String address = "127.0.0.1:8084";
        private String namespace = "public";
        private String type = "ttc-config";
        private boolean enabled = true;

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getNamespace() { return namespace; }
        public void setNamespace(String namespace) { this.namespace = namespace; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class Server {
        private boolean enabled = true;
        private String host = "0.0.0.0";
        private int port = 20880;
        private int threads = 200;
        private int ioThreads = 8;
        private int payload = 8 * 1024 * 1024;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public int getThreads() { return threads; }
        public void setThreads(int threads) { this.threads = threads; }
        public int getIoThreads() { return ioThreads; }
        public void setIoThreads(int ioThreads) { this.ioThreads = ioThreads; }
        public int getPayload() { return payload; }
        public void setPayload(int payload) { this.payload = payload; }
    }

    public static class Consumer {
        private int timeout = 3000;
        private int retries = 2;
        private String loadbalance = "random";
        private String cluster = "failover";
        private int connections = 1;
        private boolean check = false;
        private boolean async = false;
        private String serialization = "hessian2";

        public int getTimeout() { return timeout; }
        public void setTimeout(int timeout) { this.timeout = timeout; }
        public int getRetries() { return retries; }
        public void setRetries(int retries) { this.retries = retries; }
        public String getLoadbalance() { return loadbalance; }
        public void setLoadbalance(String loadbalance) { this.loadbalance = loadbalance; }
        public String getCluster() { return cluster; }
        public void setCluster(String cluster) { this.cluster = cluster; }
        public int getConnections() { return connections; }
        public void setConnections(int connections) { this.connections = connections; }
        public boolean isCheck() { return check; }
        public void setCheck(boolean check) { this.check = check; }
        public boolean isAsync() { return async; }
        public void setAsync(boolean async) { this.async = async; }
        public String getSerialization() { return serialization; }
        public void setSerialization(String serialization) { this.serialization = serialization; }
    }

    public static class Provider {
        private int timeout = 5000;
        private int threads = 200;
        private int delay = 0;
        private int weight = 100;
        private boolean async = false;
        private boolean token = false;
        private String serialization = "hessian2";

        public int getTimeout() { return timeout; }
        public void setTimeout(int timeout) { this.timeout = timeout; }
        public int getThreads() { return threads; }
        public void setThreads(int threads) { this.threads = threads; }
        public int getDelay() { return delay; }
        public void setDelay(int delay) { this.delay = delay; }
        public int getWeight() { return weight; }
        public void setWeight(int weight) { this.weight = weight; }
        public boolean isAsync() { return async; }
        public void setAsync(boolean async) { this.async = async; }
        public boolean isToken() { return token; }
        public void setToken(boolean token) { this.token = token; }
        public String getSerialization() { return serialization; }
        public void setSerialization(String serialization) { this.serialization = serialization; }
    }

    public static class Protocol {
        private String name = "ttc-rpc";
        private int port = 20880;
        private String serialization = "hessian2";
        private int compressThreshold = 1024;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public String getSerialization() { return serialization; }
        public void setSerialization(String serialization) { this.serialization = serialization; }
        public int getCompressThreshold() { return compressThreshold; }
        public void setCompressThreshold(int compressThreshold) { this.compressThreshold = compressThreshold; }
    }
}
