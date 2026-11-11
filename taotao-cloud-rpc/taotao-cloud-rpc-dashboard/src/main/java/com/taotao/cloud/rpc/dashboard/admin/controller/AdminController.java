package com.taotao.cloud.rpc.dashboard.admin.controller;

import com.taotao.cloud.rpc.dashboard.admin.model.MetricsPushRequest;
import com.taotao.cloud.rpc.dashboard.admin.model.ProviderVO;
import com.taotao.cloud.rpc.dashboard.admin.service.MetricsStorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin REST API
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger log = LogManager.getLogger(AdminController.class);

    @Autowired
    private MetricsStorageService storage;

    /**
     * 全局概览
     */
    @GetMapping("/dashboard/overview")
    public Map<String, Object> dashboardOverview() {
        return storage.dashboardOverview();
    }

    /**
     * 服务列表
     */
    @GetMapping("/services")
    public List<com.taotao.cloud.rpc.dashboard.admin.model.ServiceVO> listServices(
            @RequestParam(required = false) String keyword) {
        List<com.taotao.cloud.rpc.dashboard.admin.model.ServiceVO> all = storage.listServices();
        if (keyword != null && !keyword.isEmpty()) {
            return all.stream()
                    .filter(s -> s.getServiceKey().contains(keyword))
                    .collect(java.util.stream.Collectors.toList());
        }
        return all;
    }

    /**
     * 服务详情
     */
    @GetMapping("/services/{serviceKey}")
    public Map<String, Object> serviceDetail(@PathVariable String serviceKey) {
        Map<String, Object> detail = new HashMap<>();
        detail.put("providers", storage.listProvidersByService(serviceKey));
        detail.put("metrics", storage.getMetricsHistory(serviceKey));
        return detail;
    }

    /**
     * Provider 列表
     */
    @GetMapping("/providers")
    public List<ProviderVO> listProviders() {
        return storage.listProviders();
    }

    /**
     * 指标时序
     */
    @GetMapping("/services/{serviceKey}/metrics")
    public Map<String, List<MetricsStorageService.MetricsPoint>> getMetrics(@PathVariable String serviceKey) {
        return storage.getMetricsHistory(serviceKey);
    }

    /**
     * 链路追踪列表
     */
    @GetMapping("/traces")
    public List<MetricsStorageService.TraceRecord> listTraces() {
        return storage.listTraces();
    }

    /**
     * 接收 Provider 上报的指标
     */
    @PostMapping("/metrics/push")
    public ResponseEntity<Map<String, Object>> pushMetrics(@RequestBody MetricsPushRequest request) {
        try {
            storage.recordMetrics(request);
            return ResponseEntity.ok(success("Metrics received"));
        } catch (Exception e) {
            log.error("Failed to push metrics", e);
            return ResponseEntity.status(500).body(error(e.getMessage()));
        }
    }

    /**
     * Provider 注册
     */
    @PostMapping("/providers/register")
    public ResponseEntity<Map<String, Object>> registerProvider(@RequestBody ProviderVO provider) {
        try {
            storage.recordProvider(provider);
            return ResponseEntity.ok(success("Provider registered: " + provider.getId()));
        } catch (Exception e) {
            log.error("Failed to register provider", e);
            return ResponseEntity.status(500).body(error(e.getMessage()));
        }
    }

    private Map<String, Object> success(String message) {
        Map<String, Object> r = new HashMap<>();
        r.put("code", 0);
        r.put("message", message);
        return r;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> r = new HashMap<>();
        r.put("code", 1);
        r.put("message", message);
        return r;
    }
}
