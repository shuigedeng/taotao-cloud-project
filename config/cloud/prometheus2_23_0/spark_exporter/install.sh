###########################################
cd /root/taotao-cloud/spark_exporter


wget https://github.com/prometheus/graphite_exporter/releases/download/v0.10.1/graphite_exporter-0.10.1.linux-amd64.tar.gz

tar -zxvf graphite_exporter-0.10.1.linux-amd64.tar.gz

mv graphite_exporter-0.10.1.linux-amd64 graphite_exporter

vi graphite_exporter_mapping
mappings:
- match: '*.applicationMaster.*'
  name: spark_app_application_master
  labels:
    application: $1
    qty: $2
- match: '*.*.executor.filesystem.*.*'
  name: spark_app_filesystem_usage
  labels:
    application: $1
    executor_id: $2
    fs_type: $3
    qty: $4
- match: '*.*.jvm.*.*'
  name: spark_app_jvm_memory_usage
  labels:
    application: $1
    executor_id: $2
    mem_type: $3
    qty: $4
- match: '*.*.ExecutorMetrics.*'
  name: spark_app_executor_jvm_metrics
  labels:
    application: $1
    executor_id: $2
    mem_type: $3
- match: '*.*.executor.jvmGCTime.count'
  name: spark_app_jvm_gcTime_count
  labels:
    application: $1
    executor_id: $2
- match: '*.*.jvm.pools.*.*'
  name: spark_app_jvm_memory_pools
  labels:
    application: $1
    executor_id: $2
    mem_type: $3
    qty: $4
- match: '*.*.ExternalShuffle.shuffle-client.*'
  name: spark_app_shuffle_client
  labels:
    application: $1
    executor_id: $2
    qty: $3
- match: '*.*.executor.*.count'
  name: spark_app_executor_task_metrics
  labels:
    application: $1
    executor_id: $2
    qty: $3
- match: '*.*.executor.threadpool.*'
  name: spark_app_executor_tasks
  labels:
    application: $1
    executor_id: $2
    qty: $3
- match: '*.*.BlockManager.*.*'
  name: spark_app_block_manager
  labels:
    application: $1
    executor_id: $2
    type: $3
    qty: $4
- match: '*.*.DAGScheduler.*.*'
  name: spark_app_dag_scheduler
  labels:
    application: $1
    executor_id: $2
    type: $3
    qty: $4
- match: '*.*.CodeGenerator.*.*'
  name: spark_app_code_generator
  labels:
    application: $1
    executor_id: $2
    type: $3
    qty: $4
- match: '*.*.HiveExternalCatalog.*.*'
  name: spark_app_hive_external_catalog
  labels:
    application: $1
    executor_id: $2
    type: $3
    qty: $4
- match: '*.*.*.StreamingMetrics.*.*'
  name: spark_app_app_streaming_metrics
  labels:
    application: $1
    executor_id: $2
    app_name: $3
    type: $4
    qty: $5

./graphite_exporter --graphite.mapping-config=graphite_exporter_mapping

cd /spark/conf/
vi metrics.properties

*.source.jvm.class=org.apache.spark.metrics.source.JvmSource
master.source.jvm.class=org.apache.spark.metrics.source.JvmSource
worker.source.jvm.class=org.apache.spark.metrics.source.JvmSource
driver.source.jvm.class=org.apache.spark.metrics.source.JvmSource
executor.source.jvm.class=org.apache.spark.metrics.source.JvmSource

*.sink.graphite.class=org.apache.spark.metrics.sink.GraphiteSink
*.sink.graphite.protocol=tcp
*.sink.graphite.host=spark-ip
*.sink.graphite.port=9109
*.sink.graphite.period=1
*.sink.graphite.unit=seconds

nohup ./spark-submit
--class StreamingInput
--master spark://master:7077
--supervise
--num-executors 3
--total-executor-cores 3
--executor-memory 2g
--files /usr/etc/spark/conf/metrics.properties
/root/StreamingInput.jar
> /root/logs/StreamingInput.log.txt 2>&1 &


scrape_configs:
  - job_name: 'spark'
    static_configs:
    - targets: ['spark-ip:9108']

curl spark-ip:9108/metrics

导入附件提供的模板文件（Spark-dashboard.json）
