#!/bin/bash

# nacos
mkdir -p /root/cloud/nacos-nginx/config.d

# seata
mkdir -p /root/cloud/seata
touch /root/cloud/seata/application.yml

# es
mkdir -p /root/cloud/es/es1/data
mkdir -p /root/cloud/es/es2/data
mkdir -p /root/cloud/es/es3/data
mkdir -p /root/cloud/es/es1/plugins
mkdir -p /root/cloud/es/es2/plugins
mkdir -p /root/cloud/es/es3/plugins

#prometheus
mkdir -p /root/cloud/prometheus
touch /root/cloud/prometheus/prometheus.yml
touch /root/cloud/prometheus/node_down.yml
touch /root/cloud/prometheus/alertmanager.yml

mkdir -p /root/cloud/rocketmq/namesrv/logs
mkdir -p /root/cloud/rocketmq/namesrv/store
mkdir -p /root/cloud/rocketmq/broker1/logs
mkdir -p /root/cloud/rocketmq/broker1/store
mkdir -p /root/cloud/rocketmq/broker2/logs
mkdir -p /root/cloud/rocketmq/broker2/store
mkdir -p /root/cloud/rocketmq/broker3/logs
mkdir -p /root/cloud/rocketmq/broker3/store

mkdir -p /root/cloud/rabbitmq/rabbitmq1/data
mkdir -p /root/cloud/rabbitmq/rabbitmq2/data
mkdir -p /root/cloud/rabbitmq/rabbitmq3/data

mkdir -p /root/cloud/yapi/log
touch /root/cloud/yapi/log/yapi.log

mkdir -p /root/cloud/yapi/mongodb

# mysql8 单机版初始化安装
mkdir -p /root/mysql/data
mkdir -p /root/mysql/conf
touch  /root/mysql/conf/my.cnf

docker run --name mysql  \
--restart=always \
--privileged=true \
-e MYSQL_ROOT_PASSWORD=123456 \
-p 3306:3306 \
-v /root/mysql/data:/var/lib/mysql \
-v /root/mysql/conf/my.cnf:/etc/mysql/my.cnf \
-d mysql

sleep 2m

mysql -h 127.0.0.1 -uroot -p123456 -e "
use mysql;
update user set host='%' where user='root';
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
alter user 'root'@'%' identified by '123456' password expire never;
flush privileges;

create database `taotao-cloud-nacos-2.1.0`;
use `taotao-cloud-nacos-2.1.0`;
source nacos.sql;

create database `taotao-cloud-seata-1.5.1`;
use `taotao-cloud-seata-1.5.1`;
source seata.sql;

create database `taotao-cloud-zipkin-0.3.0`;
use `taotao-cloud-zipkin-0.3.0`;
source zipkin.sql;
"




