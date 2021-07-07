#!/bin/bash

function start_taotao_cloud() {
  /root/script/mysql.sh start
  /root/script/zookeeper.sh start
  /root/script/kafka.sh start
  /root/script/hadoop.sh start
  /root/script/hive.sh start
}

function stop_taotao_cloud() {
	/root/script/mysql.sh stop
	/root/script/zookeeper.sh stop
	/root/script/kafka.sh stop
	/root/script/hadoop.sh stop
	/root/script/hive.sh stop
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
