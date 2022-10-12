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

