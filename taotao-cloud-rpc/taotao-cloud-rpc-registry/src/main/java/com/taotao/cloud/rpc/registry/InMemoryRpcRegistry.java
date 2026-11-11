package com.taotao.cloud.rpc.registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 内存版注册中心 (FEATURE) — 单进程测试用，零依赖、毫秒级心跳剔除.
 *
 * <p>对应 nacos 服务注册的 4 个核心能力：
 * <ul>
 *   <li>{@link #register(ServiceInstance)}          — 把实例加入内存表</li>
 *   <li>{@link #deregister(String, String)}         — 主动剔除</li>
 *   <li>{@link #heartbeat(String, String)}          — 续约 + 记录 lastBeatTime</li>
 *   <li>{@link #discover(String)}                   — 拉取当前所有实例 (健康/不健康都返，由 caller 过滤)</li>
 *   <li>{@link #subscribe(String, Consumer)}        — 注册变更推送</li>
 * </ul>
 *
 * <p>心跳剔除: 后台 {@link ScheduledExecutorService} 每 {@link #scanIntervalMs} 毫秒扫一次，
 * 超过 {@link #expireMs} 未续约的实例自动标 unhealthy 并推 DEREGISTER 事件给订阅者.
 *
 * <p>线程安全: 用 ConcurrentHashMap 做主表, CopyOnWriteArrayList 做订阅者列表,
 * 最后所有 publish 都在 publish 线程同步顺序推，避免订阅者回调被打断.
 */
public class InMemoryRpcRegistry implements RpcRegistry {

    private static final Logger log = LogManager.getLogger(InMemoryRpcRegistry.class);

    /** 心跳存活窗口 (millis). 实例超过这个时间未 heartbeat 将被判定过期. 默认 30s. */
    private final long expireMs;
    /** 扫描间隔 (millis). 默认 1s. */
    private final long scanIntervalMs;
    /** 心跳建议间隔 (sec, 作为 heartbeat() 返回值给 Server). 默认 5s. */
    private final long heartbeatIntervalSec;

    /** 主表: serviceName → instanceId → 记录 (含 lastBeatTime 和 healthy) */
    private final Map<String, Map<String, Internal>> table = new ConcurrentHashMap<>();
    /** 订阅者: serviceName → [listener] */
    private final Map<String, List<Subscription>> subscribers = new ConcurrentHashMap<>();
    /** 后台扫描器 */
    private final ScheduledExecutorService scanner;
    private volatile boolean closed = false;
    /** 事件序号 (调试用) */
    private final AtomicLong eventSeq = new AtomicLong();

    private static class Internal {
        ServiceInstance instance;
        volatile long lastBeatMs;
        volatile boolean healthy = true;
        Internal(ServiceInstance i, long now) {
            this.instance = i;
            this.lastBeatMs = now;
        }
        void beat(long now) { this.lastBeatMs = now; this.healthy = true; }
    }

    public InMemoryRpcRegistry() { this(30_000L, 1_000L, 5L); }

    public InMemoryRpcRegistry(long expireMs, long scanIntervalMs, long heartbeatIntervalSec) {
        this.expireMs = expireMs;
        this.scanIntervalMs = scanIntervalMs;
        this.heartbeatIntervalSec = heartbeatIntervalSec;
        this.scanner = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "InMemoryRpcRegistry-scanner");
            t.setDaemon(true);
            return t;
        });
        this.scanner.scheduleAtFixedRate(this::scanExpired,
                scanIntervalMs, scanIntervalMs, TimeUnit.MILLISECONDS);
        log.info("InMemoryRpcRegistry started: expireMs={}, scanIntervalMs={}, hbInterval={}s",
                expireMs, scanIntervalMs, heartbeatIntervalSec);
    }

    @Override
    public boolean register(ServiceInstance instance) {
        if (instance == null || instance.getServiceName() == null
                || instance.getInstanceId() == null) {
            return false;
        }
        Map<String, Internal> inner = table.computeIfAbsent(
                instance.getServiceName(), k -> new ConcurrentHashMap<>());
        boolean existed = inner.containsKey(instance.getInstanceId());
        inner.put(instance.getInstanceId(), new Internal(instance, System.currentTimeMillis()));
        publish(instance.getServiceName(),
                new RegistryEvent(RpcRegistry.Type.REGISTER, instance.getServiceName(), instance));
        return !existed;
    }

    @Override
    public void deregister(String serviceName, String instanceId) {
        Map<String, Internal> inner = table.get(serviceName);
        if (inner == null) {
            return;
        }
        Internal removed = inner.remove(instanceId);
        if (removed != null) {
            publish(serviceName, new RegistryEvent(
                    RpcRegistry.Type.DEREGISTER, serviceName, removed.instance));
        }
        if (inner.isEmpty()) {
            table.remove(serviceName);
        }
    }

    @Override
    public long heartbeat(String serviceName, String instanceId) {
        Map<String, Internal> inner = table.get(serviceName);
        if (inner == null) {
            return -1L;
        }
        Internal rec = inner.get(instanceId);
        if (rec == null) {
            return -1L;
        }
        rec.beat(System.currentTimeMillis());
        return heartbeatIntervalSec;
    }

    @Override
    public List<ServiceInstance> discover(String serviceName) {
        Map<String, Internal> inner = table.get(serviceName);
        if (inner == null || inner.isEmpty()) return Collections.emptyList();
        List<ServiceInstance> out = new ArrayList<>(inner.size());
        for (Internal r : inner.values()) {
            out.add(r.instance);
        }
        return out;
    }

    @Override
    public Subscription subscribe(String serviceName, Consumer<RegistryEvent> listener) {
        Subscription sub = new Subscription() {
            @Override public String serviceName() { return serviceName; }
            @Override public Consumer<RegistryEvent> listener() { return listener; }
        };
        subscribers.computeIfAbsent(serviceName, k -> new CopyOnWriteArrayList<>()).add(sub);
        // 订阅时立即给个 FULL_SNAPSHOT，让客户端可以初始化本地缓存
        if (listener != null && !closed) {
            try {
                listener.accept(new RegistryEvent(
                        RpcRegistry.Type.FULL_SNAPSHOT,
                        serviceName, discover(serviceName)));
            } catch (Throwable t) {
                log.warn("subscribe initial-snapshot listener threw: {}", t.getMessage());
            }
        }
        return sub;
    }

    @Override
    public void unsubscribe(String serviceName, Subscription subscription) {
        if (subscription == null) {
            return;
        }
        List<Subscription> list = subscribers.get(serviceName);
        if (list != null) {
            list.remove(subscription);
        }
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        scanner.shutdownNow();
        subscribers.clear();
        table.clear();
        log.info("InMemoryRpcRegistry closed");
    }

    /** 后台扫描: 超时实例标 unhealthy 并触发 DEREGISTER 推送. */
    private void scanExpired() {
        if (closed) {
            return;
        }
        long now = System.currentTimeMillis();
        for (Map.Entry<String, Map<String, Internal>> e : table.entrySet()) {
            String serviceName = e.getKey();
            for (Internal rec : e.getValue().values()) {
                if (rec.healthy && now - rec.lastBeatMs > expireMs) {
                    rec.healthy = false;
                    log.info("instance expired: {}/{} (idle {}ms)",
                            serviceName, rec.instance.getInstanceId(),
                            now - rec.lastBeatMs);
                    // 删表：过期实例从注册表移除，discover() 不再返回
                    java.util.Iterator<Internal> it = e.getValue().values().iterator();
                    while (it.hasNext()) {
                        if (now - it.next().lastBeatMs > expireMs) it.remove();
                    }
                    publish(serviceName, new RegistryEvent(
                            RpcRegistry.Type.DEREGISTER, serviceName, rec.instance));
                }
            }
        }
    }

    /** 是否健康（test helper, 不在接口） */
    public boolean isHealthy(String serviceName, String instanceId) {
        Map<String, Internal> inner = table.get(serviceName);
        if (inner == null) {
            return false;
        }
        Internal r = inner.get(instanceId);
        return r != null && r.healthy;
    }

    /** 手动触发扫描 (test helper) */
    public void triggerScan() {
        scanExpired();
    }

    private void publish(String serviceName, RegistryEvent ev) {
        ev.getClass(); // suppress unused warning
        long seq = eventSeq.incrementAndGet();
        log.debug("publish event #{} {} {}/{}", seq, ev.getType(), serviceName,
                ev.getInstance() != null ? ev.getInstance().getInstanceId() : "<snapshot>");
        List<Subscription> list = subscribers.get(serviceName);
        if (list == null) {
            return;
        }
        for (Subscription sub : list) {
            try {
                sub.listener().accept(ev);
            } catch (Throwable t) {
                log.warn("subscriber threw for {}/{}: {}", serviceName, sub, t.getMessage());
            }
        }
    }

    /** 测试用：当前存储的实例数 */
    public int size(String serviceName) {
        Map<String, Internal> inner = table.get(serviceName);
        return inner == null ? 0 : inner.size();
    }
}
