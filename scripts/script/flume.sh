#!/bin/bash

function start_flume() {
  nohup ${FLUME_HOME}/bin/flume-ng agent -n taotao-cloud-log -c $FLUME_HOME/conf \
-f ${FLUME_HOME}/conf/taotao-cloud-log-kafka-agent.conf -Dflume.root.logger=INFO &

    echo "flume started"
}

function stop_flume() {
   ps -ef | grep flume | grep -v grep |awk '{print $2}' | xargs kill
}

case $1 in
"start")
    start_flume
    ;;
"stop")
    stop_flume
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop'
    ;;
esac

