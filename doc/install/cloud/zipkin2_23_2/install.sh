###########################################
# zipkin-server最新版本下载地址：
# https://repo1.maven.org/maven2/io/zipkin/zipkin-server/2.23.2/zipkin-server-2.23.2-exec.jar
#
# zipkin-server更多版本（2.14.1-2.23.2【当前最新版】）下载地址：
# https://repo1.maven.org/maven2/io/zipkin/zipkin-server/

cd /opt/soft/

wget https://repo1.maven.org/maven2/io/zipkin/zipkin-server/2.23.2/zipkin-server-2.23.2-exec.jar

mkdir /opt/cloud/zipkin
cp zipkin-server-2.23.2-exec.jar /opt/cloud/zipkin

# zipkin mysql脚本
https://github.com/openzipkin/zipkin/blob/master/zipkin-storage/mysql-v1/src/main/resources/mysql.sql

mysql –uroot –p
create database `taotao-cloud-zipkin`;
use  `taotao-cloud-zipkin`;
source /opt/cloud/zipkin/mysql.sql

# java -jar zipkin-server-2.23.2-exec.jar
# --zipkin.collector.rabbitmq.addresses=localhost
# --zipkin.collector.rabbitmq.username=xxx
# --zipkin.collector.rabbitmq.password=xxx
# --STORAGE_TYPE=mysql
# --MYSQL_HOST=192.168.10.220
# --MYSQL_TCP_PORT=3306
# --MYSQL_DB=cloud-zipkin
# --MYSQL_USER=root
# --MYSQL_PASS=123456

# zipkin elasticsearch
java -jar zipkin-server-2.23.2-exec.jar
#--KAFKA_BOOTSTRAP_SERVERS=localhost:9092
--STORAGE_TYPE=elasticsearch
--ES_HOSTS=http://127.0.0.1:9200

##################### zipkin.sh #############################
#!/bin/bash

function start_zipkin() {
     nohup java -jar /opt/cloud/zipkin/zipkin-server-2.23.2-exec.jar \
        --MYSQL_HOST=127.0.0.1 \
        --MYSQL_TCP_PORT=3306 \
        --MYSQL_DB=taotao-cloud-zipkin \
        --MYSQL_USER=root \
        --MYSQL_PASS=123456 \
     >/opt/cloud/zipkin/start.out 2>&1 &
     sleep 10
     echo "zipkin started"
}

function stop_zipkin() {
     ps -ef | grep zipkin|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "zipkin stoped"
}

case $1 in
"start")
    start_zipkin
    ;;
"stop")
    stop_zipkin
    ;;
"restart")
    stop_zipkin
    sleep 15
    start_zipkin
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac

