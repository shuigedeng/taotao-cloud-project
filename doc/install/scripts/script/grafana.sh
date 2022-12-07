#!/bin/bash

function start_grafana() {
  nohup /opt/cloud/grafana-8.0.3/bin/grafana-server \
  -homepath /opt/cloud/grafana-8.0.3 \
   >/opt/cloud/grafana-8.0.3/start.out 2>&1 &

  sleep 10
  echo "grafana started"
}

function stop_grafana() {
    ps -ef | grep grafana|grep -v grep|awk '{print $2}' |xargs kill -9

    sleep 10
    echo "grafana stoped"
}

case $1 in
"start")
    start_grafana
    ;;
"stop")
    stop_grafana
    ;;
"restart")
    stop_grafana
    sleep 10
    start_grafana
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac

