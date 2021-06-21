###########################################
wget https://github.com/prometheus/prometheus/releases/download/v2.23.0/prometheus-2.23.0.linux-amd64.tar.gz

tar -zxvf prometheus-2.23.0.linux-amd64.tar.gz -C /root/taotao-common/

mv prometheus-2.23.0.linux-amd64 prometheus2.23.0

# 使用本地路径存储数据
mkdir /root/taotao-common/prometheus2.23.0/data


/root/taotao-common/prometheus2.23.0/prometheus \
--storage.tsdb.path="/root/taotao-common/prometheus2.23.0/data" \
--log.level=debug \
--web.enable-lifecycle \
--web.enable-admin-api \
--config.file="/root/taotao-common/prometheus2.23.0/prometheus.yml"

http://taotao-cloud:9090
http://taotao-cloud:9090/metrics

##################### prometheus.sh #############################
#!/bin/bash

function start_prometheus() {
  /root/taotao-common/prometheus2.23.0/prometheus \
  --storage.tsdb.path="/root/taotao-common/prometheus2.23.0/data" \
  --log.level=debug \
  --web.enable-lifecycle \
  --web.enable-admin-api \
  --config.file="/root/taotao-common/prometheus2.23.0/prometheus.yml"

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
