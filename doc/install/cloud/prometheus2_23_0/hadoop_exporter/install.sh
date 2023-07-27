###########################################
cd /root/taotao-cloud/hadoop

# 第一种 ***********************************************************
使用jvm_exporter监控，jvm_exporter是一个可以配置抓取和暴露JMX目标的mBeans的收集器。
github地址：https://github.com/prometheus/jmx_exporter

wget https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.3.1/jmx_prometheus_javaagent-0.3.1.jar

# 创建空文件
namenode.yaml
datanode.yaml
resourcemanager.yaml
nodemanager.yaml
journalnode.yaml
zkfc.yaml
hffps.yaml
proxyserver.yaml
historyserver.yaml

cd hadoop/etc/hadoop/
vim hadoop-env.sh

export HDFS_NAMENODE_OPTS="-javaagent:/root/taotao-cloud/hadoop/jmx_prometheus_javaagent-0.13.0.jar=30002:/root/taotao-cloud/hadoop/hadoop/namenode.yaml $HDFS_NAMENODE_OPTS"
export HDFS_DATANODE_OPTS="-javaagent:/root/taotao-cloud/hadoop/jmx_prometheus_javaagent-0.13.0.jar=30003:/root/taotao-cloud/hadoop/hadoop/datanode.yaml $HDFS_DATANODE_OPTS"
export YARN_RESOURCEMANAGER_OPTS="-javaagent:/root/taotao-cloud/hadoop/jmx_prometheus_javaagent-0.13.0.jar=30004:/root/taotao-cloud/hadoop/hadoop/resourcemanager.yaml $YARN_RESOURCEMANAGER_OPTS"
export YARN_NODEMANAGER_OPTS="-javaagent:/root/taotao-cloud/hadoop/jmx_prometheus_javaagent-0.13.0.jar=30005:/root/taotao-cloud/hadoop/hadoop/nodemanager.yaml $YARN_NODEMANAGER_OPTS"
export HDFS_JOURNALNODE_OPTS="-javaagent:/root/taotao-cloud/hadoop/jmx_prometheus_javaagent-0.13.0.jar=30006:/root/taotao-cloud/hadoop/hadoop/journalnode.yaml $HDFS_JOURNALNODE_OPTS" 
export HDFS_ZKFC_OPTS="-javaagent:/root/taotao-cloud/hadoop/jmx_prometheus_javaagent-0.13.0.jar=30007:/root/taotao-cloud/hadoop/hadoop/zkfc.yaml $HDFS_ZKFC_OPTS"
export HDFS_HTTPFS_OPTS="-javaagent:/root/taotao-cloud/hadoop/jmx_prometheus_javaagent-0.13.0.jar=30008:/root/taotao-cloud/hadoop/hadoop/httpfs.yaml $HDFS_HTTPFS_OPTS" 
export YARN_PROXYSERVER_OPTS="-javaagent:/root/taotao-cloud/hadoop/jmx_prometheus_javaagent-0.13.0.jar=30009:/root/taotao-cloud/hadoop/hadoop/proxyserver.yaml $YARN_PROXYSERVER_OPTS" 
export MAPRED_HISTORYSERVER_OPTS="-javaagent:/root/taotao-cloud/hadoop/jmx_prometheus_javaagent-0.13.0.jar=30010:/root/taotao-cloud/hadoop/hadoop/historyserver.yaml $MAPRED_HISTORYSERVER_OPTS"

在prometheus根目录下新建configs目录，并新建文件 组件名.json
[
 {
  "targets": ["192.168.10.220:30002","ip2:port","ip3:port"]
 }
]

[
 {
  "targets": ["192.168.10.220:30003","ip2:port","ip3:port"]
 }
]

  - job_name: 'hdfs-namenode'
    file_sd_configs:
    - files:
      - configs/namenode.json

  - job_name: 'hdfs-datanode'
    file_sd_configs:
    - files:
      - configs/datanode.json

  - job_name: 'yarn-resourcemanager'
    file_sd_configs:
    - files:
      - configs/resourcemanager.json

  - job_name: 'yarn-nodemanager'
    file_sd_configs:
    - files:
      - configs/nodemanager.json

  - job_name: 'hdfs-journalnode'
    file_sd_configs:
    - files:
      - configs/journalnode.json

  - job_name: 'hdfs-zkfc'
    file_sd_configs:
    - files:
      - configs/zkfc.json

  - job_name: 'hdfs-httpfs'
    file_sd_configs:
    - files:
      - configs/httpfs.json

  - job_name: 'yarn-proxyserver'
    file_sd_configs:
    - files:
      - configs/proxyserver.json

  - job_name: 'mapred-historyserver'
    file_sd_configs:
    - files:
      - configs/historyserver.json

