#!/bin/bash

function start_hadoop() {
    start-dfs.sh
    echo "dfs started"
    sleep 20

    start-yarn.sh
    echo "yarn started"
}

function stop_hadoop() {
    stop-yarn.sh
    echo "dfs stoped"
    sleep 10

    stop-dfs.sh
    echo "dfs stoped"
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
