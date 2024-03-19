##################################################
wget http://mirrors.hust.edu.cn/apache/kafka/2.6.0/kafka_2.13-2.6.0.tgz

tar -zxvf kafka_2.13-2.6.0.tgz  -C /root/taotao-bigdata/

cd /root/taotao-bigdata/ && mv kafka_2.13-2.6.0/ kafka2.13-2.6.0

cd kafka2.13-2.6.0/conf

vim server.properties
# listeners=PLAINTEXT://192.168.1.5:9092
# advertised.listeners=PLAINTEXT://host:9092
# log.dirs=/root/taotao-bigdata/kafka2.13-2.6.0/logs
# zookeeper.connect=192.168.1.5:2181/kafka
# 这个是删除topic时才用得到的，如果不想删除topic，可以不加
# delete.topic.enable=true

vim /root/taotao-bigdata/kafka2.13-2.6.0/bin/kafka-server-stop.sh
# #PIDS=$(ps ax | grep -i 'kafka\.Kafka' | grep java | grep -v grep | awk '{print $1}')
# PIDS=$(jps -lm | grep -i 'kafka.Kafka' | awk '{print $1}')
#
# # kill -s $SIGNAL $PIDS
#   kill -s KILL $PIDS



##################### kafka.sh #############################
#!/bin/bash

function start_kafka() {
  /root/taotao-bigdata/kafka2.13-2.6.0/bin/kafka-server-start.sh -daemon /root/taotao-bigdata/kafka2.13-2.6.0/config/server.properties
  sleep 10
  echo "kafka started"
}

function stop_kafka() {
  /root/taotao-bigdata/kafka2.13-2.6.0/bin/kafka-server-stop.sh
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
