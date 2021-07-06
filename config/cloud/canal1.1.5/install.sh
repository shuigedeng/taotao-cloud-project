###########################################

cd /root/taotao-cloud/canal1.1.5/{deployer, adapter}

wget https://github.com/alibaba/canal/releases/download/canal-1.1.5/canal.deployer-1.1.5.tar.gz
wget https://github.com/alibaba/canal/releases/download/canal-1.1.5/canal.adapter-1.1.5.tar.gz

tar -zxvf canal.deployer-1.1.5.tar.gz -C deployer
tar -zxvf canal.adapter-1.1.5.tar.gz -C adapter

# 对于自建 MySQL , 需要先开启 Binlog 写入功能，配置 binlog-format 为 ROW 模式，my.cnf 中配置如下
[mysqld]
log-bin=mysql-bin # 开启 binlog
binlog-format=ROW # 选择 ROW 模式
server_id=1 # 配置 MySQL replaction 需要定义，不要和 canal 的 slaveId 重复

#授权 canal 链接 MySQL 账号具有作为 MySQL slave 的权限, 如果已有账户可直接 grant
CREATE USER canal IDENTIFIED BY 'canal';
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
-- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;
FLUSH PRIVILEGES;

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
  nohup sh /opt/taotao-cloud/canal1.1.5/deploy/bin/startup.sh \
  >/opt/taotao-cloud/canal1.1.5/deploy/canal-deploy.out 2>&1 &
  sleep 10

  nohup sh /opt/taotao-cloud/canal1.1.5/adapter/bin/startup.sh \
  >/opt/taotao-cloud/canal1.1.5/adapter/canal-adapter.out 2>&1 &
  sleep 10

  echo "canal started"
}

function stop_canal() {
   sh /opt/taotao-cloud/canal1.1.5/deploy/bin/stop.sh
   sh /opt/taotao-cloud/canal1.1.5/adapter/bin/stop.sh

   sleep 10
   echo "canal stoped"
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
