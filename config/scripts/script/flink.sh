#!/bin/bash

function start_flink() {
    /opt/bigdata/flink-1.10.3/bin/start-cluster.sh
    echo "flink started"
}

function stop_flink() {
     /opt/bigdata/flink-1.10.3/bin/stop-cluster.sh
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

