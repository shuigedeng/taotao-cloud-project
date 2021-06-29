#!/bin/bash

function start_azkaban() {
	/root/taotao-bigdata/azkaban3.90.0/bin/start-solo.sh
	sleep 10
  echo "azkaban started"
}

function stop_azkaban() {
	/root/taotao-bigdata/azkaban3.90.0/bin/shutdown-solo.sh
  echo "azkaban stoped"
}

case $1 in
"start")
    start_azkaban
    ;;
"stop")
    stop_azkaban
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop'
    ;;
esac
