###########################################
cd /opt/taotao-cloud/rocketmq4.9.0

wget https://apache.website-solution.net/rocketmq/4.9.0/rocketmq-all-4.9.0-bin-release.zip

zip rocketmq-all-4.9.0-bin-release.zip

# RocketMQ默认的虚拟机内存较大，启动Broker如果因为内存不足失败，需要编辑如下两个配置文件，修改JVM内存大小
vi runbroker.sh
vi runserver.sh

JAVA_OPT="${JAVA_OPT} -server -Xms256m -Xmx256m -Xmn128m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"

# 测试RocketMQ
发送消息
  # 1.设置环境变量
  export NAMESRV_ADDR=localhost:9876
  # 2.使用安装包的Demo发送消息
  sh bin/tools.sh org.apache.rocketmq.example.quickstart.Producer

接收消息
  # 1.设置环境变量
  export NAMESRV_ADDR=localhost:9876
  # 2.接收消息
  sh bin/tools.sh org.apache.rocketmq.example.quickstart.Consumer

# rocketmq-console控制台
java -jar /Users/gongninggang/source/rocketmq-externals/rocketmq-console/target/rocketmq-console-ng-1.0.1.jar

# mqadmin使用
sh mqadmin
sh mqadmin help topicList
sh mqadmin topicList -n '192.168.10.220:9876'

##################### rocketmq.sh #############################
#!/bin/bash

function start_rocketmq() {
     nohup sh /opt/taotao-cloud/rocketmq4.9.0/bin/mqnamesrv >/opt/taotao-cloud/rocketmq4.9.0/mqnamesrv.log 2>&1 &
     nohup sh /opt/taotao-cloud/rocketmq4.9.0/bin/mqbroker -n 192.168.10.220:9876 >/opt/taotao-cloud/rocketmq4.9.0/broker.log 2>&1 &
     sleep 10
     echo " rocketmq started"
}

function stop_rocketmq() {
     sh /opt/taotao-cloud/rocketmq4.9.0/bin/mqshutdown namesrv
     sh /opt/taotao-cloud/rocketmq4.9.0/bin/mqshutdown broker
     sleep 10
     echo "rocketmq stoped"
}

case $1 in
"start")
    start_rocketmq
    ;;
"stop")
    stop_rocketmq
    ;;
"restart")
    stop_rocketmq
    sleep 15
    start_rocketmq
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac



1:Dokcer搜索RocketMq
        docker search rocketmq

2:查看某一个镜像里面的所有版本（如foxiswho/rocketmq）
        curl https://registry.hub.docker.com/v1/repositories/foxiswho/rocketmq/tags\
        | tr -d '[\[\]" ]' | tr '}' '\n'\
        | awk -F: -v image='foxiswho/rocketmq' '{if(NR!=NF && $3 != ""){printf("%s:%s\n",image,$3)}}'

3:启动NameServer
        docker run -d -p 9876:9876 --name rmqserver  foxiswho/rocketmq:server-4.5.1

4:启动Broker
        docker run -d -p 10911:10911 -p 10909:10909\
        --name rmqbroker --link rmqserver:namesrv\
        -e "NAMESRV_ADDR=namesrv:9876" -e "JAVA_OPTS=-Duser.home=/opt"\
        -e "JAVA_OPT_EXT=-server -Xms128m -Xmx128m"\
        foxiswho/rocketmq:broker-4.5.1

5:安装Console
        docker run -d --name rmqconsole -p 8180:8080 --link rmqserver:namesrv\
        -e "JAVA_OPTS=-Drocketmq.namesrv.addr=namesrv:9876\
        -Dcom.rocketmq.sendMessageWithVIPChannel=false"\
        -t styletang/rocketmq-console-ng

6:查看是否安装成功
         docker ps|grep rocketmq

http://192.168.10.220:8180/
