#!/bin/bash

function start_taotao_cloud() {
  /root/script/redis.sh start
  /root/script/mysql.sh start
  /root/script/zookeeper.sh start
  /root/script/kafka.sh start
  /root/script/nacos.sh start
  /root/script/seata.sh start
  /root/script/sentinel.sh start
  /root/script/zipkin.sh start
  /root/script/skywalking.sh start
}

function stop_taotao_cloud() {
	/root/script/redis.sh stop
	/root/script/mysql.sh stop
	/root/script/zookeeper.sh stop
	/root/script/kafka.sh stop
	/root/script/nacos.sh stop
	/root/script/seata.sh stop
	/root/script/sentinel.sh stop
	/root/script/zipkin.sh stop
	/root/script/skywalking.sh stop
}

case $1 in
"start")
    start_taotao_cloud
    ;;
"stop")
    stop_taotao_cloud
    ;;
"restart")
    stop_taotao_cloud
    sleep 15
    start_taotao_cloud
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
