###########################################
cd /root/taotao-cloud/zookeeper

# 第一种 ***********************************************************
使用jvm_exporter监控，jvm_exporter是一个可以配置抓取和暴露JMX目标的mBeans的收集器。
github地址：https://github.com/prometheus/jmx_exporter

wget https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.3.1/jmx_prometheus_javaagent-0.3.1.jar

https://github.com/prometheus/jmx_exporter/blob/master/example_configs/zookeeper.yaml

vim zookeeper.yaml
rules:
  # replicated Zookeeper
  - pattern: "org.apache.ZooKeeperService<name0=ReplicatedServer_id(\\d+)><>(\\w+)"
    name: "zookeeper_$2"
    type: GAUGE
  - pattern: "org.apache.ZooKeeperService<name0=ReplicatedServer_id(\\d+), name1=replica.(\\d+)><>(\\w+)"
    name: "zookeeper_$3"
    type: GAUGE
    labels:
      replicaId: "$2"
  - pattern: "org.apache.ZooKeeperService<name0=ReplicatedServer_id(\\d+), name1=replica.(\\d+), name2=(\\w+)><>(Packets\\w+)"
    name: "zookeeper_$4"
    type: COUNTER
    labels:
      replicaId: "$2"
      memberType: "$3"
  - pattern: "org.apache.ZooKeeperService<name0=ReplicatedServer_id(\\d+), name1=replica.(\\d+), name2=(\\w+)><>(\\w+)"
    name: "zookeeper_$4"
    type: GAUGE
    labels:
      replicaId: "$2"
      memberType: "$3"
  - pattern: "org.apache.ZooKeeperService<name0=ReplicatedServer_id(\\d+), name1=replica.(\\d+), name2=(\\w+), name3=(\\w+)><>(\\w+)"
    name: "zookeeper_$4_$5"
    type: GAUGE
    labels:
      replicaId: "$2"
      memberType: "$3"
  # standalone Zookeeper
  - pattern: "org.apache.ZooKeeperService<name0=StandaloneServer_port(\\d+)><>(\\w+)"
    type: GAUGE
    name: "zookeeper_$2"
  - pattern: "org.apache.ZooKeeperService<name0=StandaloneServer_port(\\d+), name1=InMemoryDataTree><>(\\w+)"
    type: GAUGE
    name: "zookeeper_$2"

# 修改bin/zkServer.sh启动配置
# 第一种
"""
if [ "x$SERVER_JVMFLAGS"  != "x" ]
then
    JVMFLAGS="$SERVER_JVMFLAGS $JVMFLAGS"
fi

## 新增javaagent
JMX_DIR="/opt/taotao-cloud/jmx_exporter"
JVMFLAGS="$JVMFLAGS -javaagent:$JMX_DIR/jmx_prometheus_javaagent-0.3.1.jar=30001:$JMX_DIR/zookeeper.yml"

if [ "x$2" != "x" ]
then
    ZOOCFG="$ZOOCFGDIR/$2"
fi
"""
# 第二种
vim /opt/taotao-cloud/zookeeper3_6_2/conf/java.env

export JMX_DIR="/opt/taotao-cloud/jmx_exporter"
export SERVER_JVMFLAGS="$SERVER_JVMFLAGS -javaagent:$JMX_DIR/jmx_prometheus_javaagent-0.13.0.jar=30001:$JMX_DIR/zookeeper/zookeeper.yaml"

# 重启bin/zkServer.sh

# curl ‘http://localhost:30001’

- job_name: 'zookeeper'
  static_configs:
  - targets: ['ip1:30001','ip2:30001','ip3:30001']

# 第二种 ***********************************************************
使用zookeeper_exporter监控
github地址：https://github.com/jiankunking/zookeeper_exporter

wget https://github.com/carlpett/zookeeper_exporter/releases/download/v1.0.2/zookeeper_exporter

"""
	功能描述
conf	打印配置
cons	列出所有连接到这台服务器的客户端全部连接/会话详细信息。包括"接受/发送"的包数量、会话id、操作延迟、最后的操作执行等等信息。
crst	重置所有连接的连接和会话统计信息。
dump	列出那些比较重要的会话和临时节点。这个命令只能在leader节点上有用。
envi	打印出服务环境的详细信息。
reqs	列出未经处理的请求
ruok	即"Are you ok"，测试服务是否处于正确状态。如果确实如此，那么服务返回"imok"，否则不做任何相应。
stat	输出关于性能和连接的客户端的列表。
srst	重置服务器的统计。
srvr	列出连接服务器的详细信息
wchs	列出服务器watch的详细信息。
wchc	通过session列出服务器watch的详细信息，它的输出是一个与watch相关的会话的列表。
wchp	通过路径列出服务器watch的详细信息。它输出一个与session相关的路径。
mntr	输出可用于检测集群健康状态的变量列表
"""
"""
zk_version	版本
zk_avg_latency	平均响应延迟
zk_max_latency	最大响应延迟
zk_min_latency	最小 响应延迟
zk_packets_received	收包数
zk_packets_sent	发包数
zk_num_alive_connections	活跃连接数
zk_outstanding_requests	堆积请求数
zk_server_state	主从状态
zk_znode_count	znode 数
zk_watch_count	watch 数
zk_ephemerals_count	临时节点数
zk_approximate_data_size	近似数据总和大小
zk_open_file_descriptor_count	打开文件描述符数
zk_max_file_descriptor_count	最大文件描述符数
leader	主节点
zk_followers	Follower数
zk_synced_followers	已同步的Follower数
zk_pending_syncs	阻塞中的sync操作
"""

nohup /opt/taotao-cloud/zookeeper_exporter/zookeeper_exporter  \
-bind-addr :9141 -zookeeper localhost:2181 \
>/opt/taotao-cloud/zookeeper_exporter/zookeeper_exporter.out 2>&1 &

# grafana导入模板模板编号9236

################################ 告警规则 ######################################
groups:
- name: zookeeperStatsAlert
  rules:
  - alert: 堆积请求数过大
    expr: avg(zk_outstanding_requests) by (instance) > 10    for: 1m
    labels:      severity: critical
    annotations:
      summary: "Instance {{ $labels.instance }} "
      description: "积请求数过大"
  - alert: 阻塞中的 sync 过多
    expr: avg(zk_pending_syncs) by (instance) > 10
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "Instance {{ $labels.instance }} "
      description: "塞中的 sync 过多"
  - alert: 平均响应延迟过高
    expr: avg(zk_avg_latency) by (instance) > 10
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "Instance {{ $labels.instance }} "
      description: '平均响应延迟过高'
  - alert: 打开文件描述符数大于系统设定的大小
    expr: zk_open_file_descriptor_count > zk_max_file_descriptor_count * 0.85
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "Instance {{ $labels.instance }} "
      description: '打开文件描述符数大于系统设定的大小'
  - alert: zookeeper服务器宕机
    expr: zk_up == 0
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "Instance {{ $labels.instance }} "
      description: 'zookeeper服务器宕机'
  - alert: zk主节点丢失
    expr: absent(zk_server_state{state="leader"})  != 1
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "Instance {{ $labels.instance }} "
      description: 'zk主节点丢失'
