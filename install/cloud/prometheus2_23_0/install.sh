###########################################

cd /opt/soft

wget https://github.com/prometheus/prometheus/releases/download/v2.23.0/prometheus-2.23.0.linux-amd64.tar.gz

tar -zxvf prometheus-2.23.0.linux-amd64.tar.gz -C /opt/cloud

cd /opt/cloud/prometheus-2.23.0.linux-amd64

# 使用本地路径存储数据
mkdir data

/opt/cloud/prometheus-2.23.0.linux-amd64/prometheus \
--storage.tsdb.path="/opt/cloud/prometheus-2.23.0.linux-amd64/data" \
--log.level=debug \
--web.enable-lifecycle \
--web.enable-admin-api \
--config.file="/opt/cloud/prometheus-2.23.0.linux-amd64/prometheus.yml"

http://taotao-cloud:9090
http://taotao-cloud:9090/metrics

# 修改时间
rpm -ivh http://mirrors.wlnmp.com/centos/wlnmp-release-centos.noarch.rpm
yum install wntp -y
ntpdate time1.aliyun.com

########配置邮件告警
vim alertmanager.yml
global:
  resolve_timeout: 5m
  smtp_smarthost: 'smtp.163.com:25' # 邮箱smtp服务器代理
  smtp_from: 'xxxxxxx@163.com' # 发送邮箱名称
  smtp_auth_username: 'xxxxxx@163.com' # 邮箱名称
  smtp_auth_password: 'xxxxx' # 邮箱密码或授权码
  smtp_require_tls: false
route:
  group_by: ['alertname']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 1h
  receiver: 'mail'
receivers:
- name: 'mail'
  email_configs:
  - to: 'xxxxxxxx@qq.com'

##################### prometheus.sh #############################
#!/bin/bash

function start_prometheus() {
  nohup /opt/cloud/prometheus-2.23.0.linux-amd64/prometheus \
  --storage.tsdb.path="/opt/cloud/prometheus-2.23.0.linux-amd64/data" \
  --log.level=info \
  --web.enable-lifecycle \
  --web.enable-admin-api \
  --config.file="/opt/cloud/prometheus-2.23.0.linux-amd64/prometheus.yml" \
  >/opt/cloud/prometheus-2.23.0.linux-amd64/start.out 2>&1 &

  sleep 10
  echo "prometheus started"
}

function stop_prometheus() {
    ps -ef | grep prometheus-2.23.0.linux-amd64|grep -v grep|awk '{print $2}' |xargs kill -9
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
