#!/bin/bash

function start_taotao_cloud() {
  /opt/taotao-cloud/script/taotao-cloud-monitor.sh start
  /opt/taotao-cloud/script/taotao-cloud-xxljob.sh start

  /opt/taotao-cloud/script/taotao-cloud-auth.sh start
  /opt/taotao-cloud/script/taotao-cloud-gateway.sh start
  /opt/taotao-cloud/script/taotao-cloud-sys.sh start
  /opt/taotao-cloud/script/taotao-cloud-order.sh start
  /opt/taotao-cloud/script/taotao-cloud-goods.sh start
}

function stop_taotao_cloud() {
  /opt/taotao-cloud/script/taotao-cloud-monitor.sh stop
  /opt/taotao-cloud/script/taotao-cloud-auth.sh stop
  /opt/taotao-cloud/script/taotao-cloud-xxl-job.sh stop
}

case $1 in
"start")
    start_taotao_cloud
    ;;
"stop")
    stop_taotao_cloud
    ;;
"restart")
    stop_taotao_cloud
    sleep 15
    start_taotao_cloud
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
