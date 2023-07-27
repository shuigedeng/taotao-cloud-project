################################################
wget https://mirrors.bfsu.edu.cn/apache/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz

tar -zxvf apache-hive-3.1.2-bin.tar.gz -C /bigdata

mv /bigdata/apache-hive-3.1.2-bin /root/taotao-bigdata/hive3.1.2

cd /root/taotao-bigdata/hive3.1.2/conf

vim hive-site.xml

## 重点
cp /opt/soft/mysql-connector-java-8.0.20.jar /root/taotao-bigdata/hive3.1.2/lib
rm -rf /root/taotao-bigdata/hive3.1.2/lib/guava*.jar
cp /root/taotao-bigdata/hadoop3.3.0/share/hadoop/common/lib/guava-27.0-jre.jar /root/taotao-bigdata/hive3.1.2/lib/

cp hive-env.sh.template hive-env.sh
vim hive-env.sh
# 最末尾添加
 export JAVA_HOME=/opt/common/jdk1.8.0_211
 HADOOP_HOME=/opt/bigdata/hadoop-3.3.0
 export HIVE_CONF_DIR=/opt/bigdata/apache-hive-3.1.2-bin/conf
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
mkdir /opt/bigdata/apache-hive-3.1.2-bin/logs
vim hive-log4j2.properties
# 最末尾添加
# hive.log.dir=/opt/bigdata/apache-hive-3.1.2-bin/logs

cp hive-exec-log4j2.properties.template hive-exec-log4j2.properties
# 最末尾添加
# hive.log.dir=/opt/bigdata/apache-hive-3.1.2-bin/logs

################################################

schematool -dbType mysql -initSchema

nohup hive --service metastore >/opt/bigdata/apache-hive-3.1.2-bin/logs/metastore.log  2>&1 &
nohup hive --service hiveserver2 >/opt/bigdata/apache-hive-3.1.2-bin/logs/hiveserver2.log  2>&1 &

hive --hiveconf hive.root.logger=DEBUG,console

beeline -i ~/.hiverc -u jdbc:hive2://192.168.10.220:10000 -n root


###########################################
#!/bin/bash

function start_hive() {
   nohup hive --service metastore >/opt/bigdata/apache-hive-3.1.2-bin/logs/metastore.log  2>&1 &
   sleep 10
   echo "hive metastore started"

   nohup hive --service hiveserver2 >/opt/bigdata/apache-hive-3.1.2-bin/logs/hiveserver2.log  2>&1 &
    sleep 10
   echo "hive hiveserver2 started"
}

function stop_hive() {
     ps -ef | grep metastore|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "hive metastore stoped"

     ps -ef | grep hiveserver2|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "hive hiveserver2 stoped"
}

case $1 in
"start")
    start_hive
    ;;
"stop")
    stop_hive
    ;;
"restart")
    stop_hive
    sleep 15
    start_hive
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac



