#!/bin/bash

function start_sentinel() {
     nohup java -Dserver.port=8849 -Dcsp.sentinel.dashboard.server=172.16.6.151:8849  -jar /opt/taotao-cloud/sentinel1.8.0/sentinel-dashboard-1.8.0.jar >/opt/taotao-cloud/sentinel1.8.0/start.log 2>&1 &
     sleep 10
     echo " sentinel started"
}

function stop_sentinel() {
     ps -ef | grep sentinel|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "sentinel stoped"
}

case $1 in
"start")
    start_sentinel
    ;;
"stop")
    stop_sentinel
    ;;
"restart")
    stop_sentinel
    sleep 15
    start_sentinel
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
