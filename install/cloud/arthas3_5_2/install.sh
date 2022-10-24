wget https://github.com/alibaba/arthas/releases/download/arthas-all-3.6.6/arthas-tunnel-server-3.6.6-fatjar.jar

http://127.0.0.1:8081/actuator/arthas 登陆用户名是arthas
密码在arthas tunnel server的日志里可以找到，比如：

 <dependency>
        <groupId>com.taobao.arthas</groupId>
        <artifactId>arthas-spring-boot-starter</artifactId>
        <version>3.4.4</version>
    </dependency>


arthas:
  agent-id: URJZ5L48RPBR2ALI5K4V  #需手工指定agent-id
  tunnel-server: ws://127.0.0.1:7777/ws
  
##################### arthas.sh #############################
#!/bin/bash

function start_arthas() {
   nohup java -jar \
    -Dserver.port=7777 \
    /opt/cloud/arthas/arthas-tunnel-server-3.5.2-fatjar.jar \
    >/opt/cloud/arthas/start.out 2>&1 &

  sleep 10
  echo "arthas started"
}

function stop_arthas() {
  ps -ef | grep arthas|grep -v grep|awk '{print $2}' |xargs kill -9
  sleep 10
  echo "arthas stoped"
}

case $1 in
"start")
    start_arthas
    ;;
"stop")
    stop_arthas
    ;;
"restart")
    stop_arthas
    sleep 3
    start_arthas
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
