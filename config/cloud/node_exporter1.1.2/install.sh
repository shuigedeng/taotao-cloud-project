###########################################
# https://node_exporter.com/node_exporter/download

cd /root/taotao-cloud/

wget https://github.com/prometheus/node_exporter/releases/download/v1.1.2/node_exporter-1.1.2.linux-amd64.tar.gz

tar -zxvf node_exporter-1.1.2.linux-amd64.tar.gz

mv node_exporter-1.1.2.linux-amd64 node_exporter1.1.2

##################### node_exporter.sh #############################
#!/bin/bash

function start_node_exporter() {
  nohup /opt/taotao-cloud/node_exporter1.1.2/node_exporter \
   >/opt/taotao-cloud/node_exporter1.1.2/node_exporter.out 2>&1 &

  sleep 10
  echo "node_exporter started"
}

function stop_node_exporter() {
    ps -ef | grep node_exporter|grep -v grep|awk '{print $2}' |xargs kill -9

    sleep 10
    echo "node_exporter stoped"
}

case $1 in
"start")
    start_node_exporter
    ;;
"stop")
    stop_node_exporter
    ;;
"restart")
    stop_node_exporter
    sleep 10
    start_node_exporter
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
