#!/bin/bash

function start_nacos() {
     /opt/taotao-cloud/nacos2.0.2/bin/startup.sh -m standalone
     sleep 30
     echo " nacos started"
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
    stop_nacos
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
