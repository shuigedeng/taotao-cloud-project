#/bin/bash

nohup ${FLUME_HOME}/bin/flume-ng agent -n taotao-cloud-article-log -c $FLUME_HOME/conf \
-f ${FLUME_HOME}/conf/taotao-cloud-article-log -Dflume.root.logger=INFO,console \
-Dflume.monitoring.type=http -Dflume.monitoring.port=31001
