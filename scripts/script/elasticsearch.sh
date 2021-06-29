#!/bin/bash

function start_elasticsearch() {
     su elasticsearch
     /root/taotao-bigdata/elasticsearch7.9.2/bin/elasticsearch -d
     echo "elasticsearch started"
     sleep 10
}

function stop_elasticsearch() {
     ps -ef | grep seata|grep -v elastic|awk '{print $2}' |xargs kill -9

     # curl -XPOST 'http://localhost:9200/_shutdown'

     sleep 10
     echo "elasticsearch stoped"
}

case $1 in
"start")
    start_elasticsearch
    ;;
"stop")
    stop_elasticsearch
    ;;
"restart")
    stop_elasticsearch
    sleep 15
    start_elasticsearch
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
