#!/bin/bash

function start_nacos() {
     nohup /opt/cloud/nacos/bin/startup.sh -m standalone \
      >/opt/cloud/nacos/start.out 2>&1 &
     sleep 30
     echo "nacos started"
}

function stop_nacos() {
     ps -ef | grep nacos|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "nacos stoped"
}

case $1 in
"start")
    start_nacos
    ;;
"stop")
    stop_nacos
    ;;
"restart")
    stop_nacos
    sleep 15
    start_nacos
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
