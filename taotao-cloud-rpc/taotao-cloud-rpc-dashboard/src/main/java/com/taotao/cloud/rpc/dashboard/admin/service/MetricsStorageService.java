package com.taotao.cloud.rpc.dashboard.admin.service;

import com.taotao.cloud.rpc.dashboard.admin.model.MetricsPushRequest;
import com.taotao.cloud.rpc.dashboard.admin.model.ProviderVO;
import com.taotao.cloud.rpc.dashboard.admin.model.ServiceVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 简易内存存储（生产中应使用 ttc-config 持久化或专用 TSDB）
 */
@Service
public class MetricsStorageService {

    private static final Logger log = LogManager.getLogger(MetricsStorageService.class);

    /** 接收到的 Provider 实例 */
    private final Map<String, ProviderVO> providers = new ConcurrentHashMap<>();

    /** 服务注册表：serviceKey -> {providers, consumers} */
    private final Map<String, ServiceRegistry> services = new ConcurrentHashMap<>();

    /** 指标时序：service:method -> 时间序列 */
    private final Map<String, List<MetricsPoint>> metricsHistory = new ConcurrentHashMap<>();

    /**
     * 每个 service:method <b>最近一次</b>上报的采样点。
     * <p>
     * QPS 是速率，只能取"当前值"；把整条历史序列加起来得到的是累计量，
     * 同一份稳定流量会随着推送次数线性放大，看板上的数字就没有意义了。
     */
    private final Map<String, MetricsPoint> latestMetrics = new ConcurrentHashMap<>();

    private static final int MAX_HISTORY = 1000;

    /**
     * metricsHistory 的 key 是 {@code service + ":" + method}，按 service 取归属时<b>必须整段相等比较</b>：
     * {@code startsWith("order")} 会把 "order-item:query" 也算进 order 头上。
     */
    private static String servicePart(String metricKey) {
        int i = metricKey.indexOf(':');
        return i < 0 ? metricKey : metricKey.substring(0, i);
    }

    /** 链路追踪 */
    private final List<TraceRecord> traces = Collections.synchronizedList(new ArrayList<>());

    public void recordProvider(ProviderVO provider) {
        providers.put(provider.getId(), provider);
        ServiceRegistry registry = services.computeIfAbsent(provider.getService(), k -> new ServiceRegistry());
        // 同一个 id 的心跳会反复推上来：List 侧也必须按 id 收敛，
        // 否则 providerCount 随心跳次数线性膨胀（而 Map 侧一直是去重的，两边会打架）。
        registry.providers.removeIf(p -> Objects.equals(p.getId(), provider.getId()));
        registry.providers.add(provider);
        log.info("Provider registered: {} -> {}", provider.getId(), provider.getAddress());
    }

    public void recordMetrics(MetricsPushRequest request) {
        if (request == null || request.getMetrics() == null) return;
        long now = System.currentTimeMillis();
        for (MetricsPushRequest.MetricsEntry e : request.getMetrics()) {
            String key = e.getService() + ":" + e.getMethod();
            MetricsPoint point = new MetricsPoint(
                    now, e.getQps(), e.getP50(), e.getP90(), e.getP99(), e.getErrorRate());
            latestMetrics.put(key, point);
            List<MetricsPoint> history = metricsHistory.computeIfAbsent(key, k -> new ArrayList<>());
            // 追加 + 截断必须是一个原子动作，否则并发上报时上限会被冲破（实测曾冲到 1012）
            synchronized (history) {
                history.add(point);
                while (history.size() > MAX_HISTORY) {
                    history.remove(0);
                }
            }
        }
    }

    public List<ServiceVO> listServices() {
        List<ServiceVO> result = new ArrayList<>();
        services.forEach((serviceKey, registry) -> {
            ServiceVO vo = new ServiceVO();
            vo.setServiceKey(serviceKey);
            vo.setServiceName(serviceKey);
            vo.setProviderCount(registry.providers.size());
            vo.setConsumerCount(registry.consumers.size());
            // 每个方法取"最近一次"上报的 qps，跨方法相加才是这个服务当前的速率
            long currentQps = 0L;
            for (Map.Entry<String, MetricsPoint> en : latestMetrics.entrySet()) {
                if (servicePart(en.getKey()).equals(serviceKey)) {
                    currentQps += en.getValue().getQps();
                }
            }
            Map<String, Object> m = new HashMap<>();
            m.put("qps", currentQps);
            vo.setMetrics(m);
            result.add(vo);
        });
        return result;
    }

    public List<ProviderVO> listProviders() {
        return new ArrayList<>(providers.values());
    }

    public List<ProviderVO> listProvidersByService(String serviceKey) {
        ServiceRegistry reg = services.get(serviceKey);
        if (reg == null) return new ArrayList<>();
        return new ArrayList<>(reg.providers);
    }

    public Map<String, List<MetricsPoint>> getMetricsHistory(String serviceKey) {
        Map<String, List<MetricsPoint>> result = new HashMap<>();
        metricsHistory.forEach((key, value) -> {
            if (servicePart(key).equals(serviceKey)) {
                synchronized (value) {
                    result.put(key, new ArrayList<>(value));
                }
            }
        });
        return result;
    }

    public void addTrace(TraceRecord trace) {
        traces.add(trace);
        if (traces.size() > 1000) {
            traces.remove(0);
        }
    }

    public List<TraceRecord> listTraces() {
        return new ArrayList<>(traces);
    }

    public Map<String, Object> dashboardOverview() {
        Map<String, Object> overview = new HashMap<>();
        overview.put("serviceCount", services.size());
        overview.put("providerCount", providers.size());
        long currentQps = 0L;
        for (MetricsPoint p : latestMetrics.values()) {
            currentQps += p.getQps();
        }
        overview.put("totalQps", currentQps);
        return overview;
    }

    public static class ServiceRegistry {
        // 心跳重复推送 + 控制台的读侧遍历都发生在这个 list 上：写时复制让它两处都成立
        public final List<ProviderVO> providers = new CopyOnWriteArrayList<>();
        public final List<ConsumerVO> consumers = new CopyOnWriteArrayList<>();
    }

    public static class ConsumerVO {
        public String id;
        public String service;
        public String address;
    }

    public static class MetricsPoint {
        public long timestamp;
        public long qps;
        public long p50;
        public long p90;
        public long p99;
        public double errorRate;

        public MetricsPoint(long timestamp, long qps, long p50, long p90, long p99, double errorRate) {
            this.timestamp = timestamp;
            this.qps = qps;
            this.p50 = p50;
            this.p90 = p90;
            this.p99 = p99;
            this.errorRate = errorRate;
        }

        public long getTimestamp() { return timestamp; }
        public long getQps() { return qps; }
        public long getP50() { return p50; }
        public long getP90() { return p90; }
        public long getP99() { return p99; }
        public double getErrorRate() { return errorRate; }
    }

    public static class TraceRecord {
        public String traceId;
        public String service;
        public String method;
        public long startTime;
        public long rt;
        public boolean success;
        public String error;
        public String remoteAddress;

        public String getTraceId() { return traceId; }
        public void setTraceId(String traceId) { this.traceId = traceId; }
        public String getService() { return service; }
        public void setService(String service) { this.service = service; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getRt() { return rt; }
        public void setRt(long rt) { this.rt = rt; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getRemoteAddress() { return remoteAddress; }
        public void setRemoteAddress(String remoteAddress) { this.remoteAddress = remoteAddress; }
    }
}
