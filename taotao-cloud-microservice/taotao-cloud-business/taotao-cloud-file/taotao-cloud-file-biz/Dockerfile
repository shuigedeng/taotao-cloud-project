FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk17:amd64 as builder

WORKDIR /root

ADD ./build/libs/taotao-cloud-file-2023.09.jar ./

RUN java -Djarmode=layertools -jar taotao-cloud-file-2023.09.jar extract

#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-skywalking:openjdk-17-8.6.0
#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk17:amd64
FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk17:skywalking8.16.0-pyroscope0.11.5-jre-font

ARG SKYWALKING_BACKEND_SERVICE=192.168.10.220:11800
ARG SKYWALKING_AGENT_AUTHENTICATION=taotao-cloud
ARG APP_NAME=taotao-cloud-file
ARG APP_VERSION=2023.09
ARG APP_JAR_NAME=${APP_NAME}-${APP_VERSION}.jar
ARG APP_PORT=33339
ARG APP_PRROFILES=dev

LABEL application_name=${APP_NAME}
LABEL application_version=${APP_VERSION}
LABEL org.opencontainers.image.authors=981376577@qq.com

#****************************app****************************
ENV TZ=Asia/Shanghai
ENV APP_NAME=${APP_NAME}
ENV APP_VERSION=${APP_VERSION}
ENV APP_JAR_NAME=${APP_NAME}-${APP_VERSION}.jar
ENV APP_PRROFILES=${APP_PRROFILES}
#****************************skywalking 从env中获取配置数据****************************
ENV SW_AGENT_NAME=${APP_NAME}
ENV SW_AGENT_AUTHENTICATION=${SKYWALKING_AGENT_AUTHENTICATION}
ENV SW_AGENT_COLLECTOR_BACKEND_SERVICES=${SKYWALKING_BACKEND_SERVICE}
ENV SW_AGENT_TRACE_IGNORE_PATH="Redisson/PING,/actuator/**,/admin/**"
#日志数据的grpc服务器的主机
ENV SW_GRPC_LOG_SERVER_HOST=192.168.10.220
#日志数据的grpc服务器的端口
ENV SW_GRPC_LOG_SERVER_PORT=11800
#日志数据的最大大小
ENV SW_GRPC_LOG_MAX_MESSAGE_SIZE=10485760
#发送数据时将超时多长时间。单位是秒
ENV SW_GRPC_LOG_GRPC_UPSTREAM_TIMEOUT=30
ENV SW_LOGGING_LEVEL=INFO
ENV SW_LOGGING_FILE_NAME=skywalking.log
ENV SW_LOGGING_DIR=/root/logs/${APP_NAME}
#****************************pyroscope 从env中获取配置数据****************************
ENV PYROSCOPE_APPLICATION_NAME=${APP_NAME}
ENV PYROSCOPE_SERVER_ADDRESS=http://192.168.10.220:4040
ENV PYROSCOPE_PROFILING_INTERVAL=10ms
ENV PYROSCOPE_PROFILER_EVENT=cpu,alloc,lock,wall,itimer
ENV PYROSCOPE_UPLOAD_INTERVAL=10s
ENV PYROSCOPE_LOG_LEVEL=info
ENV PYROSCOPE_LABELS="APP=taotao-cloud-monitor"
#ENV PYROSCOPE_AUTH_TOKEN=info

ENV DEBUG_OPTIONS = "-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8889"

ENV JAVA_OPTIONS="-Xms2048m -Xmx2048m -Xmn1280m \
                  -Xss512k \
                  -XX:MaxDirectMemorySize=1024m \
                  -XX:MetaspaceSize=256m \
                  -XX:MaxMetaspaceSize=512m \
                  -XX:ReservedCodeCacheSize=256m \
                  -XX:+DisableExplicitGC \
                  -XX:+UnlockDiagnosticVMOptions \
                  -XX:+UseZGC  \
                  -XX:MaxGCPauseMillis=50 \
                  -XX:-UseBiasedLocking \
                  -XX:GuaranteedSafepointInterval=0  \
                  -XX:FlightRecorderOptions=maxchunksize=128m  \
                  -XX:+UseCountedLoopSafepoints \
                  -XX:StartFlightRecording=disk=true,maxsize=4096m,maxage=3d \
                  -XX:-OmitStackTraceInFastThrow \
                  -XX:+UnlockExperimentalVMOptions \
                  -XX:SurvivorRatio=8 \
                  -XX:+UseCompressedOops \
                  -XX:+UseCompressedClassPointers \
                  -XX:+SegmentedCodeCache \
                  -XX:+PrintCommandLineFlags \
                  -XX:+ExplicitGCInvokesConcurrent \
                  -XX:+HeapDumpOnOutOfMemoryError \
                  -XX:+ShowCodeDetailsInExceptionMessages \
                  -XX:ParallelGCThreads=4 \
                  -Xlog:async \
                  -XX:AsyncLogBufferSize=409600 \
                  -Xlog:gc*=debug:file=/root/logs/${APP_NAME}/gc.log:utctime,level,tags:filecount=50,filesize=100M \
                  -Xlog:jit+compilation=debug:file=/root/logs/${APP_NAME}/jit.compile.log:utctime,level,tags:filecount=10,filesize=100M \
                  -Xlog:safepoint=debug:file=/root/logs/${APP_NAME}/safepoint%t.log:utctime,level,tags:filecount=10,filesize=10M \
                  -verbose:gc \
                  -Dnetworkaddress.cache.ttl=10 \
                  -Djava.security.egd=file:/dev/./urandom \
                  -Dfile.encoding=utf-8 \
                  --add-opens java.base/java.security=ALL-UNNAMED \
                  --add-opens java.base/jdk.internal.misc=ALL-UNNAMED \
                  --add-opens java.base/java.text=ALL-UNNAMED \
                  --add-opens java.base/java.nio=ALL-UNNAMED \
                  --add-opens java.base/jdk.internal.access=ALL-UNNAMED \
                  --add-opens java.base/java.time=ALL-UNNAMED \
                  --add-opens java.base/java.io=ALL-UNNAMED \
                  --add-opens java.base/java.net=ALL-UNNAMED \
                  --add-opens java.base/java.lang=ALL-UNNAMED \
                  --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
                  --add-opens java.base/java.util=ALL-UNNAMED \
                  --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
                  --add-opens java.base/sun.reflect.annotation=ALL-UNNAMED \
                  --add-opens java.base/sun.net=ALL-UNNAMED \
                  --add-opens java.base/java.math=ALL-UNNAMED \
                  --add-exports java.desktop/sun.awt=ALL-UNNAMED \
                  --add-opens java.desktop/sun.awt=ALL-UNNAMED \
                  -javaagent:/skywalking/agent/skywalking-agent.jar \
                  -Dspring.profiles.active=${APP_PRROFILES} \
                  -javaagent:/skywalking/agent/skywalking-agent.jar "
#在-Dspring.profiles.active上面加上开启远程debug  ${DEBUG_OPTIONS} \  不能同时使用Dskywalking

USER root

WORKDIR /root

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
	  && echo $TZ > /etc/timezone \
    && mkdir -p /root/logs/${APP_NAME} \
	  && touch /root/logs/${APP_NAME}/jit.compile.log /root/logs/${APP_NAME}/gc.log

RUN ls -l /etc/apk
#RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

#RUN echo 'https://mirrors.ustc.edu.cn/alpine/v3.7/main \
#       https://mirrors.ustc.edu.cn/alpine/v3.7/community\
#       https://mirrors.ustc.edu.cn/alpine/edge/main\
#       https://mirrors.ustc.edu.cn/alpine/edge/community\
#       https://mirrors.ustc.edu.cn/alpine/edge/testing' >/etc/apk/repositories
RUN apk update --allow-untrusted && apk add curl --allow-untrusted
RUN apk add libstdc++ --allow-untrusted

VOLUME /root/logs

EXPOSE ${APP_PORT}

EXPOSE 8889

COPY --from=builder /root/dependencies/ ./
RUN true
COPY --from=builder /root/spring-boot-loader/ ./
RUN true
COPY --from=builder /root/snapshot-dependencies/ ./
RUN true
COPY --from=builder /root/application/ ./
RUN true

HEALTHCHECK --interval=60s --timeout=30s --retries=5 CMD curl -fs http://127.0.0.1:${APP_PORT}/actuator/health || exit 1

ENTRYPOINT sleep 10; java ${JAVA_OPTIONS} org.springframework.boot.loader.JarLauncher
