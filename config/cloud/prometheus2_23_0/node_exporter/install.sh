###########################################
# https://node_exporter.com/node_exporter/download

cd /root/taotao-cloud/

wget https://github.com/prometheus/node_exporter/releases/download/v1.1.2/node_exporter-1.1.2.linux-amd64.tar.gz

tar -zxvf node_exporter-1.1.2.linux-amd64.tar.gz

mv node_exporter-1.1.2.linux-amd64 node_exporter1.1.2

nohup /opt/taotao-cloud/node_exporter1.1.2/node_exporter \
 >/opt/taotao-cloud/node_exporter1.1.2/node_exporter.out 2>&1 &

- job_name: 'node_exporter'
  static_configs:
    - targets: [ '127.0.0.1:9100' ]


groups:
    - name: 主机状态-监控告警
      rules:
      - alert: 主机状态
        expr: up == 0
        for: 1m
        labels:
          status: 非常严重
        annotations:
          summary: "{{$labels.instance}}:服务器宕机"
          description: "{{$labels.instance}}:服务器延时超过5分钟"

      - alert: CPU使用情况
        expr: 100-(avg(irate(node_cpu_seconds_total{mode="idle"}[5m])) by(instance)* 100) > 60
        for: 1m
        labels:
          status: 一般告警
        annotations:
          summary: "{{$labels.mountpoint}} CPU使用率过高！"
          description: "{{$labels.mountpoint }} CPU使用大于60%(目前使用:{{$value}}%)"

      - alert: 内存使用
        expr: 100 -(node_memory_MemTotal_bytes -node_memory_MemFree_bytes+node_memory_Buffers_bytes+node_memory_Cached_bytes ) / node_memory_MemTotal_bytes * 100> 80
        for: 1m
        labels:
          status: 严重告警
        annotations:
          summary: "{{$labels.mountpoint}} 内存使用率过高！"
          description: "{{$labels.mountpoint }} 内存使用大于80%(目前使用:{{$value}}%)"
      - alert: IO性能
        expr: 100-(avg(irate(node_disk_io_time_seconds_total[1m])) by(instance)* 100) < 60
        for: 1m
        labels:
          status: 严重告警
        annotations:
          summary: "{{$labels.mountpoint}} 流入磁盘IO使用率过高！"
          description: "{{$labels.mountpoint }} 流入磁盘IO大于60%(目前使用:{{$value}})"

      - alert: 网络
        expr: ((sum(rate (node_network_receive_bytes_total{device!~'tap.*|veth.*|br.*|docker.*|virbr*|lo*'}[5m])) by (instance)) / 100) > 102400
        for: 1m
        labels:
          status: 严重告警
        annotations:
          summary: "{{$labels.mountpoint}} 流入网络带宽过高！"
          description: "{{$labels.mountpoint }}流入网络带宽持续2分钟高于100M. RX带宽使用率{{$value}}"

      - alert: 网络
        expr: ((sum(rate (node_network_transmit_bytes_total{device!~'tap.*|veth.*|br.*|docker.*|virbr*|lo*'}[5m])) by (instance)) / 100) > 102400
        for: 1m
        labels:
          status: 严重告警
        annotations:
          summary: "{{$labels.mountpoint}} 流出网络带宽过高！"
          description: "{{$labels.mountpoint }}流出网络带宽持续2分钟高于100M. RX带宽使用率{{$value}}"

      - alert: TCP会话
        expr: node_netstat_Tcp_CurrEstab > 1000
        for: 1m
        labels:
          status: 严重告警
        annotations:
          summary: "{{$labels.mountpoint}} TCP_ESTABLISHED过高！"
          description: "{{$labels.mountpoint }} TCP_ESTABLISHED大于1000%(目前使用:{{$value}}%)"

      - alert: 磁盘容量
        expr: 100-(node_filesystem_free_bytes{fstype=~"ext4|xfs"}/node_filesystem_size_bytes {fstype=~"ext4|xfs"}*100) > 80
        for: 1m
        labels:
          status: 严重告警
        annotations:
          summary: "{{$labels.mountpoint}} 磁盘分区使用率过高！"
          description: "{{$labels.mountpoint }} 磁盘分区使用大于80%(目前使用:{{$value}}%)"
