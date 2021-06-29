#!/bin/bash

function start_kafka() {
  /opt/taotao-bigdata/kafka2.13-2.6.0/bin/kafka-server-start.sh -daemon /opt/taotao-bigdata/kafka2.13-2.6.0/config/server.properties
  sleep 10
  echo "kafka started"
}

function stop_kafka() {
  /opt/taotao-bigdata/kafka2.13-2.6.0/bin/kafka-server-stop.sh
  sleep 10
  echo "kafka stoped"
}

case $1 in
"start")
    start_kafka
    ;;
"stop")
    stop_kafka
    ;;
"restart")
    stop_kafka
    sleep 10
    start_kafka
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
