###########################################
# 编译安装
cd /root/github
git clone https://github.com/alibaba/nacos.git
cd nacos/
mvn -Prelease-nacos -Dmaven.test.skip=true clean install -U
cd nacos/distribution/target/nacos-server-2.0.2-SNAPSHOT
cp -r nacos/ /opt/taotao-cloud/nacos2.0.2
cd /root/taotao-cloud/nacos2.0.2/conf

# 直接下载安装
cd /opt/taotao-cloud
wget  https://github.com/alibaba/nacos/releases/download/2.0.2/nacos-server-2.0.2.tar.gz
tar -zxvf nacos-server-2.0.2.tar.gz
mv nacos nacos2.0.2
cd nacos2.0.2

vim application.properties
# #*************** Config Module Related Configurations ***************#
# ### If use MySQL as datasource:
spring.datasource.platform=mysql
# ### Count of DB:
db.num=1
# ### Connect URL of DB:
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.user.0=root
db.password.0=123456

server.tomcat.accesslog.enabled=false
management.endpoints.web.exposure.include=*
management.metrics.export.elastic.enabled=true

# startup.sh
JAVA_OPT="${JAVA_OPT} -server -Xms300m -Xmx300m -Xmn100m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=160m"

mysql –u root –p 123456 -D nacos</opt/taotao-cloud/nacos2.0.2/conf/nacos-mysql.sql

##################### nacos.sh #############################
#!/bin/bash

function start_nacos() {
     nohup /opt/taotao-cloud/nacos2.0.2/bin/startup.sh -m standalone \
      >/opt/taotao-cloud/nacos2.0.2/start.out 2>&1 &
     sleep 30
     echo " nacos started"
}

function stop_nacos() {
     ps -ef | grep nacos|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "nacos stoped"
}

case $1 in
"start")
    start_nacos
    ;;
"stop")
    stop_nacos
    ;;
"restart")
    stop_nacos
    sleep 15
    start_nacos
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
