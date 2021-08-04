##################################################
cd /opt/soft

wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/zookeeper-3.6.3/apache-zookeeper-3.6.3-bin.tar.gz

tar -zxvf apache-zookeeper-3.6.3-bin.tar.gz -C /opt/cloud

cd /opt/cloud/apache-zookeeper-3.6.3-bin

mkdir data
mkdir data/zookeeper

cd conf
cp zoo_sample.cfg zoo.cfg

vim zoo.cfg
dataDir=/opt/cloud/apache-zookeeper-3.6.3-bin/data/zookeeper
# #关闭zookeeper内置的管理器
admin.enableServer=false

#zkServer.sh
vim conf/java.env

#!/bin/sh
export JAVA_HOME="/opt/common/jdk1.8.0_211"
# heap size MUST be modified according to cluster environment
#这里是需要设置的内存大小，-Xms512m 最小内存 -Xmx1024m 最大使用内存
export JVMFLAGS="-Xms512m -Xmx1024m $JVMFLAGS"

##################### zookeeper.sh #############################
#!/bin/bash

function start_zookeeper() {
     /opt/cloud/apache-zookeeper-3.6.3-bin/bin/zkServer.sh start
     echo "zkServer started"
     sleep 10
}

function stop_zookeeper() {
     /opt/cloud/apache-zookeeper-3.6.3-bin/bin/zkServer.sh stop
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
