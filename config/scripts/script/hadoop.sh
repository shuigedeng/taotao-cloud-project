#!/bin/bash

function start_hadoop() {
    start-dfs.sh
    echo "dfs started"
    sleep 10

    start-yarn.sh
    echo "yarn started"
    sleep 10

#    start-history-server.sh

    hdfs --daemon start httpfs
    echo "httpfs started"
    sleep 10

    mapred --daemon start historyserver
    echo "historyserver started"
    sleep 10
}

function stop_hadoop() {
    mapred --daemon stop historyserver
    echo "historyserver stoped"
    sleep 10

    hdfs --daemon stop httpfs
    echo "httpfs stoped"
    sleep 10

    stop-yarn.sh
    echo "yarn stoped"
    sleep 10

    stop-dfs.sh
    echo "dfs stoped"
    sleep 10

#    stop-history-server.sh
}

case $1 in
"start")
    start_hadoop
    ;;
"stop")
    stop_hadoop
    ;;
"restart")
    stop_hadoop
    sleep 15
    start_hadoop
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac

