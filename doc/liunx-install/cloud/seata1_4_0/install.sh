###########################################
cd /opt/cloud/

wget https://github.com/seata/seata/releases/download/v1.4.0/seata-server-1.4.0.zip

unzip seata-server-1.4.0.zip

mv /opt/cloud/seata /opt/cloud/

cd /opt/cloud/seata/conf

# 获取seata源码 后面要用
cd /opt/github
git clone https://github.com/seata/seata.git
cd seata
mvn -Prelease-all -DskipTests clean install -U

# 1/修改registry.conf
使用的nacos作为配置中心和注册中心，使用将配置文件改为nacos

# 2.修改file.conf
cp /opt/soft/mysql-connector-java-8.0.20.jar /opt/cloud/seata/lib
修改数据库地址，注意mysql5/mysql8驱动不同
创建数据库 在数据库中执行 sql文件
mysql -uroot -p
create database `taotao-cloud-seata`;
use `taotao-cloud-seata`;
source /opt/cloud/seata/conf/mysql.sql
#source /opt/github/seata/script/server/db/mysql.sql

# 3.导入config.txt配置到nacos
cd conf
cp /opt/github/seata/script/config-center/config.txt .
cp -r /opt/github/seata/script/config-center/nacos .

cd nacos
nacos-config.sh -h localhost -p 8848 -g SEATA_GROUP -t 6f9ac92d-8a72-4581-92b0-af71dbd67e2e -u nacos -w nacos

# 4.在业务系统中执行sql文件
cp /opt/github/seata/script/client/at/db/mysql.sql /opt/cloud/seata/conf/client-mysql.sql

vim bin/seata-server.sh

exec “$JAVACMD” $JAVA_OPTS -server -Xmx1024m -Xms1024m -Xmn512m -Xss256k

##################### seata.sh #############################
#!/bin/bash

function start_seata() {
     nohup sh /opt/cloud/seata/bin/seata-server.sh -p 8091 -h 192.168.10.220 \
     >/opt/cloud/seata/logs/seata.out 2>&1 &
     sleep 10
     echo "seata started"
}

function stop_seata() {
     ps -ef | grep seata|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "seata stoped"
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
