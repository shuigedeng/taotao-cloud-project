#!/bin/bash

function start_taotao_cloud() {
  # tcp -> 6379
  /root/script/redis.sh start
  # tcp -> 3306 端口 3306 是 MySQL 协议的默认端口，由 mysql 客户端、MySQL 连接器以及 mysqldump 和 mysqlpump 等实用程序使用
  # tcp -> 33060 端口 33060 是 MySQL 数据库扩展接口（MySQL X 协议）的默认端口
  /root/script/mysql.sh start
  # tcp -> 2181 对cline端提供服务
  # tcp -> 3888 选举leader使用
  # tcp -> 2888 集群内机器通讯使用（Leader监听此端口）
  /root/script/zookeeper.sh start
  # tcp -> 9092
  # JMX_PORT -> 9999
  /root/script/kafka.sh start
  # http -> 8848 http://192.168.1.10:8848/nacos  nacos/nacos
  # tcp -> 9848 客户端gRPC请求服务端端口，用于客户端向服务端发起连接和请求
  # tcp -> 9849 服务端gRPC请求服务端端口，用于服务间同步等
  # tcp -> 7848 nacos集群通信，进行选举，检测等
  /root/script/nacos.sh start
  # tcp -> 8091
  /root/script/seata.sh start
  # tcp/http -> 8719 http://192.168.1.10:8719
  /root/script/sentinel.sh start
  # tcp/http -> 9411 http://192.168.1.10:9411
  /root/script/zipkin.sh start
  # tcp -> 11800
  # http -> 18080 http://192.168.1.10:18080
  /root/script/skywalking.sh start
  # tcp/http -> 9090 http://192.168.1.10:9090 http://192.168.1.10:9090/metrics
  /root/script/prometheus.sh start
  # tcp/http -> 3000 http://192.168.1.10:3000 admin/admin
  /root/script/grafana.sh start

  # tcp/http -> 9200 http://192.168.1.10:9200
  su - elasticsearch -c "/home/elasticsearch/elasticsearch.sh start"

  # tcp/http -> 5601 http://192.168.1.10:5601
  /root/script/kibana.sh start
  # tcp/http -> 9601 input port
  /root/script/logstash.sh start
}

function stop_taotao_cloud() {
	/root/script/redis.sh stop
	/root/script/mysql.sh stop
	/root/script/zookeeper.sh stop
	/root/script/kafka.sh stop
	/root/script/nacos.sh stop
	/root/script/seata.sh stop
	/root/script/sentinel.sh stop
	/root/script/zipkin.sh stop
	/root/script/skywalking.sh stop
	/root/script/prometheus.sh stop
  /root/script/grafana.sh stop

	su - elasticsearch -c "/home/elasticsearch/elasticsearch.sh stop"

  /root/script/kibana.sh stop
  /root/script/logstash.sh stop
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
