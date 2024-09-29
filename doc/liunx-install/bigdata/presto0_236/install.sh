##################################################
wget https://repo1.maven.org/maven2/com/facebook/presto/presto-server/0.236/presto-server-0.236.tar.gz
wget https://repo1.maven.org/maven2/com/facebook/presto/presto-cli/0.236/presto-cli-0.236-executable.jar

tar presto-server-0.236.tar.gz -C /root/taotao-bigdata

mv presto-server-0.236 presto0.236

mkdir etc data

cd presto0.236/etc

# 添加hudi支持
cp hudi-presto-bundle-0.6.2-incubating.jar presto0.236/plugins/hive-hadoop2

http://192.168.10.220:28081/ui

# 客户端安装
mv presto-cli-0.236-executable.jar presto
chmod u+x presto

##################################################
#!/bin/bash

function start_presto() {
    /opt/bigdata/presto-server-0.236/bin/launcher start
    echo "presto started"
}

function stop_presto() {
    /opt/bigdata/presto-server-0.236/bin/launcher stop
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


./presto --server 192.168.10.200:28081 --catalog hive  --schema taotao_cloud_log
