#!/bin/bash

wget http://www.apache.org/dyn/closer.lua/flume/1.9.0/apache-flume-1.9.0-bin.tar.gz

tar -zxvf apache-flume-1.9.0-bin.tar.gz /root/taotao-bigdata/

mv /root/taotao-bigdata/apache-flume-1.9.0-bin /root/taotao-bigdata/flume1.9.0

vim .bashrc
export FLUME_HOME="/root/taotao-bigdata/flume1.9.0"
export PATH=$PATH:$FLUME_HOME/bin


cd /root/taotao-bigdata/flume1.9.0/conf
cp flume-env.sh.template flume-env.sh
vim flume-env.sh
# export JAVA_HOME=/root/taotao-common/jdk1.8.0_211

cp $HADOOP_HOME/share/hadoop/client/*.jar $FLUME_HOME/lib
cp $HADOOP_HOME/share/hadoop/common/*.jar $FLUME_HOME/lib
cp $HADOOP_HOME/share/hadoop/hdfs/*.jar $FLUME_HOME/lib
cp $HADOOP_HOME/share/hadoop/common/lib/*.jar $FLUME_HOME/lib
cp $HADOOP_HOME/share/hadoop/hdfs/lib/*.jar $FLUME_HOME/lib

nohup /root/taotao-bigdata/flume1.9.0/bin/flume-ng agent \
-n taotao-cloud-offline-access-log \
 -c /root/taotao-bigdata/flume1.9.0/conf \
-f ${FLUME_HOME}/conf/offline-access-log.conf
-Dflume.root.logger=INFO,console \
-Dflume.monitoring.type=http \
-Dflume.monitoring.port=31001 \
>/root/taotao-bigdata/flume1.9.0/logs/taotao-cloud-offline-access-log.out 2>&1 &

nohup /root/taotao-bigdata/flume1.9.0/bin/flume-ng agent  \
-n taotao-cloud-realtime-access-log  \
-c /root/taotao-bigdata/flume1.9.0/conf \
-f /root/taotao-bigdata/flume1.9.0/conf/realtime-access-log.conf  \
-Dflume.root.logger=INFO,console \
-Dflume.monitoring.type=http
-Dflume.monitoring.port=31002 \
>/root/taotao-bigdata/flume1.9.0/logs/taotao-cloud-realtime-access-log.out 2>&1 &
