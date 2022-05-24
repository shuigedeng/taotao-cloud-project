#!/bin/bash

function start_redis() {
	/opt/cloud/redis_cluster/redis-6.0.9/src/redis-server /opt/cloud/redis_cluster/7100/redis.conf
	/opt/cloud/redis_cluster/redis-6.0.9/src/redis-server /opt/cloud/redis_cluster/7101/redis.conf
	/opt/cloud/redis_cluster/redis-6.0.9/src/redis-server /opt/cloud/redis_cluster/7102/redis.conf
	/opt/cloud/redis_cluster/redis-6.0.9/src/redis-server /opt/cloud/redis_cluster/7103/redis.conf
	/opt/cloud/redis_cluster/redis-6.0.9/src/redis-server /opt/cloud/redis_cluster/7104/redis.conf
	/opt/cloud/redis_cluster/redis-6.0.9/src/redis-server /opt/cloud/redis_cluster/7105/redis.conf

  sleep 10
  echo "redis cluster started"
}

function stop_redis() {
  /opt/cloud/redis_cluster/redis-6.0.9/src/redis-cli -p 7100 -h 192.168.10.200 shutdown 2>/dev/null
  /opt/cloud/redis_cluster/redis-6.0.9/src/redis-cli -p 7101 -h 192.168.10.200 shutdown 2>/dev/null
  /opt/cloud/redis_cluster/redis-6.0.9/src/redis-cli -p 7102 -h 192.168.10.200 shutdown 2>/dev/null
  /opt/cloud/redis_cluster/redis-6.0.9/src/redis-cli -p 7103 -h 192.168.10.200 shutdown 2>/dev/null
  /opt/cloud/redis_cluster/redis-6.0.9/src/redis-cli -p 7104 -h 192.168.10.200 shutdown 2>/dev/null
  /opt/cloud/redis_cluster/redis-6.0.9/src/redis-cli -p 7105 -h 192.168.10.200 shutdown 2>/dev/null

  #for port in $(seq 7100 7105)
  #do
  #  PID=$(netstat -ntulp | grep :$port | awk '{print $7}' | awk -F"/" '{print $1}')
#
  #  if [ $? -eq 0 ]; then
  #      echo "process id: $PID"
  #  else
  #      echo "process process not exist"
  #      exit
  #  fi
#
  #  kill -9 ${PID}
#
  #  if [ $? -eq 0 ]; then
  #      echo "kill $port success"
  #  else
  #      echo "kill $port fail"
  #  fi
  #done

  sleep 10
  echo "redis cluster stoped"
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

