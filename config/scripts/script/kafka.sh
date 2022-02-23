#!/bin/bash

function start_kafka() {
  /opt/cloud/kafka_2.12-2.8.0/bin/kafka-server-start.sh -daemon /opt/cloud/kafka_2.12-2.8.0/config/server.properties
  sleep 10
  echo "kafka started"
}

function stop_kafka() {
  /opt/cloud/kafka_2.12-2.8.0/bin/kafka-server-stop.sh
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
    sleep 3
    start_kafka
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
