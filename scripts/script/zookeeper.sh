#!/bin/bash

function start_zookeeper() {
     /opt/taotao-bigdata/zookeeper3.6.2/bin/zkServer.sh start
     echo "zkServer started"
     sleep 10
}

function stop_zookeeper() {
     /opt/taotao-bigdata/zookeeper3.6.2/bin/zkServer.sh stop
     sleep 10
     echo "zkServer stoped"
}

case $1 in
"start")
    start_zookeeper
    ;;
"stop")
    stop_zookeeper
    ;;
"restart")
    stop_zookeeper
    sleep 15
    start_zookeeper
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
