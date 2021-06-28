###########################################
cd /opt/taotao-cloud/

wget https://github.com/seata/seata/releases/download/v1.4.0/seata-server-1.4.0.zip

unzip seata-server-1.4.0.zip

mv /opt/taotao-cloud/seata /opt/taotao-cloud/seata1.4.0

cd /opt/taotao-cloud/seata1.4.0/conf

# 获取seata源码 后面要用
cd /opt/github
git clone https://github.com/seata/seata.git
cd seata
mvn -Prelease-all -DskipTests clean install -U

# 修改registry.conf
使用的nacos作为配置中心和注册中心，使用将配置文件改为nacos

# 修改file.conf
修改数据库地址，注意mysql5/mysql8驱动不同

创建数据库 在数据库中执行 sql文件
mysql –uroot –p123456 -Dseata</taotao-cloud/seata/conf/mysql.sql

# 导入config.txt配置到nacos
cd conf
cp /opt/github/seata/script/config-service/config.txt .
cp -r /opt/github/seata/script/config-service/nacos .

cd nacos
sh nacos-config.sh -h localhost -p 8848 -g SEATA_GROUP -u nacos -w nacos

# 在业务系统中执行sql文件
cp /opt/github/seata/script/client/at/db/mysql.sql /opt/taotao-cloud/seata1.4.0/conf/client-mysql.sql

##################### seata.sh #############################
#!/bin/bash

function start_seata() {
     nohup sh /opt/taotao-cloud/seata1.4.0/bin/seata-server.sh -p 8091 -h 103.218.242.48 >/opt/taotao-cloud/seata1.4.0/logs/seata.out 2>&1 &
     sleep 10
     echo " nacos started"
}

function stop_seata() {
     ps -ef | grep seata|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "nacos stoped"
}

case $1 in
"start")
    start_seata
    ;;
"stop")
    stop_seata
    ;;
"restart")
    stop_seata
    sleep 15
    start_seata
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
