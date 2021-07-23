################################################

https://mirrors.bfsu.edu.cn/apache/hbase/2.4.4/hbase-2.4.4-bin.tar.gz

tar -zxvf hbase-2.3.3-bin.tar.gz -C /root/taotao-bigdata

mv hbase-2.3.3/ hbase2.3.3/
cd conf

vim hbase-env.sh
# export JAVA_HOME=/root/taotao-common/jdk1.8.0_211
# export HBASE_MANAGES_ZK=false 使用外部zookeeper，所以改成false
# export HBASE_PID_DIR=${HBASE_HOME}/pids

vim regionservers
taotao-cloud

vim log4j.properties
hbase.log.dir=/root/taotao-bigdata/hbase2.3.3/logs

################################################
#!/bin/bash

function start_hbase() {
    /root/taotao-bigdata/hbase2.3.3/bin/start-hbase.sh
    echo "hbase started"
}

function stop_hbase() {
     /root/taotao-bigdata/hbase2.3.3/bin/stop-hbase.sh
     echo "hbase stoped"
}

case $1 in
"start")
    start_hbase
    ;;
"stop")
    stop_hbase
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop'
    ;;
esac
