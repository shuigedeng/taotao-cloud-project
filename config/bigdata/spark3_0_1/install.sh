################################################
wget https://archive.apache.org/dist/spark/spark-3.0.0/spark-3.0.0-bin-hadoop3.2.tgz

tar -zxvf spark-3.0.0-bin-hadoop3.2.tgz -C /root/taotao-bigdata

mv spark-3.0.0-bin-hadoop3.2 spark3.0.0

cp spark-env.sh.template spark-env.sh

vim spark-env.sh
export JAVA_HOME="/opt/common/jdk1.8.0_211"
export SCALA_HOME="/opt/common/scala-2.12.14"
export HADOOP_HOME="/opt/bigdata/hadoop-3.3.0"
export HADOOP_CONF_DIR="$HADOOP_HOME/etc/hadoop"
export YARN_CONF_DIR="$HADOOP_HOME/etc/hadoop"
export SPARK_MASTER_HOST=172.16.3.240
export SPARK_HOME="/opt/bigdata/spark-3.0.0-bin-hadoop3.2"
export SPARK_LOG_DIR="/opt/bigdata/spark-3.0.0-bin-hadoop3.2/logs"
export SPARK_PID_DIR="/opt/bigdata/spark-3.0.0-bin-hadoop3.2/pid"

cp slaves.template slaves && vim slaves && 172.16.3.240

cp log4j.properties.template log4j.properties

export SPARK_HOME="/opt/bigdata/spark-3.0.0-bin-hadoop3.2"
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin

spark-shell --master spark://172.16.3.240:7077

spark.master.taotaocloud.com:8080
spark.worker.taotaocloud.com:8080
spark.task.taotaocloud.com:8080

#########  测试
./bin/spark-submit \
    --class org.apache.spark.examples.SparkPi \
    --master yarn \
    --deploy-mode cluster \
    --driver-memory 4g \
    --executor-memory 2g \
    --executor-cores 1 \
    --queue default \
    examples/jars/spark-examples*.jar \
    10

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


