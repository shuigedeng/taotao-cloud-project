##################################################
wget https://repo1.maven.org/maven2/com/facebook/presto/presto-server/0.236/presto-server-0.236.tar.gz

tar presto-server-0.236.tar.gz -C /root/taotao-bigdata

mv presto-server-0.236 presto0.236

mkdri etc data

cd presto0.236/etc

# 添加hudi支持
mkdir presto0.236/plugins/hive-hadoop3
cp hudi-presto-bundle-0.6.2-incubating.jar .


http://taotao-cloud:8080/ui

# 客户端安装

##################################################
#!/bin/bash

function start_presto() {
    /root/taotao-bigdata/presto0.236/bin/launcher start
    echo "presto started"
}

function stop_presto() {
    /root/taotao-bigdata/presto0.236/bin/launcher stop
    echo "presto stoped"
}

case $1 in
"start")
    start_presto
    ;;
"stop")
    stop_presto
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop'
    ;;
esac


./presto --server taotao-cloud:8080 --catalog hive  --schema default
