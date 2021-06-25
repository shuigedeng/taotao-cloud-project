##################################################
cd /opt/taotao-bigdata/

wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/zookeeper-3.6.2/apache-zookeeper-3.6.2-bin.tar.gz

tar -zxvf apache-zookeeper-3.6.2-bin.tar.gz

mv apache-zookeeper-3.6.2-bin zookeeper3.6.2

cd zookeeper3.6.2

mkdir data
mkdir data/zookeeper

cd conf
cp zoo_sample.cfg zoo.cfg

vim zoo.cfg
dataDir=/opt/taotao-bigdata/zookeeper3.6.2/data/zookeeper
# #关闭zookeeper内置的管理器
admin.enableServer=false

##################### zookeeper.sh #############################
#!/bin/bash

function start_zookeeper() {
     /opt/taotao-bigdata/zookeeper3.6.2/bin/zkServer.sh start
     echo "zkServer started"
     sleep 10
}

function stop_zookeeper() {
     /opt/taotao-bigdata/zookeeper3.6.2/bin/zkServer.sh stop
     sleep 10
     echo "zkServer stoped"
}

case $1 in
"start")
    start_zookeeper
    ;;
"stop")
    stop_zookeeper
    ;;
"restart")
    stop_zookeeper
    sleep 15
    start_zookeeper
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
