FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk17:amd64 as builder

WORKDIR /root

ADD ./build/libs/taotao-cloud-order-biz-2021.10.1.jar ./

RUN java -Djarmode=layertools -jar taotao-cloud-order-biz-2021.10.1.jar extract

FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-skywalking:openjdk-17-8.6.0

ARG BACKEND_SERVICE=172.16.6.151:11800
ARG AGENT_AUTHENTICATION=taotao-cloud
ARG APP_NAME=taotao-cloud-order-biz
ARG APP_VERSION=2021.10.1
ARG JAR=${APP_NAME}-${VERSION}
ARG JAR_NAME=${JAR}.jar
ARG APP_PORT=33338

LABEL application_name=${APP_NAME}
LABEL application_version=${APP_VERSION}
LABEL org.opencontainers.image.authors=981376577@qq.com

ENV TZ=Asia/Shanghai
ENV APP_NAME=${APP_NAME}
ENV VERSION=${APP_VERSION}
ENV JAR_NAME=${APP_NAME}-${VERSION}.jar
ENV BACKEND_SERVICE=${BACKEND_SERVICE}
ENV AGENT_AUTHENTICATION=${AGENT_AUTHENTICATION}
ENV TAOTAO_CLOUD_MAIL_PASSWORD=${TAOTAO_CLOUD_MAIL_PASSWORD}
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
                  -Dspring.profiles.active=dev"

USER root

WORKDIR /root

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
	  && echo $TZ > /etc/timezone \
    && mkdir -p /root/logs/${APP_NAME} \
	  && touch /root/logs/${APP_NAME}/${APP_NAME}.jit.compile.log /root/logs/${APP_NAME}/${APP_NAME}.gc.log \
    && sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
    && apk add curl

VOLUME /root/logs

EXPOSE ${APP_PORT}

COPY --from=builder /root/dependencies/ ./
COPY --from=builder /root/spring-boot-loader/ ./
COPY --from=builder /root/snapshot-dependencies/ ./
COPY --from=builder /root/application/ ./

HEALTHCHECK --interval=120s --timeout=60s --retries=5 CMD curl -fs http://127.0.0.1:${APP_PORT}/actuator/health || exit 1

ENTRYPOINT sleep 30; java ${JAVA_OPTIONS} org.springframework.boot.loader.JarLauncher
