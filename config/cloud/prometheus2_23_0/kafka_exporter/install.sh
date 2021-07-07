###########################################
cd /root/taotao-cloud/kafka_exporter

# 第一种 ***********************************************************
使用jvm_exporter监控，jvm_exporter是一个可以配置抓取和暴露JMX目标的mBeans的收集器。
github地址：https://github.com/prometheus/jmx_exporter

wget https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.3.1/jmx_prometheus_javaagent-0.3.1.jar

https://raw.githubusercontent.com/prometheus/jmx_exporter/master/example_configs/kafka-2_0_0.yml

# vim kafka-2_0_0.yml

lowercaseOutputName: true

rules:
# Special cases and very specific rules
- pattern : kafka.server<type=(.+), name=(.+), clientId=(.+), topic=(.+), partition=(.*)><>Value
  name: kafka_server_$1_$2
  type: GAUGE
  labels:
    clientId: "$3"
    topic: "$4"
    partition: "$5"
- pattern : kafka.server<type=(.+), name=(.+), clientId=(.+), brokerHost=(.+), brokerPort=(.+)><>Value
  name: kafka_server_$1_$2
  type: GAUGE
  labels:
    clientId: "$3"
    broker: "$4:$5"
- pattern : kafka.coordinator.(\w+)<type=(.+), name=(.+)><>Value
  name: kafka_coordinator_$1_$2_$3
  type: GAUGE

# Generic per-second counters with 0-2 key/value pairs
- pattern: kafka.(\w+)<type=(.+), name=(.+)PerSec\w*, (.+)=(.+), (.+)=(.+)><>Count
  name: kafka_$1_$2_$3_total
  type: COUNTER
  labels:
    "$4": "$5"
    "$6": "$7"
- pattern: kafka.(\w+)<type=(.+), name=(.+)PerSec\w*, (.+)=(.+)><>Count
  name: kafka_$1_$2_$3_total
  type: COUNTER
  labels:
    "$4": "$5"
- pattern: kafka.(\w+)<type=(.+), name=(.+)PerSec\w*><>Count
  name: kafka_$1_$2_$3_total
  type: COUNTER

- pattern: kafka.server<type=(.+), client-id=(.+)><>([a-z-]+)
  name: kafka_server_quota_$3
  type: GAUGE
  labels:
    resource: "$1"
    clientId: "$2"

- pattern: kafka.server<type=(.+), user=(.+), client-id=(.+)><>([a-z-]+)
  name: kafka_server_quota_$4
  type: GAUGE
  labels:
    resource: "$1"
    user: "$2"
    clientId: "$3"

# Generic gauges with 0-2 key/value pairs
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.+), (.+)=(.+)><>Value
  name: kafka_$1_$2_$3
  type: GAUGE
  labels:
    "$4": "$5"
    "$6": "$7"
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.+)><>Value
  name: kafka_$1_$2_$3
  type: GAUGE
  labels:
    "$4": "$5"
- pattern: kafka.(\w+)<type=(.+), name=(.+)><>Value
  name: kafka_$1_$2_$3
  type: GAUGE

# Emulate Prometheus 'Summary' metrics for the exported 'Histogram's.
#
# Note that these are missing the '_sum' metric!
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.+), (.+)=(.+)><>Count
  name: kafka_$1_$2_$3_count
  type: COUNTER
  labels:
    "$4": "$5"
    "$6": "$7"
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.*), (.+)=(.+)><>(\d+)thPercentile
  name: kafka_$1_$2_$3
  type: GAUGE
  labels:
    "$4": "$5"
    "$6": "$7"
    quantile: "0.$8"
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.+)><>Count
  name: kafka_$1_$2_$3_count
  type: COUNTER
  labels:
    "$4": "$5"
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.*)><>(\d+)thPercentile
  name: kafka_$1_$2_$3
  type: GAUGE
  labels:
    "$4": "$5"
    quantile: "0.$6"
- pattern: kafka.(\w+)<type=(.+), name=(.+)><>Count
  name: kafka_$1_$2_$3_count
  type: COUNTER
- pattern: kafka.(\w+)<type=(.+), name=(.+)><>(\d+)thPercentile
  name: kafka_$1_$2_$3
  type: GAUGE
  labels:
    quantile: "0.$4"

# 修改bin/zkServer.sh启动配置
$ cat bin/kafka-server-start.sh
if [ $# -lt 1 ];
then
    echo "USAGE: $0 [-daemon] server.properties [--override property=value]*"
    exit 1
fi
base_dir=$(dirname $0)
if [ "x$KAFKA_LOG4J_OPTS" = "x" ]; then
    export KAFKA_LOG4J_OPTS="-Dlog4j.configuration=file:$base_dir/../config/log4j.properties"
fi
if [ "x$KAFKA_HEAP_OPTS" = "x" ]; then
    export KAFKA_HEAP_OPTS="-Xmx1G -Xms1G"
fi
EXTRA_ARGS=${EXTRA_ARGS-'-name kafkaServer -loggc'}
#如下两行内容
export JMX_PORT="9999"
export KAFKA_OPTS="-javaagent:/usr/local/kafka/jmx/jmx_prometheus_javaagent-0.12.0.jar=9991:/usr/local/kafka/jmx/kafka-2_0_0.yml"
COMMAND=$1
case $COMMAND in
  -daemon)
    EXTRA_ARGS="-daemon "$EXTRA_ARGS
    shift
    ;;
  *)
    ;;
esac
exec $base_dir/kafka-run-class.sh $EXTRA_ARGS kafka.Kafka "$@"

- job_name: 'kafka-cluster'
  scrape_interval: 5s
  static_configs:
  - targets: ['10.3.0.41:9991']
  - targets: ['10.3.0.42:9991']
  - targets: ['10.3.20.4:9991']

# 第二种 ***********************************************************
使用kafka_exporter监控
https://github.com/danielqsj/kafka_exporter

wget https://github.com/danielqsj/kafka_exporter/releases/download/v1.3.1/kafka_exporter-1.3.1.linux-amd64.tar.gz

tar -zxvf kafka_exporter-1.3.1.linux-amd64.tar.gz

mv kafka_exporter-1.3.1.linux-amd64 kafka_exporter

nohup /opt/taotao-cloud/kafka_exporter/kafka_exporter  \
--kafka.server=kafka:9092 \
>/opt/taotao-cloud/kafka_exporter/kafka_exporter.out 2>&1 &

curl http://127.0.0.1:9308

- job_name: 'kafka'
  static_configs:
  - targets:
    - '127.0.0.1:9308'

# kafka导入模板模板编号7589，10466,11963

################################ 告警规则 ######################################
groups:
- name: kafka_server
  rules:
  - alert: UnderReplicatedPartitions复制不足的分区数
    expr: avg_over_time(kafka_server_ReplicaManager_Value{name="UnderReplicatedPartitions",}[1m]) >= 1
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} app UnderReplicatedPartitions"
      description: "app: {{ $labels.app }} ,Instance: {{ $labels.instance }} 复制不足的分区数: {{ $value }}"
  - alert: ActiveControllerCount集群中控制器的数量
    expr: sum(avg_over_time(kafka_controller_KafkaController_Value{name="ActiveControllerCount",}[1m])) by(app) != 1
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} app ActiveControllerCount"
      description: "app: {{ $labels.app }}, 集群中控制器的数量异常：{{ $value }}"
  - alert: OfflinePartitionsCount没有活动领导者的分区数
    expr: avg_over_time(kafka_controller_KafkaController_Value{name="OfflinePartitionsCount",}[1m]) > 0
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} app OfflinePartitionsCount"
      description: "app: {{ $labels.app }}, OfflinePartitionsCount没有活动领导者的分区数, count：{{ $value }}"
  - alert: BytesOutPerSec出网络流量M/s
    expr: avg_over_time(kafka_server_BrokerTopicMetrics_OneMinuteRate{name="BytesOutPerSec",topic=""}[1m]) / 1024 /1024 >= 450
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} 网络流量出异常"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}, 出网络流量M/s大于 450， 当前值为：{{ $value }}"
  - alert: BytesInPerSec入网络流量M/s
    expr: avg_over_time(kafka_server_BrokerTopicMetrics_OneMinuteRate{name="BytesInPerSec",topic=""}[1m]) / 1024 /1024 >= 150
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} 网络流量出异常"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}, 出网络流量M/s大于 150， 当前值为：{{ $value }}"
  - alert: RequestHandlerAvgIdlePercent请求处理程序线程空闲的平均时间百分比
    for: 1m
    labels:
      job: kafka
    expr: avg_over_time(kafka_server_KafkaRequestHandlerPool_OneMinuteRate{name="RequestHandlerAvgIdlePercent",}[1m]) <= 0.3
    annotations:
      summary: "{{ $labels.app }} 请求处理程序线程空闲的平均时间百分比"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}，请求处理程序线程空闲百分比低于30%， 当前值为：{{ $value }}"
  - alert: NetworkProcessorAvgIdlePercent网络处理器线程空闲的平均时间百分比
    expr: avg_over_time(kafka_network_SocketServer_Value{name="NetworkProcessorAvgIdlePercent",}[1m]) <= 0.3
    annotations:
      summary: "{{ $labels.app }} 网络处理器线程空闲的平均时间百分比"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}，网络处理器线程空闲的平均时间百分比30%， 当前值为：{{ $value }}"
  - alert: connection_count 已建立的连接数
    for: 1m
    labels:
      job: kafka
    expr: sum(avg_over_time(kafka_server_socket_server_metrics_connection_count{listener="PLAINTEXT",}[1m])) by (instance,app) > 3000
    annotations:
      summary: "{{ $labels.app }} 已建立的连接数"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}，已建立的连接数大于3000， 当前值为：{{ $value }}"
  - alert: connection_creation 每秒新建连接数
    expr: sum(avg_over_time(kafka_server_socket_server_metrics_connection_creation_rate[1m])) by (instance)  > 100
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} 每秒新建连接数"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}，每秒新建连接数大于100， 当前值为：{{ $value }}"
  - alert: RequestQueueTimeMs 请求在请求队列中等待的时间
    expr: avg_over_time(kafka_network_RequestMetrics_999thPercentile{name="RequestQueueTimeMs",request="Produce",}[1m]) > 5000
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} 请求在请求队列中等待的时间"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}，请求在请求队列中等待的时间大于5000ms， 当前值为：{{ $value }}"
  - alert: LocalTimeMs leader处理请求的时间
    expr: avg_over_time(kafka_network_RequestMetrics_999thPercentile{name="LocalTimeMs",request="Produce",}[1m])  > 5000
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} leader处理请求的时间"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}, leader处理请求的时间大于5000ms， 当前值为：{{ $value }}"
  - alert: RemoteTimeMs 请求等待follower的时间
    expr: avg_over_time(kafka_network_RequestMetrics_999thPercentile{name="RemoteTimeMs",request="Produce",}[1m]) > 1000
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} 请求等待follower的时间"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}, 请求等待follower的时间大于1000ms， 当前值为：{{ $value }}"
  - alert: ResponseQueueTimeMs 请求在响应队列中等待的时间
    expr: avg_over_time(kafka_network_RequestMetrics_999thPercentile{name="ResponseQueueTimeMs",request="Produce",}[1m]) > 1000
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} 请求在响应队列中等待的时间"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}, 请求在响应队列中等待的时间大于1000ms， 当前值为：{{ $value }}"
  - alert: ResponseSendTimeMs 发送响应的时间
    expr: avg_over_time(kafka_network_RequestMetrics_999thPercentile{name="ResponseSendTimeMs",request="Produce",}[1m]) > 1000
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} 发送响应的时间"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}, 发送响应的时间大于1000ms， 当前值为：{{ $value }}"
  - alert: MessagesInPerSec 汇总传入消息速率
    expr:  avg_over_time(kafka_server_BrokerTopicMetrics_OneMinuteRate{name="MessagesInPerSec",topic=""}[1m]) > 200000
    for: 1m
    labels:
      job: kafka
    annotations:
      summary: "{{ $labels.app }} 汇总传入消息速率"
      description: "app: {{ $labels.app }}, instance：{{ $labels.instance }}, 汇总传入消息速率大于200000 m/s， 当前值为：{{ $value }}"

