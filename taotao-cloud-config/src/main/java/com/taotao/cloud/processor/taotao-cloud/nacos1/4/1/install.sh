###########################################
cd /root/github

git clone https://github.com/alibaba/nacos.git

cd nacos/

mvn -Prelease-nacos -Dmaven.test.skip=true clean install -U

cd nacos/distribution/target/nacos-server-1.4.1-SNAPSHOT

cp -r nacos/ /root/taotao-cloud/nacos1.4.1

cd /root/taotao-cloud/nacos1.4.1/conf

vim application.properties
# #*************** Config Module Related Configurations ***************#
# ### If use MySQL as datasource:
# spring.datasource.platform=mysql
#
# ### Count of DB:
# db.num=1
#
# ### Connect URL of DB:
# db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
# db.user.0=root
# db.password.0=123456

mysql –uroot –p123456 -Dnacos</root/taotao-cloud/nacos-1.4.1/conf/nacos-mysql.sql


##################### nacos.sh #############################
#!/bin/bash

function start_nacos() {
     /root/taotao-cloud/nacos1.4.1/bin/startup.sh -m standalone
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
