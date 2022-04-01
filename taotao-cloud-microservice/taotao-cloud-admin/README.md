# prometheus 模块

## 功能
- `Spring cloud` 对接 Prometheus `http_sd`，支持 `servlet` 和 `webflux`，建议集成到 Spring boot admin 这类非业务服务中。

### 添加配置
```yaml
- job_name: taotao-cloud
  honor_timestamps: true
  scrape_interval: 15s
  scrape_timeout: 10s
  metrics_path: /actuator/prometheus
  scheme: http
  http_sd_configs:
  - url: 'http://{ip}:{port}/actuator/prometheus/sd'
```

## alert web hook

### 添加配置
```yaml
receivers:
- name: "alerts"
  webhook_configs:
  - url: 'http://{ip}:{port}/actuator/prometheus/alerts'
    send_resolved: true
```

### 自定义监听事件并处理
```java
@Async
@EventListener
public void onAlertEvent(AlertMessage message) {
	// 处理 alert webhook message
}
```
