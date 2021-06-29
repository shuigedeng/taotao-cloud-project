###########################################
# https://grafana.com/grafana/download

cd /root/taotao-cloud/grafana8.0.3

wget https://dl.grafana.com/oss/release/grafana-8.0.3.linux-amd64.tar.gz

tar -zxvf grafana-8.0.3.linux-amd64.tar.gz

mv grafana-8.0.3.linux-amd64 grafana8.0.3






# 使用本地路径存储数据
mkdir prometheus2.23.0/data

/root/taotao-cloud/prometheus2.23.0/prometheus \
--storage.tsdb.path="/root/taotao-cloud/prometheus2.23.0/data" \
--log.level=debug \
--web.enable-lifecycle \
--web.enable-admin-api \
--config.file="/root/taotao-cloud/prometheus2.23.0/prometheus.yml"

http://taotao-cloud:9090
http://taotao-cloud:9090/metrics

##################### prometheus.sh #############################
#!/bin/bash

function start_prometheus() {
  /root/taotao-cloud/prometheus2.23.0/prometheus \
  --storage.tsdb.path="/root/taotao-cloud/prometheus2.23.0/data" \
  --log.level=debug \
  --web.enable-lifecycle \
  --web.enable-admin-api \
  --config.file="/root/taotao-cloud/prometheus2.23.0/prometheus.yml"

  sleep 10
  echo "prometheus started"
}

function stop_prometheus() {
	  /root/taotao-cloud/prometheus6.0.1/bin/prometheus-cli -h 192.168.1.5 -a taotao-cloud shutdown
    sleep 10
    echo "prometheus stoped"
}

case $1 in
"start")
    start_prometheus
    ;;
"stop")
    stop_prometheus
    ;;
"restart")
    stop_prometheus
    sleep 10
    start_prometheus
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
