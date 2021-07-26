#!/bin/bash

function start_taotao_cloud() {
  # tcp -> 3306 端口 3306 是 MySQL 协议的默认端口，由 mysql 客户端、MySQL 连接器以及 mysqldump 和 mysqlpump 等实用程序使用
  # tcp -> 33060 端口 33060 是 MySQL 数据库扩展接口（MySQL X 协议）的默认端口
  /root/script/cloud/mysql.sh start
  
  # tcp -> 6379
  /root/script/cloud/redis.sh start
 
  # tcp -> 2181 对cline端提供服务
  # tcp -> 3888 选举leader使用
  # tcp -> 2888 集群内机器通讯使用（Leader监听此端口）
  /root/script/cloud/zookeeper.sh start
  
  # tcp -> 9092
  # JMX_PORT -> 9999
  /root/script/cloud/kafka.sh start
  
  # http -> 8848 http://172.16.3.240:8848/nacos  nacos/nacos
  # tcp -> 9848 客户端gRPC请求服务端端口，用于客户端向服务端发起连接和请求
  # tcp -> 9849 服务端gRPC请求服务端端口，用于服务间同步等
  # tcp -> 7848 nacos集群通信，进行选举，检测等
  /root/script/cloud/nacos.sh start
  
  # tcp -> 8091
  /root/script/cloud/seata.sh start
  
  # tcp/http -> 8849 http://172.16.3.240:8849 sentinel/sentinel
  /root/script/cloud/sentinel.sh start
  
  # tcp/http -> 9411 http://172.16.3.240:9411
  /root/script/cloud/zipkin.sh start
  
  # tcp -> 11800
  # http -> 18080 http://172.16.3.240:18080
  /root/script/cloud/skywalking.sh start
  
  # tcp/http -> 9090 http://172.16.3.240:9090 http://172.16.3.240:9090/metrics
  /root/script/cloud/prometheus.sh start
  
  # tcp/http -> 3000 http://172.16.3.240:3000 admin/admin
  /root/script/cloud/grafana.sh start

  # tcp/http -> 9200 http://172.16.3.240:9200
  su - elasticsearch -c "/home/elasticsearch/elasticsearch.sh start"

  # tcp/http -> 5601 http://172.16.3.240:5601
  /root/script/cloud/kibana.sh start

  # tcp/http -> 5601 http://172.16.3.240:8081
  /root/script/cloud/arthas.sh start

  # tcp/http -> 5601 http://172.16.3.240:8089
  /root/script/cloud/canal.sh start
  
  # tcp/http -> 9601 input port
  #/root/script/cloud/logstash.sh start

  # tcp/http -> 9100
  #/root/script/cloud/node_exporter.sh start
}

function stop_taotao_cloud() {
  /root/script/cloud/canal.sh stop

  /root/script/cloud/arthas.sh stop

  /root/script/cloud/kibana.sh stop
  
  su - elasticsearch -c "/home/elasticsearch/elasticsearch.sh stop"
  
  /root/script/cloud/grafana.sh stop
  
  /root/script/cloud/prometheus.sh stop
  
  /root/script/cloud/skywalking.sh stop
  
  /root/script/cloud/zipkin.sh stop
  
  /root/script/cloud/sentinel.sh stop
  
  /root/script/cloud/seata.sh stop
  
  /root/script/cloud/nacos.sh stop
  
  /root/script/cloud/kafka.sh stop
  
  /root/script/cloud/zookeeper.sh stop
  
  /root/script/cloud/redis.sh stop
  
  /root/script/cloud/mysql.sh stop
  
  #root/script/cloud/logstash.sh stop
  #/root/script/cloud/node_exporter.sh stop
}

case $1 in
"start")
    start_taotao_cloud
    ;;
"stop")
    stop_taotao_cloud
    ;;
"restart")
    stop_taotao_cloud
    sleep 15
    start_taotao_cloud
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
