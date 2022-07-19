#/bin/bash

nohup /root/taotao-bigdata/flume1.9.0/bin/flume-ng agent \
-n taotao-cloud-offline-access-log \
-c /root/taotao-bigdata/flume1.9.0/conf \
-f /root/taotao-bigdata/flume1.9.0/conf/offline-access-log.conf \
-Dflume.root.logger=INFO,console \
-Dflume.monitoring.type=http \
-Dflume.monitoring.port=31001 \
>/root/taotao-bigdata/flume1.9.0/logs/taotao-cloud-offline-access-log.out 2>&1 &

nohup /root/taotao-bigdata/flume1.9.0/bin/flume-ng agent  \
-n taotao-cloud-realtime-access-log  \
-c /root/taotao-bigdata/flume1.9.0/conf \
-f /root/taotao-bigdata/flume1.9.0/conf/realtime-access-log.conf \
-Dflume.root.logger=INFO,console \
-Dflume.monitoring.type=http \
-Dflume.monitoring.port=31002 \
>/root/taotao-bigdata/flume1.9.0/logs/taotao-cloud-realtime-access-log.out 2>&1 &
