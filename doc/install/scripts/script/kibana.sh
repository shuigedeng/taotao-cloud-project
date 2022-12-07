#!/bin/bash

function start_kibana() {
  nohup /opt/cloud/kibana-7.8.0-linux-x86_64/bin/kibana \
   --allow-root \
  >/opt/cloud/kibana-7.8.0-linux-x86_64/start.out 2>&1 &
  sleep 10

  echo "kibana started"
}

function stop_kibana() {
   ps -ef | grep kibana-7.8.0-linux-x86_64|grep -v grep|awk '{print $2}' |xargs kill -9

   sleep 10
   echo "kibana stoped"
}

case $1 in
"start")
    start_kibana
    ;;
"stop")
    stop_kibana
    ;;
"restart")
    stop_kibana
    sleep 10
    start_kibana
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac

