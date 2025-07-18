########################使用graalvm镜像编译############################3
## Simple Dockerfile adding Maven and GraalVM Native Image compiler to the standard
## 镜像版本号来自 graalvm 官网 https://github.com/graalvm/container/pkgs/container/graalvm-ce
#FROM ghcr.io/graalvm/graalvm-ce:ol8-java17-22.2.0
#
#ADD https://192.168.1.111/files/apache-maven-3.8.6.tar /tools/
#ADD https://192.168.1.111/files/native-image-installable-svm-java17-linux-amd64-22.2.0.jar /tools/
#COPY . /build
#
#WORKDIR /tools
#RUN set -x && \
#    tar -xvf apache-maven-*.tar && \
#    rm -rf apache-maven-*.tar && \
#    mv apache-maven-* maven && \
#    mv native-image-installable-*.jar native-image-installable.jar && \
#    export MAVEN_HOME=/tools/maven && \
#    export PATH=$PATH:$MAVEN_HOME/bin && \
#    gu -L install native-image-installable.jar && \
#    mvn --version && \
#    native-image --version && \
#    cd /build && \
#    mvn -Pnative clean package -DskipTests
# We use a Docker multi-stage build here in order to only take the compiled native Spring Boot App from the first build container
#FROM oraclelinux:7-slim
#
#MAINTAINER Shanhy
#
## Add Spring Boot Native app spring-boot-graal to Container
#COPY --from=0 "/build/target/spring-native-demo" spring-native-demo
#
#ENV PORT=8081
#
## Fire up our Spring Boot Native app by default
#CMD [ "sh", "-c", "./spring-native-demo -Dserver.port=$PORT" ]

FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk21:latest as builder

WORKDIR /root

ADD ./build/libs/taotao-cloud-data-sync-2025.07.jar ./

RUN java -Djarmode=layertools -jar taotao-cloud-data-sync-2025.07.jar extract

#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-skywalking:openjdk-17-8.6.0
#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk21:latest
FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk21-skywalking-9-3-0:latest

ARG BACKEND_SERVICE=192.168.10.200:11800
ARG AGENT_AUTHENTICATION=taotao-cloud
ARG TAOTAO_CLOUD_MAIL_PASSWORD=xxxx
ARG APP_NAME=taotao-cloud-data-sync
ARG APP_VERSION=2025.07
ARG JAR=${APP_NAME}-${VERSION}
ARG JAR_NAME=${JAR}.jar
ARG APP_PORT=33334
ARG PRROFILES=dev

LABEL application_name=${APP_NAME}
LABEL application_version=${APP_VERSION}
LABEL org.opencontainers.image.authors=981376577@qq.com

ENV TZ=Asia/Shanghai
ENV APP_NAME=${APP_NAME}
ENV APP_VERSION=${APP_VERSION}
ENV JAR_NAME=${APP_NAME}-${VERSION}.jar
ENV BACKEND_SERVICE=${BACKEND_SERVICE}
ENV AGENT_AUTHENTICATION=${AGENT_AUTHENTICATION}
ENV TAOTAO_CLOUD_MAIL_PASSWORD=${TAOTAO_CLOUD_MAIL_PASSWORD}
ENV PRROFILES=${PRROFILES}
ENV JAVA_OPTIONS="-Xms512m \
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
                  -Xlog:gc*=debug:file=/root/logs/${APP_NAME}/gc.log:utctime,level,tags:filecount=50,filesize=100M \
                  -Xlog:jit+compilation=debug:file=/root/logs/${APP_NAME}/jit.compile.log:utctime,level,tags:filecount=10,filesize=100M \
                  -XX:MetaspaceSize=256m \
                  -XX:MaxMetaspaceSize=256m \
                  -verbose:gc \
                  -Xlog:async \
                  -XX:AsyncLogBufferSize=409600 \
                  -XX:ParallelGCThreads=4 \
                  -Djava.security.egd=file:/dev/./urandom \
                  -Dfile.encoding=utf-8 \
                  --add-opens java.base/java.lang=ALL-UNNAMED \
                  --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
                  --add-opens java.base/java.util=ALL-UNNAMED \
                  --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
                  -Dspring.profiles.active=${PRROFILES} \
                  -javaagent:/skywalking/agent/skywalking-agent.jar \
                  -Dskywalking.agent.service_name=${APP_NAME} \
                  -Dskywalking.agent.authentication=${AGENT_AUTHENTICATION} \
                  -Dskywalking.collector.backend_service=${BACKEND_SERVICE} \
                  -Dskywalking.logging.file_name=skywalking.log \
                  -Dskywalking.logging.level=INFO \
                  -Dskywalking.logging.dir=/root/logs/${APP_NAME}"

USER root

WORKDIR /root

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && mkdir -p /root/logs/${APP_NAME} \
    && touch /root/logs/${APP_NAME}/jit.compile.log /root/logs/${APP_NAME}/gc.log \
    && sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
    && apk add curl

VOLUME /root/logs

EXPOSE ${APP_PORT}

COPY --from=builder /root/dependencies/ ./
RUN true
COPY --from=builder /root/spring-boot-loader/ ./
RUN true
COPY --from=builder /root/snapshot-dependencies/ ./
RUN true
COPY --from=builder /root/application/ ./
RUN true

HEALTHCHECK --interval=60s --timeout=60s --retries=5 CMD curl -fs http://127.0.0.1:${APP_PORT}/actuator/health || exit 1

ENTRYPOINT sleep 30; java ${JAVA_OPTIONS} org.springframework.boot.loader.launch.JarLauncher
