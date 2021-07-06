###########################################
# https://mirrors.huaweicloud.com/kibana

cd /root/taotao-cloud/kibana7.8.0

wget https://mirrors.huaweicloud.com/kibana/7.8.0/kibana-7.8.0-linux-x86_64.tar.gz

tar -zxvf kibana-7.8.0-linux-x86_64.tar.gz

mv kibana-7.8.0-linux-x86_64 kibana7.8.0

# 修改配置文件
vim ./config/kibana.yml
# 修改以下几项：
# 服务端口
server.port: 5601
# 服务器ip  本机
server.host: "0.0.0.0"
# Elasticsearch 服务地址
elasticsearch.hosts: ["http://192.168.1.10:9200"]
# 设置语言为中文
i18n.locale: "zh-CN"

##################### kibana.sh #############################
#!/bin/bash

function start_kibana() {
  nohup sh /opt/taotao-cloud/kibana7.8.0/bin/kibana >/opt/taotao-cloud/kibana7.8.0/start.out 2>&1 &
  sleep 10

  echo "kibana started"
}

function stop_kibana() {
   ps -ef | grep kibana|grep -v grep|awk '{print $2}' |xargs kill -9

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
