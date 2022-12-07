###########################################
# https://mirrors.huaweicloud.com/elasticsearch/

cd /opt/soft

wget https://mirrors.huaweicloud.com/elasticsearch/7.12.1/elasticsearch-7.12.1-linux-x86_64.tar.gz

tar -zxvf elasticsearch-7.12.1-linux-x86_64.tar.gz -C /opt/cloud

cd /opt/cloud/elasticsearch-7.12.1

# 修改配置文件
vim config/elasticsearch.yml
# 修改以下几项：
node.name: node-1 # 设置节点名
network.host: 0.0.0.0 # 允许外部 ip 访问
cluster.initial_master_nodes: ["node-1"] # 设置集群初始主节点

vim config/jvm.options
-Xms1g
-Xmx1g

#新建用户并赋权
adduser elasticsearch
passwd elasticsearch

chown -R elasticsearch elasticsearch-7.12.1

elasticsearch/jvm.options
-Xms [SIZE] g -Xmx [SIZE] g

su elasticsearch
cd ~
vim elasticsearch.sh
chmod +x elasticsearch.sh

# 错误处理
# 启动之后可能会报以下三个错误：
[1]: max file descriptors [4096] for elasticsearch process is too low, increase to at least [65535]
[2]: max number of threads [3795] for user [es] is too low, increase to at least [4096]
[3]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]

su root
 
[1] 和 [2] 的解决方法：
  # 修改 /etc/security/limits.conf 文件
  vim /etc/security/limits.conf
  # 添加以下四行
  soft nofile 65536
  hard nofile 131072
  soft nproc 2048
  hard nproc 4096

[3] 的解决方法：
  在/etc/sysctl.conf文件最后添加一行
  vm.max_map_count=262144
  执行/sbin/sysctl -p 立即生效

##################### elasticsearch.sh #############################
#!/bin/bash

function start_elasticsearch() {
  nohup /opt/cloud/elasticsearch-7.12.1/bin/elasticsearch \
  -d >/opt/cloud/elasticsearch-7.12.1/start.out 2>&1 &
  sleep 10
  echo "elasticsearch started"
}

function stop_elasticsearch() {
   sleep 5
   ps -ef | grep elasticsearch-7.12.1|grep -v grep|awk '{print $2}' |xargs kill -9

   sleep 10
   echo "elasticsearch stoped"
}

case $1 in
"start")
    start_elasticsearch
    ;;
"stop")
    stop_elasticsearch
    ;;
"restart")
    stop_elasticsearch
    sleep 10
    start_elasticsearch
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
