###########################################
# https://mirrors.huaweicloud.com/kibana

cd /opt/soft

wget https://mirrors.huaweicloud.com/kibana/7.8.0/kibana-7.8.0-linux-x86_64.tar.gz

tar -zxvf kibana-7.8.0-linux-x86_64.tar.gz -C /opt/cloud

cd /opt/cloud/kibana-7.8.0-linux-x86_64

# 修改配置文件
vim config/kibana.yml
# 修改以下几项：
# 服务端口
server.port: 5601
# 服务器ip  本机
server.host: "0.0.0.0"
# Elasticsearch 服务地址
elasticsearch.hosts: ["http://127.0.0.1:9200"]
# 设置语言为中文
i18n.locale: "zh-CN"

# bin/kibana
NODE_OPTIONS="$NODE_OPTIONS --max-old-space-size=400"

##################### c #############################
#!/bin/bash

function start_kibana() {
  nohup /opt/cloud/kibana-7.8.0-linux-x86_64/bin/kibana \
   --allow-root \
  >/opt/cloud/kibana-7.8.0-linux-x86_64/start.out 2>&1 &
  sleep 10

  echo "kibana started"
}

function stop_kibana() {
   ps -ef | grep kibana-7.8.0-linux-x86_64|grep -v grep|awk '{print $2}' |xargs kill -9

   sleep 10
   echo "kibana stoped"
}

case $1 in
"start")
    start_kibana
    ;;
"stop")
    stop_kibana
    ;;
"restart")
    stop_kibana
    sleep 10
    start_kibana
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
