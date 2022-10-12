#!/bin/bash

function start_spark() {
    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/start-all.sh
    echo "spark started"
    sleep 10

    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/start-history-server.sh
    echo "spark history server started"
    sleep 10

    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/start-thriftserver.sh
    echo "spark thriftserver started"
    sleep 10
}

function stop_spark() {
    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/stop-all.sh
    echo "spark stoped"
    sleep 10

    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/stop-history-server.sh
    echo "spark history server stoped"
    sleep 10

    /opt/bigdata/spark-3.0.0-bin-hadoop3.2/sbin/stop-thriftserver.sh
    echo "spark thriftserver stoped"
    sleep 10
}

case $1 in
"start")
    start_spark
    ;;
"stop")
    stop_spark
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop'
    ;;
esac

