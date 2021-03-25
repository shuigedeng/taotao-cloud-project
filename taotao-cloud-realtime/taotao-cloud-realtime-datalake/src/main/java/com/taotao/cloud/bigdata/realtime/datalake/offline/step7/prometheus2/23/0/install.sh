###########################################
wget https://github.com/prometheus/prometheus/releases/download/v2.23.0/prometheus-2.23.0.linux-amd64.tar.gz

tar -zxvf prometheus-2.23.0.linux-amd64.tar.gz -C /root/taotao-common/

mv prometheus-2.23.0.linux-amd64 prometheus2.23.0

# 使用本地路径存储数据
mkdir /root/taotao-common/prometheus2.23.0/data

mkdir -p /root/taotao-common/prometheus2.23.0/supervisor/logs

vim /root/taotao-common/prometheus2.23.0/prometheus.yml
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'prometheus'
    static_configs:
    - targets: ['localhost:9090']

  - job_name: 'taotao-cloud-weblog-collect'
    scrape_interval: 30s
    static_configs:
    - targets: ['host:9527']

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
  nohup /root/taotao-common/prometheus2.23.0/prometheus \
  --storage.tsdb.path="/root/taotao-common/prometheus2.23.0/data" \
  --log.level=debug \
  --web.enable-lifecycle \
  --web.enable-admin-api \
  --config.file="/root/taotao-common/prometheus2.23.0/prometheus.yml"
  >/root/taotao-common/prometheus2.23.0/prometheus.out 2>&1 &

     sleep 10
     echo "prometheus started"
}

function stop_prometheus() {
     ps -ef | grep prometheus|grep -v grep|awk '{print $2}' |xargs kill -9
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
