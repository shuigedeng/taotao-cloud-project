###########################################
# https://mirrors.huaweicloud.com/logstash

cd /root/taotao-cloud/logstash7.8.0

wget wget https://mirrors.huaweicloud.com/logstash/7.8.0/logstash-7.8.0.tar.gz

tar -zxvf logstash-7.8.0.tar.gz

mv logstash-7.8.0-linux-x86_64 logstash7.8.0

# 修改配置文件
cp config/logstash-sample.conf config/logstash-es.conf

vim config/logstash-es.conf
vim config/logstash-mysql.conf

# 安装插件
vim Gemfile
# 将source这一行改成如下所示：
source "https://gems.ruby-china.com/"

config/jvm.options
-Xms256m
-Xmx256m

./bin/logstash-plugin install logstash-codec-json_lines
./bin/logstash-plugin install logstash-input-jdbc
./bin/logstash-plugin install logstash-output-elasticsearch

##################### logstash.sh #############################
#!/bin/bash

function start_logstash() {
  nohup /opt/taotao-cloud/logstash7.8.0/bin/logstash  \
  -f /opt/taotao-cloud/logstash7.8.0/config/logstash-es.conf \
  >/opt/taotao-cloud/logstash7.8.0/logstash-es.out 2>&1 &
  sleep 10

  echo "logstash started"
}

function stop_logstash() {
   ps -ef | grep logstash|grep -v grep|awk '{print $2}' |xargs kill -9

   sleep 10
   echo "logstash stoped"
}

case $1 in
"start")
    start_logstash
    ;;
"stop")
    stop_logstash
    ;;
"restart")
    stop_logstash
    sleep 10
    start_logstash
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
