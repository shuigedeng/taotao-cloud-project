###########################################
wget https://dl.grafana.com/oss/release/grafana-7.3.7.linux-amd64.tar.gz

tar -zxvf grafana-7.3.7.linux-amd64.tar.gz -C /root/taotao-common

mv grafana-7.3.7.linux-amd64 grafana7.3.7

cp /root/taotao-common/grafana7.3.7/graf/conf/sample.ini /root/taotao-common/grafana7.3.7/graf/conf/grafana.ini

# 启动
/root/taotao-common/grafana7.3.7/bin/grafana-server -config /root/taotao-common/grafana7.3.7/graf/conf/grafana.ini

http://taotao-cloud:3000  admin/admin(grafana)

##################### grafana.sh #############################
#!/bin/bash

function start_grafana() {
  nohup
   /root/taotao-common/grafana7.3.7/bin/grafana-server -config /root/taotao-common/grafana7.3.7/graf/conf/grafana.ini
  >/root/taotao-common/grafana7.3.7/grafana.out 2>&1 &

     sleep 10
     echo "grafana started"
}

function stop_grafana() {
     ps -ef | grep grafana|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "grafana stoped"
}

case $1 in
"start")
    start_grafana
    ;;
"stop")
    stop_grafana
    ;;
"restart")
    stop_grafana
    sleep 10
    start_grafana
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
