##################################################
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.9.2-linux-x86_64.tar.gz

tar -zxvf elasticsearch-7.9.2-linux-x86_64.tar.gz -C /root/taotao-bigdata/

cd /root/taotao-bigdata/

mv elasticsearch-7.9.2-linux-x86_64 elasticsearch7.9.2

cd elasticsearch7.9.2

cd bin
# 在最前面添加
# export JAVA_HOME=/root/taotao-common/jdk11.0.1
# export PATH=$JAVA_HOME/bin:$PATH
#
# if [ -x "$JAVA_HOME/bin/java" ]; then
#         JAVA="/root/taotao-common/jdk11.0.1/bin/java"
# else
#         JAVA=`which java`
# fi

vim log4j2.properties
# logger.deprecation.name = org.elasticsearch.deprecation
# logger.deprecation.level = error

vim jvm.options
# 修改
# -Xms2g
# -Xmx2g

# 注释
# #8-13:-XX:+UseConcMarkSweepGC
# #8-13:-XX:CMSInitiatingOccupancyFraction=75
# #8-13:-XX:+UseCMSInitiatingOccupancyOnly
# 添加如下
# -XX:+UseG1GC
# -XX:CMSInitiatingOccupancyFraction=75
# -XX:+UseCMSInitiatingOccupancyOnly

vim elasticsearch.yml
# 最后添加 xpack.ml.enabled: false
# cluster.initial_master_nodes: ["taotao-cloud"]
# http.port: 9200
# network.host: 0.0.0.0
# cluster.name: taotao-cloud-elasticsearch-application
# cluster.initial_master_nodes: ["taotao-cloud"]
# bootstrap.memory_lock: false
# bootstrap.system_call_filter: false

vim /etc/security/limits.conf
# 最后添加
# * soft nofile 65536
# * hard nofile 65536
#
# # End of file
vim /etc/sysctl.conf
# 最后添加
# vm.max_map_count=655360

adduser elasticsearch
passwd elasticsearch >> qwerjkl;
chown -R elasticsearch:elasticsearch elasticsearch7.9.2
chmod -R 777 /root

su elasticsearch
cd /root/taotao-bigdata/elasticsearch7.9.2/bin
./elasticsearch -d


##################### elasticsearch.sh #############################
#!/bin/bash

function start_elasticsearch() {
     su elasticsearch
     /root/taotao-bigdata/elasticsearch7.9.2/bin/elasticsearch -d
     echo "elasticsearch started"
     sleep 10
}

function stop_elasticsearch() {
     ps -ef | grep seata|grep -v elastic|awk '{print $2}' |xargs kill -9

     # curl -XPOST 'http://localhost:9200/_shutdown'

     sleep 10
     echo "elasticsearch stoped"
}

case $1 in
"start")
    start_elasticsearch
    ;;
"stop")
    stop_elasticsearch
    ;;
"restart")
    stop_elasticsearch
    sleep 15
    start_elasticsearch
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
