################################################
wget https://mirror.bit.edu.cn/apache/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz

tar -zxvf apache-hive-3.1.2-bin.tar.gz -C /bigdata

mv /bigdata/apache-hive-3.1.2-bin /root/taotao-bigdata/hive3.1.2

cd /root/taotao-bigdata/hive3.1.2/conf

vim hive-site.xml

## 重点
cp ~/soft/mysql-connector-java-8.0.17.jar /root/taotao-bigdata/hive3.1.2/lib
rm -rf /root/taotao-bigdata/hive3.1.2/lib/guava*.jar
cp /root/taotao-bigdata/hadoop3.3.0/share/hadoop/common/lib/guava-27.0-jre.jar /root/taotao-bigdata/hive3.1.2/lib/


cp hive-env.sh.template hive-env.sh
vim hive-env.sh
# 最末尾添加
# export JAVA_HOME=/root/taotao-common/jdk1.8.0_211
# HADOOP_HOME=/root/taotao-bigdata/hadoop3.3.0
# export HIVE_CONF_DIR=/root/taotao-bigdata/hive3.1.2/conf
#
# tez配置可以不添加
# export TEZ_HOME=/bigdata/tez
# export TEZ_JARS=""
# for jar in `ls $TEZ_HOME |grep jar`; do
# export TEZ_JARS=$TEZ_JARS:$TEZ_HOME/$jar
# done
# for jar in `ls $TEZ_HOME/lib`; do
# export TEZ_JARS=$TEZ_JARS:$TEZ_HOME/lib/$jar
# done
# export HIVE_AUX_JARS_PATH=/bigdata/hadoop-3.1.4/share/hadoop/common/hadoop-lzo-0.4.21-SNAPSHOT.jar$TEZ_JARS

cp hive-log4j2.properties.template hive-log4j2.properties
mkdir /root/taotao-bigdata/hive3.1.2/logs
vim hive-log4j2.properties
# 最末尾添加
# hive.log.dir=/root/taotao-bigdata/hive3.1.2/logs

cp hive-exec-log4j2.properties.template hive-exec-log4j2.properties
# 最末尾添加
# hive.log.dir=/root/taotao-bigdata/hive3.1.2/logs

################################################

schematool -dbType mysql -initSchema

nohup hive --service metastore >/root/taotao-bigdata/hive3.1.2/logs/metastore.log  2>&1 &
nohup hive --service hiveserver2 >/root/taotao-bigdata/hive3.1.2/logs/hiveserver2.log  2>&1 &

hive --hiveconf hive.root.logger=DEBUG,console

beeline -i ~/.hiverc -u jdbc:hive2://127.0.0.1:10000 -n root

###########################################
#!/bin/bash

HIVE_LOG_DIR=$HIVE_HOME/logs

function check_process()
{
    pid=$(ps -ef 2>/dev/null | grep -v grep | grep -i $1 | awk '{print $2}')
    ppid=$(netstat -nltp 2>/dev/null | grep $2 | awk '{print $7}' | cut -d '/' -f 1)
    echo $pid
    [[ "$pid" =~ "$ppid" ]] && [ "$ppid" ] && return 0 || return 1
}

function hive_start()
{
    metapid=$(check_process HiveMetastore 9083)
    cmd="nohup hive --service metastore >$HIVE_LOG_DIR/metastore.log 2>&1 &"
    cmd=$cmd" sleep 4; hdfs dfsadmin -safemode wait >/dev/null 2>&1"
    [ -z "$metapid" ] && eval $cmd || echo "Metastroe started"
    server2pid=$(check_process HiveServer2 10000)
    cmd="nohup hive --service hiveserver2 >$HIVE_LOG_DIR/hiveServer2.log 2>&1 &"
    [ -z "$server2pid" ] && eval $cmd || echo "HiveServer2 started"
}

function hive_stop()
{
    metapid=$(check_process HiveMetastore 9083)
    [ "$metapid" ] && kill $metapid || echo "Metastore stoped"
    server2pid=$(check_process HiveServer2 10000)
    [ "$server2pid" ] && kill $server2pid || echo "HiveServer2 stoped"
}

case $1 in
"start")
    hive_start
    ;;
"stop")
    hive_stop
    ;;
"restart")
    hive_stop
    sleep 2
    hive_start
    ;;
"status")
    check_process HiveMetastore 9083 >/dev/null && echo "Metastore 正常" || echo "Metasto failed"
    check_process HiveServer2 10000 >/dev/null && echo "HiveServer2 正常" || echo "HiveServe failed"
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart|status'
    ;;
esac



