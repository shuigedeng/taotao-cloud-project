################################################
wget https://archive.apache.org/dist/spark/spark-3.0.0/spark-3.0.0-bin-hadoop3.2.tgz

tar -zxvf spark-3.0.0-bin-hadoop3.2.tgz -C /root/taotao-bigdata

mv spark-3.0.0-bin-hadoop3.2 spark3.0.0

cp spark-env.sh.template spark-env.sh

vim spark-env.sh
# export HADOOP_HOME="/root/taotao-bigdata/hadoop3.3.0"
# export JAVA_HOME="/root/taotao-common/jdk1.8.0_211"
# export SCALA_HOME="/root/taotao-common/scala2.12.11"
# export HADOOP_CONF_DIR="$HADOOP_HOME/etc/hadoop"
# export YARN_CONF_DIR="$HADOOP_HOME/etc/hadoop"
# export SPARK_MASTER_HOST=taotao-cloud
# export SPARK_HOME="/root/taotao-bigdata/spark3.0.0"

cp slaves.template slaves && vim slaves && taotao-cloud

cp log4j.properties.template log4j.properties

export SPARK_HOME="/root/taotao-bigdata/spark3.0.0"
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin

./spark-shell --master spark://taotao-cloud:7077

spark.master.taotaocloud.com:8080
spark.worker.taotaocloud.com:8080
spark.task.taotaocloud.com:8080

################################################
#!/bin/bash

function start_spark() {
    /root/taotao-bigdata/spark3.0.0/sbin/start-all.sh
    echo "spark started"

    start-history-server.sh
}

function stop_spark() {
    /root/taotao-bigdata/spark3.0.0/sbin/stop-all.sh
    echo "spark stoped"

    stop-history-server.sh
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
