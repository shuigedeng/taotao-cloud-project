#!/bin/bash

function start_hbase() {
    /opt/hbase-2.3.3/bin/start-hbase.sh
    echo "hbase started"
}

function stop_hbase() {
     /opt/hbase-2.3.3/bin/stop-hbase.sh
     echo "hbase stoped"
}

case $1 in
"start")
    start_hbase
    ;;
"stop")
    stop_hbase
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop'
    ;;
esac
