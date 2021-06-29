#!/bin/bash

function start_redis() {
	/opt/taotao-cloud/redis-6.2.4/bin/redis-server /opt/taotao-cloud/redis-6.2.4/redis.conf
  sleep 10
  echo "redis started"
}

function stop_redis() {
	/opt/taotao-cloud/redis-6.2.4/bin/redis-cli -h 192.168.1.10 -a taotao-cloud shutdown
  sleep 10
  echo "redis stoped"
}

case $1 in
"start")
    start_redis
    ;;
"stop")
    stop_redis
    ;;
"restart")
    stop_redis
    sleep 10
    start_redis
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
