#!/bin/bash

function start_taotao_bigdata() {
  #/root/script/cloud/redis.sh start

  /root/script/cloud/mysql.sh start

  /root/script/cloud/zookeeper.sh start

  /root/script/cloud/kafka.sh start

  # /root/script/cloud/prometheus.sh start

  # /root/script/cloud/grafana.sh start

  # su - elasticsearch -c "/home/elasticsearch/elasticsearch.sh start"

  /root/script/bigdata/hadoop.sh start

  /root/script/bigdata/hive.sh start

  /root/script/bigdata/spark.sh start

#  /root/script/bigdata/flink.sh start
}

function stop_taotao_bigdata() {
   #  /root/script/bigdata/flink.sh stop

  /root/script/bigdata/spark.sh stop

  /root/script/bigdata/hive.sh stop

  /root/script/bigdata/hadoop.sh stop

  # su - elasticsearch -c "/home/elasticsearch/elasticsearch.sh stop"

  # /root/script/cloud/grafana.sh stop

  # /root/script/cloud/prometheus.sh stop

  /root/script/cloud/kafka.sh stop

  /root/script/cloud/zookeeper.sh stop

  /root/script/cloud/mysql.sh stop

  # /root/script/cloud/redis.sh stop

}

case $1 in
"start")
    start_taotao_bigdata
    ;;
"stop")
    stop_taotao_bigdata
    ;;
"restart")
    stop_taotao_bigdata
    sleep 15
    start_taotao_bigdata
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
