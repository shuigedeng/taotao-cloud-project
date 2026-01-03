################################################
wget https://archive.apache.org/dist/spark/spark-3.0.0/spark-3.0.0-bin-hadoop3.2.tgz

tar -zxvf spark-3.0.0-bin-hadoop3.2.tgz -C /root/taotao-bigdata

mv spark-3.0.0-bin-hadoop3.2 spark3.0.0

cp spark-env.sh.template spark-env.sh

# 环境变量配置
export SPARK_HOME="/opt/bigdata/spark-3.0.0-bin-hadoop3.2"
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin

vim spark-env.sh
export JAVA_HOME="/opt/common/jdk1.8.0_211"
export SCALA_HOME="/opt/common/scala-2.12.14"
export HADOOP_HOME="/opt/bigdata/hadoop-3.3.0"
export HADOOP_CONF_DIR="$HADOOP_HOME/etc/hadoop"
export YARN_CONF_DIR="$HADOOP_HOME/etc/hadoop"
export SPARK_MASTER_HOST=127.0.0.1
export SPARK_HOME="/opt/bigdata/spark-3.0.0-bin-hadoop3.2"
export SPARK_LOG_DIR="/opt/bigdata/spark-3.0.0-bin-hadoop3.2/logs"
export SPARK_PID_DIR="/opt/bigdata/spark-3.0.0-bin-hadoop3.2/pid"
# 该目录需要事先在hdfs上创建好
export SPARK_HISTORY_OPTS="-Dspark.history.ui.port=18080 -Dspark.history.retainedApplications=3 -Dspark.history.fs.logDirectory=hdfs://127.0.0.1:8020/spark/historylog"

# history server 配置
vim spark-default.conf
spark.eventLog.enabled true
spark.eventLog.compress true
# 该目录需要事先在hdfs上创建好
spark.eventLog.dir hdfs://127.0.0.1:8020/spark/historylog

cp slaves.template slaves && vim slaves && 192.168.10.200
# 日志配置
cp log4j.properties.template log4j.properties

# 测试
spark-shell --master spark://192.168.10.200:7077
spark-shell \
  --jars /opt/github/hudi-release-0.8.0/packaging/hudi-spark-bundle/target/hudi-spark3-bundle_2.12-0.8.0.jar \
  --conf 'spark.serializer=org.apache.spark.serializer.KryoSerializer'

192.168.10.200.:8080
192.168.10.200.:18080
192.168.10.200.:8088
192.168.10.200.:4040

#########  测试
-- master local[2]
-- master spark://192.168.10.200:7077
-- master yarn

--deploy-mode client
--deploy-mode cluster

./bin/spark-submit \
    --class org.apache.spark.examples.SparkPi \
    --master spark://192.168.10.200:7077 \
    --deploy-mode client \
    --driver-memory 4g \
    --executor-memory 2g \
    --executor-cores 2 \
    --queue default \
    examples/jars/spark-examples*.jar \
    10

./bin/spark-submit \
  --class com.taotao.cloud.bigdata.spark.JavaWordCount \
  --master yarn \
  --deploy-mode cluster \
  --driver-memory 2g \
  --executor-memory 1g \
  --executor-cores 2 \
  --queue default \
  /opt/bigdata/spark-3.0.0-bin-hadoop3.2/jar/taotao-cloud-spark-2026.02.jar \
  /opt/spark/input /opt/spark/output

################################################
#!/bin/bash

function start_spark() {
    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/start-all.sh
    echo "spark started"
    sleep 10

    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/start-history-server.sh
    echo "spark history server started"
    sleep 10

    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/start-thriftserver.sh
    echo "spark thriftserver started"
    sleep 10
}

function stop_spark() {
    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/stop-all.sh
    echo "spark stoped"
    sleep 10

    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/stop-history-server.sh
    echo "spark history server stoped"
    sleep 10

    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/stop-thriftserver.sh
    echo "spark thriftserver stoped"
    sleep 10
}

case $1 in
"start")
    start_spark
    ;;
"stop")
    stop_spark
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop'
    ;;
esac


