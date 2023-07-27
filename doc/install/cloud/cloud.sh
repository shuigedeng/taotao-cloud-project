#!/bin/bash

function start_cloud() {
  # tcp -> 3306 端口 3306 是 MySQL 协议的默认端口，由 mysql 客户端、MySQL 连接器以及 mysqldump 和 mysqlpump 等实用程序使用
  # tcp -> 33060 端口 33060 是 MySQL 数据库扩展接口（MySQL X 协议）的默认端口
  /root/script/mysql.sh start
  
  # tcp -> 6379
  /root/script/redis.sh start
 
  # tcp -> 2181 对cline端提供服务
  # tcp -> 3888 选举leader使用
  # tcp -> 2888 集群内机器通讯使用（Leader监听此端口）
  /root/script/zookeeper.sh start
  
  # tcp -> 9092
  # JMX_PORT -> 9999
  /root/script/kafka.sh start
  
  # http -> 8848 http://192.168.10.220:8848/nacos  nacos/nacos
  # tcp -> 9848 客户端gRPC请求服务端端口，用于客户端向服务端发起连接和请求
  # tcp -> 9849 服务端gRPC请求服务端端口，用于服务间同步等
  # tcp -> 7848 nacos集群通信，进行选举，检测等
  /root/script/nacos.sh start
  
  # tcp -> 8091
  /root/script/seata.sh start
  
  # tcp/http -> 8849 http://192.168.10.220:8849 sentinel/sentinel
  /root/script/sentinel.sh start
  
  # tcp/http -> 9411 http://192.168.10.220:9411
  /root/script/zipkin.sh start
  
  # tcp -> 11800
  # http -> 18080 http://192.168.10.220:28080
  /root/script/skywalking.sh start

  # tcp/http -> 5601 http://192.168.10.220:8081
  /root/script/arthas.sh start
  
  # tcp/http -> 9090 http://192.168.10.220:9090 http://192.168.10.220:9090/metrics
  /root/script/prometheus.sh start
  
  # tcp/http -> 3000 http://192.168.10.220:3000 admin/admin
  /root/script/grafana.sh start

  # tcp/http -> 9200 http://192.168.10.220:9200
  su - elasticsearch -c "/home/elasticsearch/elasticsearch.sh start"

  # tcp/http -> 5601 http://192.168.10.220:5601
  /root/script/kibana.sh start

  # tcp/http -> 8089 http://192.168.10.220:8089
  /root/script/canal.sh start

  pm2 start /opt/cloud/yapi/vendors/server/app.js -n yapi --max-memory-restart 500M

  docker start rabbitmq

  docker start rmqserver
  sleep 10
  docker start rmqbroker
  docker start rmqconsole
  
  # tcp/http -> 9601 input port
  #/root/script/logstash.sh start

  # tcp/http -> 9100
  #/root/script/node_exporter.sh start
}

function stop_cloud() {
  docker stop rabbitmq

  docker stop rmqserver
  docker stop rmqbroker
  docker stop rmqconsole

  pm2 stop yapi

  #root/script/logstash.sh stop

  #/root/script/node_exporter.sh stop

  /root/script/canal.sh stop

  /root/script/kibana.sh stop
  
  su - elasticsearch -c "/home/elasticsearch/elasticsearch.sh stop"
  
  /root/script/grafana.sh stop
  
  /root/script/prometheus.sh stop
  
  /root/script/arthas.sh stop
  
  /root/script/skywalking.sh stop
  
  /root/script/zipkin.sh stop
  
  /root/script/sentinel.sh stop
  
  /root/script/seata.sh stop
  
  /root/script/nacos.sh stop
  
  /root/script/kafka.sh stop
  
  /root/script/zookeeper.sh stop
  
  /root/script/redis.sh stop
  
  /root/script/mysql.sh stop
}

case $1 in
"start")
    start_cloud
    ;;
"stop")
    stop_cloud
    ;;
"restart")
    stop_cloud
    sleep 15
    start_cloud
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
