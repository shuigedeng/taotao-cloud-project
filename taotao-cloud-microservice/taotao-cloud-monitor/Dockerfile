FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk21:latest as builder

WORKDIR /root

ADD ./build/libs/taotao-cloud-monitor-2025.07.jar ./

#RUN java -Djarmode=layertools -jar taotao-cloud-monitor-2025.07.jar extract
RUN java -Djarmode=tools -jar taotao-cloud-monitor-2025.07.jar extract --layers --destination extracted

#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-skywalking:openjdk-17-8.6.0
#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk21:latest
#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk21:skywalking9.1.0-pyroscope0.13.0-jre-font
FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk21-skywalking-9-3-0:latest

ARG SKYWALKING_BACKEND_SERVICE=192.168.218.2:11800
ARG SKYWALKING_AGENT_AUTHENTICATION=taotao-cloud
ARG APP_NAME=taotao-cloud-monitor
ARG APP_VERSION=2025.07
ARG APP_JAR_NAME=${APP_NAME}-${APP_VERSION}.jar
ARG APP_PORT=33308
ARG APP_PRROFILES=dev
ARG DINGDING_TOKEN_ID_ARG=xxxxxx
ARG DINGDING_SECRET_ARG=xxxxxx
ARG REQUEST_API_DOCS=1

LABEL application.name=${APP_NAME}
LABEL application.version=${APP_VERSION}
LABEL org.opencontainers.image.authors=981376577@qq.com

#****************************app****************************
ENV TZ=Asia/Shanghai
ENV APP_NAME=${APP_NAME}
ENV APP_PORT=${APP_PORT}
ENV APP_VERSION=${APP_VERSION}
ENV APP_JAR_NAME=${APP_NAME}-${APP_VERSION}.jar
ENV APP_PRROFILES=${APP_PRROFILES}
ENV REQUEST_API_DOCS=${REQUEST_API_DOCS}

ENV DINGDING_TOKEN_ID=${DINGDING_TOKEN_ID_ARG}
ENV DINGDING_SECRET=${DINGDING_SECRET_ARG}
#****************************skywalking 从env中获取配置数据****************************
ENV SW_AGENT_NAME=${APP_NAME}
ENV SW_AGENT_AUTHENTICATION=${SKYWALKING_AGENT_AUTHENTICATION}
ENV SW_AGENT_COLLECTOR_BACKEND_SERVICES=${SKYWALKING_BACKEND_SERVICE}
ENV SW_AGENT_TRACE_IGNORE_PATH="Redisson/PING,/actuator/**,/admin/**"
#日志数据的grpc服务器的主机
ENV SW_GRPC_LOG_SERVER_HOST=192.168.218.2
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
ENV PYROSCOPE_SERVER_ADDRESS=http://192.168.218.2:4040
ENV PYROSCOPE_PROFILING_INTERVAL=10ms
ENV PYROSCOPE_PROFILER_EVENT=cpu,alloc,lock,wall,itimer
ENV PYROSCOPE_UPLOAD_INTERVAL=10s
ENV PYROSCOPE_LOG_LEVEL=info
ENV PYROSCOPE_LABELS="APP=taotao-cloud-monitor"
#ENV PYROSCOPE_AUTH_TOKEN=info

ENV ZGC_OPTIONS = "-XX:+UnlockDiagnosticVMOptions  \
                  -XX:+UnlockExperimentalVMOptions  \
                  -XX:-OmitStackTraceInFastThrow  \
                  -Xlog:gc*=debug:file=/root/logs/${APP_NAME}/gc.log:utctime,level,tags:filecount=50,filesize=100M \
                  -Xlog:jit+compilation=debug:file=/root/logs/${APP_NAME}/jit.compile.log:utctime,level,tags:filecount=10,filesize=100M \
                  -Xlog:safepoint=debug:file=/root/logs/${APP_NAME}/safepoint%t.log:utctime,level,tags:filecount=10,filesize=10M \
                  -Dfile.encoding=UTF-8  \
                  -Djava.security.egd=file:/dev/./urandom  \
                  -Dnetworkaddress.cache.ttl=10  \
                  -Xms2048m  \
                  -Xmx2048m  \
                  -Xmn1280m  \
                  -Xss512k  \
                  -XX:MaxDirectMemorySize=1024m  \
                  -XX:MetaspaceSize=384m  \
                  -XX:ReservedCodeCacheSize=256m  \
                  -XX:+DisableExplicitGC  \
                  -XX:+UseZGC  \
                  -XX:GuaranteedSafepointInterval=0  \
                  -XX:+UseCountedLoopSafepoints  \
                  -XX:StartFlightRecording=disk=true,maxsize=4096m,maxage=3d  \
                  -XX:FlightRecorderOptions=maxchunksize=128m  \
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
                  -Dspring.profiles.active=${APP_PRROFILES} \
                  -javaagent:/skywalking/agent/skywalking-agent.jar \
                  -Dskywalking.agent.service_name=${APP_NAME} \
                  -Dskywalking.agent.authentication=${SKYWALKING_AGENT_AUTHENTICATION} \
                  -Dskywalking.logging.file_name=skywalking.log \
                  -Dskywalking.logging.level=INFO \
                  -Dskywalking.logging.dir=/root/logs/${APP_NAME} \
                  -Dskywalking.collector.backend_service=${SKYWALKING_BACKEND_SERVICE} "

ENV JAVA_OPTIONS="-Xms1g \
                  -Xmx2g \
                  -Xmn1280m \
                  -Xss512k \
                  -Xlog:async \
                  -XX:MaxDirectMemorySize=1024m \
                  -XX:MetaspaceSize=256m \
                  -XX:MaxMetaspaceSize=512m \
                  -XX:ReservedCodeCacheSize=256m \
                  -XX:+DisableExplicitGC \
                  -XX:+UnlockDiagnosticVMOptions \
                  -XX:+UseZGC  \
                  -XX:MaxGCPauseMillis=50 \
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
                  -Dspring.profiles.active=${APP_PRROFILES} \
                  -javaagent:/skywalking/agent/skywalking-agent.jar"

USER root

WORKDIR /root

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
	&& echo $TZ > /etc/timezone \
    && mkdir -p /root/logs/${APP_NAME} \
	&& touch /root/logs/${APP_NAME}/jit.compile.log /root/logs/${APP_NAME}/gc.log

#https://mirrors.aliyun.com/alpine/v3.14/main
#https://mirrors.ustc.edu.cn/alpine/v3.14/main/
#清华TUNA镜像源 https://mirror.tuna.tsinghua.edu.cn/alpine/v3.14/main
#RUN echo 'http://mirrors.aliyun.com/alpine/v3.14/main' > /etc/apk/repositories \
#    && echo 'http://mirrors.aliyun.com/alpine/v3.14/community' >>/etc/apk/repositories \
#    && set -eux \
#    && apk update --allow-untrusted \
#    && apk add curl --allow-untrusted \
#    && apk add libstdc++ --allow-untrusted \
#    && apk add --no-cache --update fontconfig ttf-dejavu \
#    && apk add --no-cache --update tzdata

VOLUME /root/logs

EXPOSE ${APP_PORT}

COPY --from=builder /root/extracted/dependencies/ ./
RUN true
COPY --from=builder /root/extracted/spring-boot-loader/ ./
RUN true
COPY --from=builder /root/extracted/snapshot-dependencies/ ./
RUN true
COPY --from=builder /root/extracted/application/ ./
RUN true

#docker inspect --format='{{json .State.Health}}' file
#interval: 30s #运行间隔时间(ms|s|m|h)(缺省为0s)
#timeout: 20s #一次检查运行的最大时间(ms|s|m|h)(默认为0s)
#retries: 3   #连续失败次数后报告不健康
#start_period: 10s #容器初始化的起始周期(ms|s|m|h)(默认0)
#HEALTHCHECK --interval=60s --timeout=30s --retries=5 CMD curl -fs http://192.168.218.2:${APP_PORT}/actuator/health || exit 1
#使用healthcheck.sh
#HEALTHCHECK --interval=60s --timeout=30s --retries=5 CMD curl -fs http://192.168.218.2:${APP_PORT}/actuator/health || /bin/sh -c 'kill -s 15 -1 && (sleep 30; kill -s 9 -1)'

#HEALTHCHECK --start-period=60s --interval=10s --timeout=3s --retries=50 CMD /bin/sh /healthcheck.sh

#ENTRYPOINT ["java","${ZGC_OPTIONS}","-jar","taotao-cloud-monitor-2025.07.jar"]
#ENTRYPOINT java ${ZGC_OPTIONS} -jar taotao-cloud-monitor-2025.07.jar
ENTRYPOINT sleep 30; java ${JAVA_OPTIONS} org.springframework.boot.loader.launch.JarLauncher

#cds
#https://docs.spring.io/spring-boot/reference/packaging/class-data-sharing.html
#https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html
RUN java -XX:ArchiveClassesAtExit=taotao-cloud-monitor.jsa -Dspring.context.exit=onRefresh -jar taotao-cloud-monitor-2025.07.jar

ENTRYPOINT ["java", "-XX:SharedArchiveFile=taotao-cloud-monitor.jsa", "-jar", "taotao-cloud-monitor-2025.07.jar"]
