#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk11:alpine
FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-skywalking:8.7.0-es7

MAINTAINER 981376577@qq.com

ENV TZ=Asia/Shanghai

ENV APP_NAME=taotao-cloud-order

ENV VERSION=2021.9.3

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /root

EXPOSE 33338

ADD ./taotao-cloud-order-biz/build/libs/${APP_NAME}-${VERSION}.jar ./

CMD sleep 10; java \
    -Xms512m \
    -Xmx512m \
    -XX:MetaspaceSize=512m \
    -XX:MaxMetaspaceSize=1024m \
    -XX:ParallelGCThreads=4 \
    -XX:+HeapDumpOnOutOfMemoryError \
    -Dfile.encoding=utf-8 \
    -javaagent:/skywalking/agent/skywalking-agent.jar \
    -Dskywalking.agent.service_name=${APP_NAME} \
    -Dskywalking.agent.authentication=taotao-cloud \
    -Dskywalking.logging.file_name=${APP_NAME}.skywalking.log \
    -Dskywalking.logging.level=INFO \
    -Dskywalking.logging.dir=/root/logs \
    -Dskywalking.collector.backend_service=172.16.6.151:11800 \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active=dev \
    -jar ${APP_NAME}-${VERSION}.jar
