################################################

wget https://archive.apache.org/dist/flink/flink-1.10.3/flink-1.10.3-bin-scala_2.12.tgz

tar -zxvf flink-1.9.3-bin-scala_2.12.tgz -C /root/taotao-bigdata

mv flink-1.9.3 flink1.9.3

export FLINK_HOME="/root/taotao-bigdata/flink1.9.3"
export PATH=$FLINK_HOME/bin:$PATH

vim flink-conf.yaml
jobmanager.rpc.address: 192.168.1.5


################################################
#!/bin/bash

spark-shell \
  --jars /opt/github/hudi-release-0.8.0/packaging/hudi-spark-bundle/target/hudi-spark3-bundle_2.12-0.8.0.jar \
  --conf 'spark.serializer=org.apache.spark.serializer.KryoSerializer'

function start_flink() {
    /opt/flink-1.9.3/bin/start-cluster.sh
    echo "flink started"
}

function stop_flink() {
     /opt/flink-1.9.3/bin/stop-cluster.sh
     echo "flink stoped"
}

case $1 in
"start")
    start_flink
    ;;
"stop")
    stop_flink
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop'
    ;;
esac
