##################################################
cd /opt/taotao-bigdata/

wget http://mirrors.hust.edu.cn/apache/kafka/2.6.0/kafka_2.12-2.8.0.tgz

tar -zxvf kafka_2.12-2.8.0.tgz

cd kafka2.12-2.8.0/conf

vim server.properties
# listeners=PLAINTEXT://:9092
# advertised.listeners=PLAINTEXT://host:9092
# log.dirs=/opt/cloud/kafka_2.12-2.8.0/logs
# zookeeper.connect=172.16.3.240:2181/kafka
# 这个是删除topic时才用得到的，如果不想删除topic，可以不加
# delete.topic.enable=true

vim bin/kafka-server-start.sh
修改：export KAFKA_HEAP_OPTS="-Xmx1G -Xms1G"
export KAFKA_HEAP_OPTS="-Xmx1G -Xms256M"

vim /opt/taotao-bigdata/kafka2.12-2.8.0/bin/kafka-server-stop.sh
# #PIDS=$(ps ax | grep -i 'kafka\.Kafka' | grep java | grep -v grep | awk '{print $1}')
# PIDS=$(jps -lm | grep -i 'kafka.Kafka' | awk '{print $1}')
#
# # kill -s $SIGNAL $PIDS
#   kill -s KILL $PIDS

bin/kafka-topics.sh --create --zookeeper HOST:2181 --replication-factor 1 --partitions 1 --topic TOPIC
bin/kafka-console-producer.sh --broker-list HOST:9092 --topic TOPIC
/opt/cloud/kafka_2.12-2.8.0/bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic taotao-cloud --from-beginning

/opt/cloud/kafka_2.12-2.8.0/bin/kafka-topics.sh --zookeeper 172.16.3.240:2181 --list
bin/kafka-topics.sh --zookeeper 172.16.3.240:2181/kafka --list
bin/kafka-topics.sh --zookeeper host:2181 --topic your_topic --describe

bin/kafka-consumer-groups.sh --new-consumer --bootstrap-server 172.16.3.240:9092 --list
bin/kafka-consumer-groups.sh --zookeeper host:2181 --list

bin/kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list host:9092 --topic topic

 kafka-server-stop.sh
 PIDS=$(ps ax | grep -i 'kafka\.Kafka' | grep java | grep -v grep | awk '{print $1}')
  这行代码修改为
  PIDS=$(jps -lm | grep -i 'kafka.Kafka' | awk '{print $1}')
##################### kafka.sh #############################
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
