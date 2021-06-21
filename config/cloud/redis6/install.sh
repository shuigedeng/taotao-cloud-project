###########################################
wget http://download.redis.io/releases/redis-6.0.1.tar.gz

tar -zxvf redis-6.0.1.tar.gz

cd redis-6.0.1/

make PREFIX=/root/taotao-cloud/redis6.0.1 install

cd /root/taotao-cloud/redis6.0.1

cp /root/soft/redis-6.0.1/redis.conf /root/taotao-cloud/redis6.0.1/conf

vim redis.conf
# bind 192.168.1.5
# daemonize yes
# pidfile /root/taotao-cloud/redis6.0.1/pid/redis_6379.pid
# logfile "/root/taotao-cloud/redis6.0.1/logs/redis.log"
# dir /root/taotao-cloud/redis6.0.1/data
# requirepass taotao-cloud



##################### redis.sh #############################
#!/bin/bash

function start_redis() {
	/root/taotao-cloud/redis6.0.1/bin/redis-server /root/taotao-cloud/redis6.0.1/conf/redis.conf
     sleep 10
     echo "redis started"
}

function stop_redis() {
	/root/taotao-cloud/redis6.0.1/bin/redis-cli -h 192.168.1.5 -a taotao-cloud shutdown
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
