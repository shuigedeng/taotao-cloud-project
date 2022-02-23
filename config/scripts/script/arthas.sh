#!/bin/bash

function start_arthas() {
   nohup java -jar \
    -Dserver.port=8081 \
    /opt/cloud/arthas/arthas-tunnel-server-3.5.2-fatjar.jar \
    >/opt/cloud/arthas/start.out 2>&1 &

  sleep 10
  echo "arthas started"
}

function stop_arthas() {
  ps -ef | grep arthas|grep -v grep|awk '{print $2}' |xargs kill -9
  sleep 10
  echo "arthas stoped"
}

case $1 in
"start")
    start_arthas
    ;;
"stop")
    stop_arthas
    ;;
"restart")
    stop_arthas
    sleep 3
    start_arthas
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac

