FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:latest as builder

WORKDIR /root

ADD ./build/libs/taotao-cloud-xxljob-2026.01.jar ./

RUN java -Djarmode=layertools -jar taotao-cloud-xxljob-2026.01.jar extract

#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-skywalking:openjdk-17-8.6.0
#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:latest
#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:skywalking9.1.0-pyroscope0.13.0-jre-font
FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:latest

ARG SKYWALKING_BACKEND_SERVICE=192.168.218.2:11800
ARG SKYWALKING_AGENT_AUTHENTICATION=taotao-cloud
ARG APP_NAME=taotao-cloud-xxljob
ARG APP_VERSION=2026.01
ARG APP_JAR_NAME=${APP_NAME}-${APP_VERSION}.jar
ARG APP_PORT=33313
ARG APP_PRROFILES=dev
ARG REQUEST_API_DOCS=0

LABEL application_name=${APP_NAME}
LABEL application_version=${APP_VERSION}
LABEL org.opencontainers.image.authors=981376577@qq.com

#****************************app****************************
ENV TZ=Asia/Shanghai
ENV APP_NAME=${APP_NAME}
ENV APP_PORT=${APP_PORT}
ENV APP_VERSION=${APP_VERSION}
ENV APP_JAR_NAME=${APP_NAME}-${APP_VERSION}.jar
ENV APP_PRROFILES=${APP_PRROFILES}
ENV REQUEST_API_DOCS=${REQUEST_API_DOCS}
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
ENV PYROSCOPE_LABELS="APP=taotao-cloud-xxljob"
#ENV PYROSCOPE_AUTH_TOKEN=info

#JVM 包含多种不同类型的参数选项、
#-- 用来设置应用参数，不是JVM参数的标准前缀
#   -- 用于将应用参数传递给 Spring Boot 应用本身(而不是 JVM) 本质：Spring Boot 的命令行参数（Command line arguments）
#   --xxx（命令行参数）的优先级高于 -Dxxx（JVM 系统属性
#-D 用来设置系统属性，属于标准选项   可以通过 System.getProperty("property") 在代码中获取
#-X 非标准参数，支持的选项范围跟具体的 JVM 实现有关 非标准但稳定的 JVM 选项
#-XX 非标准参数，设置高级选项，允许开发者调整 JVM 的行为、性能、输出调试信息，支持的选项范围也跟具体的 JVM 实现有关
#-XX 高级/实验性 JVM 选项 用于控制 JVM 内部行为，如垃圾回收器、JIT 编译器、内存管理等
#    分为两类：
#      布尔型：-XX:+Option（启用）、-XX:-Option（禁用）
#      键值型：-XX:Option=value
#布尔类型的高级选项是起到功能的开关作用，不带参数。使用 + 启用功能，使用 - 禁用功能；对于带参数的高级选项，需要指定参数值
#使用 java -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+PrintFlagsFinal -version 命令可以查看 JVM 所有的选项
ENV JAVA_OPTIONS="-Xms1g \
                  -Xmx2g \
                  -Xmn1280m \
                  -Xss512k \
                  -verbose:gc \
                  -Xlog:async \
                  -Xlog:gc*=debug:file=/root/logs/${APP_NAME}/gc.log:utctime,level,tags:filecount=50,filesize=100M \
                  -Xlog:jit+compilation=debug:file=/root/logs/${APP_NAME}/jit.compile.log:utctime,level,tags:filecount=10,filesize=100M \
                  -Xlog:safepoint=debug:file=/root/logs/${APP_NAME}/safepoint%t.log:utctime,level,tags:filecount=10,filesize=10M \
                  -XX:+UseContainerSupport \
                  -XX:MaxRAMPercentage=75 \
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
                  -Dnetworkaddress.cache.ttl=10 \
                  -Djava.security.egd=file:/dev/./urandom \
                  -Dfile.encoding=utf-8 \
                  -Duser.timezone=GMT+08 \
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

COPY --from=builder /root/dependencies/ ./
RUN true
COPY --from=builder /root/spring-boot-loader/ ./
RUN true
COPY --from=builder /root/snapshot-dependencies/ ./
RUN true
COPY --from=builder /root/application/ ./
RUN true

#docker inspect --format='{{json .State.Health}}' file
#interval: 30s #运行间隔时间(ms|s|m|h)(缺省为0s)
#timeout: 20s #一次检查运行的最大时间(ms|s|m|h)(默认为0s)
#retries: 3   #连续失败次数后报告不健康
#start_period: 10s #容器初始化的起始周期(ms|s|m|h)(默认0)
#HEALTHCHECK --interval=60s --timeout=30s --retries=5 CMD curl -fs http://192.168.218.2:${APP_PORT}/actuator/health || exit 1
#使用healthcheck.sh
#HEALTHCHECK --interval=60s --timeout=30s --retries=5 CMD curl -fs http://192.168.218.2:${APP_PORT}/actuator/health || /bin/sh -c 'kill -s 15 -1 && (sleep 10; kill -s 9 -1)'


#HEALTHCHECK --start-period=30s --interval=10s --timeout=3s --retries=50 CMD /bin/sh /healthcheck.sh

ENTRYPOINT sleep 30; java ${JAVA_OPTIONS} org.springframework.boot.loader.launch.JarLauncher
