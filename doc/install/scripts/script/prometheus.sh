#!/bin/bash

function start_prometheus() {
  nohup /opt/cloud/prometheus-2.23.0.linux-amd64/prometheus \
  --storage.tsdb.path="/opt/cloud/prometheus-2.23.0.linux-amd64/data" \
  --log.level=info \
  --web.enable-lifecycle \
  --web.enable-admin-api \
  --config.file="/opt/cloud/prometheus-2.23.0.linux-amd64/prometheus.yml" \
  >/opt/cloud/prometheus-2.23.0.linux-amd64/start.out 2>&1 &

  sleep 10
  echo "prometheus started"
}

function stop_prometheus() {
    ps -ef | grep prometheus-2.23.0.linux-amd64|grep -v grep|awk '{print $2}' |xargs kill -9
    sleep 10
    echo "prometheus stoped"
}

case $1 in
"start")
    start_prometheus
    ;;
"stop")
    stop_prometheus
    ;;
"restart")
    stop_prometheus
    sleep 10
    start_prometheus
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
