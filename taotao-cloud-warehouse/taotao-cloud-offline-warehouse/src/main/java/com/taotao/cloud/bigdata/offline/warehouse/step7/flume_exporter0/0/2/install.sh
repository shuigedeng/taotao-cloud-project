###########################################
git clone https://github.com/woozhijun/flume_exporter.git

cd flume_exporter

dnf install go

make build

cd /root/taotao-bigdata/flume_exporter

mv flume_exporter flume_exporter0.0.2

vim  config.yml
# agents:
# - name: "flume-agents"
#   enabled: true
# # multiple urls can be separated by ,
#   urls: ["http://localhost:31001/metrics","http://localhost:31002/metrics]


##################### flume_exporter.sh #############################
#!/bin/bash

function start_flume_exporter() {
  nohup /root/taotao-bigdata/flume_exporter0.0.2/flume_exporter \
 --metric-file=/root/taotao-bigdata/flume_exporter0.0.2/metrics.yml \
 --config-file=/root/taotao-bigdata/flume_exporter0.0.2/config.yml
   >/root/taotao-bigdata/flume_exporter0.0.2/logs/flume_exporter.out 2>&1 &

     sleep 10
     echo "flume_exporter started"
}

function stop_flume_exporter() {
     ps -ef | grep flume_exporter|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "flume_exporter stoped"
}

case $1 in
"start")
    start_flume_exporter
    ;;
"stop")
    stop_flume_exporter
    ;;
"restart")
    stop_flume_exporter
    sleep 10
    start_flume_exporter
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
