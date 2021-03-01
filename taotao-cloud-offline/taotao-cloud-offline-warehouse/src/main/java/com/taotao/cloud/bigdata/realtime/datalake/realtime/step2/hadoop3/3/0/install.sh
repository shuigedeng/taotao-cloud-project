###########################################
wget https://mirror.bit.edu.cn/apache/hadoop/common/hadoop-3.3.0/hadoop-3.3.0.tar.gz

tar -zxvf hadoop-3.3.0.tar.gz -C /bigdata/

cd /bigdata/hadoop-3.3.0/etc/hadoop

# 1.添加环境变量
# 2.修改配置文件

###########################################
hdfs namenode -format

start-dfs.sh

start-yarn.sh

hadoop-httpfs start

hadoop fs -mkdir -p /tmp
hadoop fs -chmod 777 /tmp
hadoop fs -mkdir -p /user/hive/warehouse
hadoop fs -chmod 777 /user/hive/warehouse
hadoop fs -mkdir -p /user/hue
hadoop fs -chmod 777 /user/hue

wget http://www.congiu.net/hive-json-serde/1.3.8/hdp23/json-serde-1.3.8-jar-with-dependencies.jar
wget http://www.congiu.net/hive-json-serde/1.3.8/hdp23/json-udf-1.3.8-jar-with-dependencies.jar

hadoop fs -mkdir -p /common/lib
hadoop fs -chmod 777 /common/lib
hadoop dfs -put -f /root/soft/json-serde-1.3.8-jar-with-dependencies.jar /common/lib
hadoop dfs -put -f /root/soft/json-udf-1.3.8-jar-with-dependencies.jar /common/lib

###########################################
#!/bin/bash

function start_hadoop() {
    start-dfs.sh
    echo "dfs started"
    sleep 20

    start-yarn.sh
    echo "yarn started"

    start-history-server.sh
}

function stop_hadoop() {
    stop-yarn.sh
    echo "dfs stoped"
    sleep 10

    stop-dfs.sh
    echo "dfs stoped"

    stop-history-server.sh
}

case $1 in
"start")
    start_hadoop
    ;;
"stop")
    stop_hadoop
    ;;
"restart")
    stop_hadoop
    sleep 15
    start_hadoop
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
