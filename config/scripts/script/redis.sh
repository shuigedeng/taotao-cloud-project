#!/bin/bash

function start_redis() {
	/opt/cloud/redis-6.2.4/bin/redis-server /opt/cloud/redis-6.2.4/redis.conf
  sleep 10
  echo "redis started"
}

function stop_redis() {
	/opt/cloud/redis-6.2.4/bin/redis-cli -h 127.0.0.1 -a taotao-cloud shutdown 2>/dev/null
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
