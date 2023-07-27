###########################################

cd /opt/soft

wget https://github.com/alibaba/canal/releases/download/canal-1.1.5/canal.deployer-1.1.5.tar.gz
wget https://github.com/alibaba/canal/releases/download/canal-1.1.5/canal.adapter-1.1.5.tar.gz
wget https://github.com/alibaba/canal/releases/download/canal-1.1.5/canal.admin-1.1.5.tar.gz

mkdir /op/cloud/canal/{deployer, adapter, admin}

tar -zxvf canal.deployer-1.1.5.tar.gz -C /opt/cloud/canal/deployer
tar -zxvf canal.adapter-1.1.5.tar.gz -C /opt/cloud/canal/adapter
tar -zxvf canal.admin-1.1.5.tar.gz -C /opt/cloud/canal/admin

export CANAL_ADMIN_HOME=/opt/cloud/canal/admin
export PATH=$PATH:${CANAL_ADMIN_HOME}/bin

export CANAL_SERVER_HOME=/opt/cloud/canal/deployer
export PATH=$PATH:${CANAL_SERVER_HOME}/bin

# 对于自建 MySQL , 需要先开启 Binlog 写入功能，配置 binlog-format 为 ROW 模式，my.cnf 中配置如下

[mysqld]
server-id=1
log-bin=mysql-bin
binlog-format=ROW
binlog-ignore-db=information_schema
binlog-ignore-db=mysql
binlog-ignore-db=performance_schema
binlog-ignore-db=sys

log-bin用于指定binlog日志文件名前缀，默认存储在/var/lib/mysql 目录下。
server-id用于标识唯一的数据库，不能和别的服务器重复，建议使用ip的最后一段，默认值也不可以。
binlog-ignore-db：表示同步的时候忽略的数据库。
binlog-do-db：指定需要同步的数据库（如果没有此项，表示同步所有的库）。

mysql -uroot -p
set global validate_password.length=6;
set global validate_password.policy=LOW;

CREATE USER canaladmin IDENTIFIED BY '123456';
GRANT ALL ON `taotao-cloud-canal-manager`.* TO 'canaladmin'@'%';
FLUSH PRIVILEGES;

CREATE USER canal IDENTIFIED BY '123456';
grant system_user on *.* to 'root';
GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%';

FLUSH PRIVILEGES;

cp /opt/soft/mysql-connector-java-8.0.20.jar /opt/cloud/canal/admin/lib/
cp /opt/soft/mysql-connector-java-8.0.20.jar /opt/cloud/canal/deployer/lib/

### 配置canal --admin
vi /opt/cloud/canal/admin/conf/application.yml
server:
  port: 8089
spring:
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8

spring.datasource:
  address: 127.0.0.1:3306
  database: taotao-cloud-canal-manager
  username: canaladmin
  password: 123456
  driver-class-name: com.mysql.cj.jdbc.Driver
  url: jdbc:mysql://${spring.datasource.address}/${spring.datasource.database}?useUnicode=true&characterEncoding=UTF-8&useSSL=false
  hikari:
    maximum-pool-size: 30
    minimum-idle: 1

canal:
  adminUser: admin
  adminPasswd: admin

1.进入mysql 中执行
mysql -uroot -p
use mysql
soruce /opt/cloud/canal/admin/conf/canal_manager.sql

---
CREATE DATABASE IF NOT EXISTS `taotao-cloud-canal-manager`;

USE `taotao-cloud-canal-manager`;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for canal_adapter_config
-- ----------------------------
DROP TABLE IF EXISTS `canal_adapter_config`;
CREATE TABLE `canal_adapter_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `content` text NOT NULL,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_cluster
-- ----------------------------
DROP TABLE IF EXISTS `canal_cluster`;
CREATE TABLE `canal_cluster` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(63) NOT NULL,
  `zk_hosts` varchar(255) NOT NULL,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_config
-- ----------------------------
DROP TABLE IF EXISTS `canal_config`;
CREATE TABLE `canal_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cluster_id` bigint(20) DEFAULT NULL,
  `server_id` bigint(20) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `content` text NOT NULL,
  `content_md5` varchar(128) NOT NULL,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sid_UNIQUE` (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_instance_config
-- ----------------------------
DROP TABLE IF EXISTS `canal_instance_config`;
CREATE TABLE `canal_instance_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cluster_id` bigint(20) DEFAULT NULL,
  `server_id` bigint(20) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `content` text NOT NULL,
  `content_md5` varchar(128) DEFAULT NULL,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_node_server
-- ----------------------------
DROP TABLE IF EXISTS `canal_node_server`;
CREATE TABLE `canal_node_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cluster_id` bigint(20) DEFAULT NULL,
  `name` varchar(63) NOT NULL,
  `ip` varchar(63) NOT NULL,
  `admin_port` int(11) DEFAULT NULL,
  `tcp_port` int(11) DEFAULT NULL,
  `metric_port` int(11) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_user
-- ----------------------------
DROP TABLE IF EXISTS `canal_user`;
CREATE TABLE `canal_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(31) NOT NULL,
  `password` varchar(128) NOT NULL,
  `name` varchar(31) NOT NULL,
  `roles` varchar(31) NOT NULL,
  `introduction` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Records of canal_user
-- ----------------------------
BEGIN;
INSERT INTO `canal_user` VALUES (1, 'admin', '6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', 'Canal Manager', 'admin', NULL, NULL, '2019-07-14 00:05:28');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
---

/opt/cloud/canal/admin/bin/startup.sh
/opt/cloud/canal/admin/bin/stop.sh
http://127.0.0.1:8089/ 默认登录 admin/123456


######## 配置canal --deployer
vi /opt/cloud/canal/deployer/conf/canal.properties

#################################################
#########               destinations            #############
#################################################
# 修改目标
canal.destinations = example
# conf root dir
canal.conf.dir = ../conf
# auto scan instance dir add/remove and start/stop instance
canal.auto.scan = true
canal.auto.scan.interval = 5

canal.instance.tsdb.spring.xml = classpath:spring/tsdb/h2-tsdb.xml
#canal.instance.tsdb.spring.xml = classpath:spring/tsdb/mysql-tsdb.xml

canal.instance.global.mode = spring
canal.instance.global.lazy = false
canal.instance.global.manager.address = ${canal.admin.manager}
#canal.instance.global.spring.xml = classpath:spring/memory-instance.xml
canal.instance.global.spring.xml = classpath:spring/file-instance.xml
#canal.instance.global.spring.xml = classpath:spring/default-instance.xml

##################################################
#########                    Kafka                   #############
##################################################
# 修改kafka
kafka.bootstrap.servers = 192.168.10.220:9092
kafka.acks = all
kafka.compression.type = none
kafka.batch.size = 16384
kafka.linger.ms = 1
kafka.max.request.size = 1048576
kafka.buffer.memory = 33554432
kafka.max.in.flight.requests.per.connection = 1
kafka.retries = 0

kafka.kerberos.enable = false
kafka.kerberos.krb5.file = "../conf/kerberos/krb5.conf"
kafka.kerberos.jaas.file = "../conf/kerberos/jaas.conf"

cd /opt/cloud/canal/deployer/conf/example/instance.properties
#################################################
## mysql serverId , v1.0.26+ will autoGen
# canal.instance.mysql.slaveId=0
# 修改slaveId 不能和mysql slaveId相同
canal.instance.mysql.slaveId=12368
# enable gtid use true/false
canal.instance.gtidon=false

# position info
canal.instance.master.address=127.0.0.1:3306
canal.instance.master.journal.name=
canal.instance.master.position=
canal.instance.master.timestamp=
canal.instance.master.gtid=

# rds oss binlog
canal.instance.rds.accesskey=
canal.instance.rds.secretkey=
canal.instance.rds.instanceId=

# table meta tsdb info
# 修改为false
canal.instance.tsdb.enable=false
#canal.instance.tsdb.url=jdbc:mysql://127.0.0.1:3306/canal_tsdb
#canal.instance.tsdb.dbUsername=canal
#canal.instance.tsdb.dbPassword=canal

#canal.instance.standby.address =
#canal.instance.standby.journal.name =
#canal.instance.standby.position =
#canal.instance.standby.timestamp =
#canal.instance.standby.gtid=

# username/password
# 修改为canal
canal.instance.dbUsername=canal
# 修改为123456
canal.instance.dbPassword=123456

canal.instance.defaultDatabaseName = location
canal.instance.connectionCharset = UTF-8
# enable druid Decrypt database password
canal.instance.enableDruid=false
#canal.instance.pwdPublicKey=MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALK4BUxdDltRRE5/zXpVEVPUgunvscYFtEip3pmLlhrWpacX7y7GCMo2/JM6LeHmiiNdH1FWgGCpUfircSwlWKUCAwEAAQ==

canal.instance.defaultDatabaseName = location

# table regex
canal.instance.filter.regex=location\\..*

# table black regex
# 修改为注释
#canal.instance.filter.black.regex=mysql\\.slave_.*

canal.instance.filter.black.regex=
# table field filter(format: schema1.tableName1:field1/field2,schema2.tableName2:field1/field2)
#canal.instance.filter.field=test1.t_product:id/subject/keywords,test2.t_company:id/name/contact/ch
# table field black filter(format: schema1.tableName1:field1/field2,schema2.tableName2:field1/field2)
#canal.instance.filter.black.field=test1.t_product:subject/product_image,test2.t_company:id/name/contact/ch

# mq config
canal.mq.topic=bin-log-taotao-cloud
# dynamic topic route by schema or table regex
#canal.mq.dynamicTopic=mytest1.user,mytest2\\..*,.*\\..*
canal.mq.partition=0
# hash partition config
#canal.mq.partitionsNum=3
#canal.mq.partitionHash=test.table:id^name,.*\\..*
#canal.mq.dynamicTopicPartitionNum=test.*:4,mycanal:6
#################################################


cd deployer
vi conf/example/instance.properties

cd adapter
conf/application.yml

# 另外需要配置conf/es7/*.yml文件，adapter将会自动加载conf/es7下的所有.yml结尾的配置文件
CREATE TABLE `test` (
  `id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `address` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 需要手动去es中创建索引，比如这里使用es-head创建
{
    "mappings":{
        "_doc":{
            "properties":{
                "name":{
                    "type":"text"
                },
                "address":{
                    "type":"text"
                }
            }
        }
    }
}

conf/es7/test.yml
"""
dataSourceKey: defaultDS
destination: example
groupId:
esMapping:
  _index: test
  _type: _doc
  _id: _id
  upsert: true
  sql: "select a.id as _id,a.name,a.address from test a"
  commitBatch: 3000
"""

##################### canal.sh #############################
#!/bin/bash

function start_canal() {
  nohup /opt/cloud/canal/admin/bin/startup.sh \
  >/opt/cloud/canal/admin/canal-admin.out 2>&1 &
  sleep 10
  echo "canal admin started"

  nohup /opt/cloud/canal/deployer/bin/startup.sh \
  >/opt/cloud/canal/deployer/canal-deployer.out 2>&1 &
  sleep 10

  echo "canal deployer started"
}

function stop_canal() {
   /opt/cloud/canal/admin/bin/stop.sh
   sleep 10
   echo "canal admin stoped"

   /opt/cloud/canal/deployer/bin/stop.sh
   sleep 10
   echo "canal deployer stoped"
}

case $1 in
"start")
    start_canal
    ;;
"stop")
    stop_canal
    ;;
"restart")
    stop_canal
    sleep 10
    start_canal
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
