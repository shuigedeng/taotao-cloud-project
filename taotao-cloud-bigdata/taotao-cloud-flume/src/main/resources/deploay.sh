#!/bin/bash

FLUME_HOME=/opt/apache-flume-1.9.0-bin

cp $HADOOP_HOME/share/hadoop/client/*.jar $FLUME_HOME/lib
cp $HADOOP_HOME/share/hadoop/common/*.jar $FLUME_HOME/lib
cp $HADOOP_HOME/share/hadoop/hdfs/*.jar $FLUME_HOME/lib
cp $HADOOP_HOME/share/hadoop/common/lib/*.jar $FLUME_HOME/lib
cp $HADOOP_HOME/share/hadoop/hdfs/lib/*.jar $FLUME_HOME/lib

nohup ${FLUME_HOME}/bin/flume-ng agent -n taotao-cloud-access-log -c $FLUME_HOME/conf \
-f ${FLUME_HOME}/conf/taotao-cloud-access-log-dir-agent.conf -Dflume.root.logger=INFO,console &

nohup ${FLUME_HOME}/bin/flume-ng agent -n taotao-cloud-access-log -c $FLUME_HOME/conf \
-f ${FLUME_HOME}/conf/taotao-cloud-access-log-dir-agent.conf -Dflume.root.logger=INFO,console \
-Dflume.monitoring.type=http -Dflume.monitoring.port=31001

nohup ${FLUME_HOME}/bin/flume-ng agent -n taotao-cloud-access-log -c $FLUME_HOME/conf \
-f ${FLUME_HOME}/conf/taotao-cloud-access-log-kafka-agent.conf -Dflume.root.logger=INFO,console &

nohup ${FLUME_HOME}/bin/flume-ng agent -n taotao-cloud-access-log -c $FLUME_HOME/conf \
-f ${FLUME_HOME}/conf/taotao-cloud-access-log-kafka-agent.conf -Dflume.root.logger=INFO,console \
-Dflume.monitoring.type=http -Dflume.monitoring.port=31001 &


nohup ${FLUME_HOME}/bin/flume-ng agent -n taotao-cloud-log -c $FLUME_HOME/conf \
-f ${FLUME_HOME}/conf/taotao-cloud-log-kafka-agent.conf -Dflume.root.logger=INFO,console &

nohup ${FLUME_HOME}/bin/flume-ng agent -n taotao-cloud-log -c $FLUME_HOME/conf \
-f ${FLUME_HOME}/conf/taotao-cloud-log-kafka-agent.conf -Dflume.root.logger=INFO,console \
-Dflume.monitoring.type=http -Dflume.monitoring.port=31001
