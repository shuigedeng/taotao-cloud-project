###########################################
cd /root/taotao-cloud/redis6_2_4

wget https://github.com/oliver006/redis_exporter/releases/download/v1.24.0/redis_exporter-v1.24.0.linux-amd64.tar.gz

tar -zxvf redis_exporter-v1.24.0.linux-amd64.tar.gz

mv redis_exporter-v1.6.0.linux-amd64 redis_exporter

############## 监控redis单机版 ##############
nohup /root/taotao-cloud/redis6_2_4/redis_exporter \
-redis.addr 192.168.10.220:6379 -redis.password 123456 \
>/root/taotao-cloud/redis6_2_4/redis_exporter.out 2>&1 &

ss -auntlp |grep 9121

  - job_name: 'redis_exporter'
    static_configs:
      - targets:
        - 192.168.10.220:9121

############## 监控redis集群版 ##############
# 先启动redis_exporter
nohup /root/taotao-cloud/redis6_2_4/redis_exporter \
-redis.addr 172.18.11.141:7005 -redis.password xxxxx \
>/root/taotao-cloud/redis6_2_4/redis_exporter.out 2>&1 &

 - job_name: 'redis_exporter_targets'
    static_configs:
      - targets:
        - redis://172.18.11.139:7000
        - redis://172.18.11.139:7001
        - redis://172.18.11.140:7002
        - redis://172.18.11.140:7003
        - redis://172.18.11.141:7004
        - redis://172.18.11.141:7005
    metrics_path: /scrape
    relabel_configs:
      - source_labels: [__address__]
        target_label: __param_target
      - source_labels: [__param_target]
        target_label: instance
      - target_label: __address__
        replacement: 172.18.11.139:9121

######################################################################

#可以重启Prometheus的服务，也可以热加载
sudo curl 'http://192.168.10.220:9090/-/reload' -X POST  #Prometheus的web访问地址

# grafana导入模板模板编号763

################################ 告警规则 ######################################
groups:
- name:  Redis
  rules:
    - alert: RedisDown
      expr: redis_up  == 0
      for: 5m
      labels:
        severity: error
      annotations:
        summary: "Redis down (instance {{ $labels.instance }})"
        description: "Redis 挂了啊，mmp\n  VALUE = {{ $value }}\n  LABELS: {{ $labels }}"
    - alert: MissingBackup
      expr: time() - redis_rdb_last_save_timestamp_seconds > 60 * 60 * 24
      for: 5m
      labels:
        severity: error
      annotations:
        summary: "Missing backup (instance {{ $labels.instance }})"
        description: "Redis has not been backuped for 24 hours\n  VALUE = {{ $value }}\n  LABELS: {{ $labels }}"
    - alert: OutOfMemory
      expr: redis_memory_used_bytes / redis_total_system_memory_bytes * 100 > 90
      for: 5m
      labels:
        severity: warning
      annotations:
        summary: "Out of memory (instance {{ $labels.instance }})"
        description: "Redis is running out of memory (> 90%)\n  VALUE = {{ $value }}\n  LABELS: {{ $labels }}"
    - alert: ReplicationBroken
      expr: delta(redis_connected_slaves[1m]) < 0
      for: 5m
      labels:
        severity: error
      annotations:
        summary: "Replication broken (instance {{ $labels.instance }})"
        description: "Redis instance lost a slave\n  VALUE = {{ $value }}\n  LABELS: {{ $labels }}"
    - alert: TooManyConnections
      expr: redis_connected_clients > 1000
      for: 5m
      labels:
        severity: warning
      annotations:
        summary: "Too many connections (instance {{ $labels.instance }})"
        description: "Redis instance has too many connections\n  VALUE = {{ $value }}\n  LABELS: {{ $labels }}"
    - alert: NotEnoughConnections
      expr: redis_connected_clients < 5
      for: 5m
      labels:
        severity: warning
      annotations:
        summary: "Not enough connections (instance {{ $labels.instance }})"
        description: "Redis instance should have more connections (> 5)\n  VALUE = {{ $value }}\n  LABELS: {{ $labels }}"
    - alert: RejectedConnections
      expr: increase(redis_rejected_connections_total[1m]) > 0
      for: 5m
      labels:
        severity: error
      annotations:
        summary: "Rejected connections (instance {{ $labels.instance }})"
        description: "Some connections to Redis has been rejected\n  VALUE = {{ $value }}\n  LABELS: {{ $labels }}"
