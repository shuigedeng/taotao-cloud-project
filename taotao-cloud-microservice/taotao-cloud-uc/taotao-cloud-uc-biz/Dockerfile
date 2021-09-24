#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk11:alpine
FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-skywalking:8.7.0-es7

MAINTAINER 981376577@qq.com

ENV TZ=Asia/Shanghai

ENV APP_NAME=taotao-cloud-uc-biz

ENV VERSION=2021.9.3

ENV DINGDING_TOKEN_ID=xxxxx

ENV DINGDING_SECRET=xxxxx

ENV JAR_NAME=${APP_NAME}-${VERSION}.jar

ENV BACKEND_SERVICE=172.16.6.151:11800

ENV AGENT_AUTHENTICATION=taotao-cloud

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /root/logs/${APP_NAME}

RUN touch /root/logs/${APP_NAME}/${APP_NAME}.jit.compile.log

WORKDIR /root

EXPOSE 33337

ADD ./build/libs/${JAR_NAME} ./

CMD sleep 10; java \
    -Xms512m \
    -Xmx2g \
    -Xss256k \
    -XX:MaxDirectMemorySize=256m \
    -XX:SurvivorRatio=8 \
    -XX:+UseCompressedOops \
    -XX:+UseCompressedClassPointers \
    -XX:+SegmentedCodeCache \
    -XX:+PrintCommandLineFlags \
    -XX:+ExplicitGCInvokesConcurrent \
    -XX:+HeapDumpOnOutOfMemoryError \
    -Xlog:gc*=debug:file=/root/logs/${APP_NAME}/${APP_NAME}.gc.log:utctime,level,tags:filecount=50,filesize=100M \
    -Xlog:jit+compilation=debug:file=/root/logs/${APP_NAME}/${APP_NAME}.jit.compile.log:utctime,level,tags:filecount=10,filesize=100M \
    -XX:MetaspaceSize=256m \
    -XX:MaxMetaspaceSize=256m \
    -verbose:gc \
    -XX:ParallelGCThreads=4 \
    -Djava.security.egd=file:/dev/./urandom \
    -Dfile.encoding=utf-8 \
    -javaagent:/skywalking/agent/skywalking-agent.jar \
    -Dskywalking.agent.service_name=${APP_NAME} \
    -Dskywalking.agent.authentication=${AGENT_AUTHENTICATION} \
    -Dskywalking.logging.file_name=${APP_NAME}.skywalking.log \
    -Dskywalking.logging.level=INFO \
    -Dskywalking.logging.dir=/root/logs/${APP_NAME} \
    -Dskywalking.collector.backend_service=${BACKEND_SERVICE} \
    -Dspring.profiles.active=dev \
    -jar ${JAR_NAME}
