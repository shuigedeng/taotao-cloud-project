#!/bin/bash

function start_canal() {
  nohup /opt/cloud/canal/admin/bin/startup.sh \
  >/opt/cloud/canal/admin/canal-admin.out 2>&1 &
  sleep 10
  echo "canal admin started"

  nohup /opt/cloud/canal/deployer/bin/startup.sh \
  >/opt/cloud/canal/deployer/canal-deployer.out 2>&1 &
  sleep 10

  echo "canal deployer started"
}

function stop_canal() {
   /opt/cloud/canal/admin/bin/stop.sh
   sleep 10
   echo "canal admin stoped"

   /opt/cloud/canal/deployer/bin/stop.sh
   sleep 10
   echo "canal deployer stoped"
}

case $1 in
"start")
    start_canal
    ;;
"stop")
    stop_canal
    ;;
"restart")
    stop_canal
    sleep 10
    start_canal
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac

