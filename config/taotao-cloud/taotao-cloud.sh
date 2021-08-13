#!/bin/bash

function start_taotao_cloud() {
  /opt/taotao-cloud/script/taotao-cloud-admin.sh start
  /opt/taotao-cloud/script/taotao-cloud-xxl-job-admin.sh start

  /opt/taotao-cloud/script/taotao-cloud-oauth2-server.sh start
  /opt/taotao-cloud/script/taotao-cloud-gateway.sh start
  /opt/taotao-cloud/script/taotao-cloud-uc.sh start
  /opt/taotao-cloud/script/taotao-cloud-order.sh start
  /opt/taotao-cloud/script/taotao-cloud-product.sh start
}

function stop_taotao_cloud() {
  /opt/taotao-cloud/script/taotao-cloud-admin.sh stop
  /opt/taotao-cloud/script/taotao-cloud-oauth2-server.sh stop
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
