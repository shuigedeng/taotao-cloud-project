#!/bin/bash

function start_seata() {
     nohup sh /opt/taotao-cloud/seata1.4.0/bin/seata-server.sh -p 8091 -h 103.218.242.48 >/opt/taotao-cloud/seata1.4.0/logs/seata.out 2>&1 &
     sleep 10
     echo " seata started"
}

function stop_seata() {
     ps -ef | grep seata|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "seata stoped"
}

case $1 in
"start")
    start_seata
    ;;
"stop")
    stop_seata
    ;;
"restart")
    stop_seata
    sleep 15
    start_seata
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
