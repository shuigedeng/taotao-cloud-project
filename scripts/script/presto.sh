#!/bin/bash

function start_presto() {
    /opt/presto-server-0.236/bin/launcher start
    echo "presto started"
}

function stop_presto() {
    /opt/presto-server-0.236/bin/launcher stop
     echo "presto stoped"
}

case $1 in
"start")
    start_presto
    ;;
"stop")
    stop_presto
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop'
    ;;
esac
