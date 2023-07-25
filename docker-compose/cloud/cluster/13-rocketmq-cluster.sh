version: '2'
services:
  namesrv:
    image: apacherocketmq/rocketmq:4.7.1
    container_name: rmqnamesrv
    ports:
      - 9876:9876
    volumes:
      - ./data/namesrv/logs:/home/rocketmq/logs
    command: sh mqnamesrv
  broker:
    image: apacherocketmq/rocketmq:4.7.1
    container_name: rmqbroker
    links:
      - namesrv
    ports:
      - 10909:10909
      - 10911:10911
      - 10912:10912
    environment:
      - NAMESRV_ADDR=namesrv:9876
    volumes:
      - ./data/broker/logs:/home/rocketmq/logs
      - ./data/broker/store:/home/rocketmq/store
      - ./data/broker/conf/broker.conf:/opt/rocketmq-4.7.1/conf/broker.conf
    command: sh mqbroker -c /opt/rocketmq-4.7.1/conf/broker.conf
  broker1:
    image: apacherocketmq/rocketmq:4.7.1
    container_name: rmqbroker-b
    links:
      - namesrv
    ports:
      - 10929:10909
      - 10931:10911
      - 10932:10912
    environment:
      - NAMESRV_ADDR=namesrv:9876
    volumes:
      - ./data1/broker/logs:/home/rocketmq/logs
      - ./data1/broker/store:/home/rocketmq/store
      - ./data1/broker/conf/broker.conf:/opt/rocketmq-4.7.1/conf/broker.conf
    command: sh mqbroker -c /opt/rocketmq-4.7.1/conf/broker.conf
  console:
    image: styletang/rocketmq-console-ng
    container_name: rocketmq-console-ng
    restart: always
    ports:
      - 8076:8080
    depends_on:
      - namesrv
    environment:
      - JAVA_OPTS= -Dlogging.level.root=info
      - Drocketmq.namesrv.addr=rmqnamesrv:9876
      - Dcom.rocketmq.sendMessageWithVIPChannel=false
