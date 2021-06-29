#!/bin/bash

function start_mysql() {
     systemctl start mysqld
     sleep 10
     echo "mysqld started"
}

function stop_mysql() {
     systemctl stop mysqld
     sleep 10
     echo "mysqld stoped"
}

case $1 in
"start")
    start_mysql
    ;;
"stop")
    stop_mysql
    ;;
"restart")
    stop_mysql
    sleep 10
    start_mysql
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
