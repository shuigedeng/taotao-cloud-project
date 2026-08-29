FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:latest as builder
#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-graalvm25:latest as builder

WORKDIR /builder

COPY ./build/libs/taotao-cloud-monitor-2026.10.jar ./

RUN java -Djarmode=tools -jar taotao-cloud-monitor-2026.10.jar extract --layers --destination extracted

# FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-skywalking:openjdk-17-8.6.0
# FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:amd64
#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:latest
FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:latest

ARG SKYWALKING_BACKEND_SERVICE=127.0.0.1:11800
ARG SKYWALKING_AGENT_AUTHENTICATION=taotao-cloud
ARG APP_NAME=taotao-cloud-monitor
ARG APP_VERSION=2026.10
ARG APP_JAR_NAME=${APP_NAME}-${APP_VERSION}.jar
ARG APP_PORT=33308
ARG APP_PROFILES=dev
ARG DINGDING_TOKEN_ID_ARG=xxxxxx
ARG DINGDING_SECRET_ARG=xxxxxx
ARG REQUEST_API_DOCS=1

LABEL application_name=${APP_NAME}
LABEL application_version=${APP_VERSION}
LABEL org.opencontainers.image.authors=981376577@qq.com

#****************************app****************************
ENV TZ=Asia/Shanghai
ENV APP_NAME=${APP_NAME}
ENV APP_PORT=${APP_PORT}
ENV APP_VERSION=${APP_VERSION}
ENV APP_JAR_NAME=${APP_NAME}-${APP_VERSION}.jar
ENV APP_PROFILES=${APP_PROFILES}
ENV REQUEST_API_DOCS=${REQUEST_API_DOCS}

ENV DINGDING_TOKEN_ID=${DINGDING_TOKEN_ID_ARG}
ENV DINGDING_SECRET=${DINGDING_SECRET_ARG}
#****************************skywalking 从env中获取配置数据****************************
ENV SW_AGENT_NAME=${APP_NAME}
ENV SW_AGENT_AUTHENTICATION=${SKYWALKING_AGENT_AUTHENTICATION}
ENV SW_AGENT_COLLECTOR_BACKEND_SERVICES=${SKYWALKING_BACKEND_SERVICE}
ENV SW_AGENT_TRACE_IGNORE_PATH="Redisson/PING,/actuator/**,/admin/**"
#日志数据的grpc服务器的主机
ENV SW_GRPC_LOG_SERVER_HOST=127.0.0.1
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
ENV PYROSCOPE_SERVER_ADDRESS=http://127.0.0.1:4040
ENV PYROSCOPE_PROFILING_INTERVAL=10ms
ENV PYROSCOPE_PROFILER_EVENT=cpu,alloc,lock,wall,itimer
ENV PYROSCOPE_UPLOAD_INTERVAL=10s
ENV PYROSCOPE_LOG_LEVEL=info
ENV PYROSCOPE_LABELS="APP=taotao-cloud-monitor"
#ENV PYROSCOPE_AUTH_TOKEN=info
#
#ENV JAVA_OPTIONS_BACKUP="-Xms1g \
#                  -Xmx2g \
#                  -Xss256k \
#                  -XX:MaxDirectMemorySize=1024m \
#                  -XX:ReservedCodeCacheSize=2048m \
#                  -XX:SoftRefLRUPolicyMSPerMB=50 \
#                  -XX:CICompilerCount=2  \
#                  -XX:SurvivorRatio=8 \
#                  -XX:+UseZGC \
#                  -XX:+SegmentedCodeCache \
#                  -XX:+PrintCommandLineFlags \
#                  -XX:+ExplicitGCInvokesConcurrent \
#                  -XX:-OmitStackTraceInFastThrow\
#                  -XX:+HeapDumpOnOutOfMemoryError \
#                  -XX:+ShowCodeDetailsInExceptionMessages \
#                  -XX:ParallelGCThreads=4 \
#                  -Xlog:async \
#                  -XX:AsyncLogBufferSize=409600 \
#                  -Xlog:gc*=debug:file=/root/logs/${APP_NAME}/${APP_NAME}.gc.log:utctime,level,tags:filecount=50,filesize=100M \
#                  -Xlog:jit+compilation=debug:file=/root/logs/${APP_NAME}/${APP_NAME}.jit.compile.log:utctime,level,tags:filecount=10,filesize=100M \
#                  -verbose:gc \
#                  -Djava.security.egd=file:/dev/./urandom \
#                  -Dfile.encoding=utf-8 \
#                  --add-opens java.base/java.lang=ALL-UNNAMED \
#                  --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
#                  --add-opens java.base/java.util=ALL-UNNAMED \
#                  --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
#                  -Dspring.profiles.active=${PRROFILES} \
#                  -javaagent:/skywalking/agent/skywalking-agent.jar \
#                  -Dskywalking.agent.service_name=${APP_NAME} \
#                  -Dskywalking.agent.authentication=${AGENT_AUTHENTICATION} \
#                  -Dskywalking.logging.file_name=skywalking.log \
#                  -Dskywalking.logging.level=INFO \
#                  -Dskywalking.logging.dir=/root/logs/${APP_NAME} \
#                  -Dskywalking.collector.backend_service=${BACKEND_SERVICE} "

ENV JAVA_OPTIONS="-Xms1g \
                  -Xmx2g \
                  -Xmn1280m \
                  -Xss512k \
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
                  -XX:ConcGCThreads=4 \
                  -XX:AsyncLogBufferSize=409600 \
                  -Xlog:gc*=debug:file=/root/logs/${APP_NAME}/gc.log:utctime,level,tags:filecount=50,filesize=100M \
                  -Xlog:jit+compilation=debug:file=/root/logs/${APP_NAME}/jit.compile.log:utctime,level,tags:filecount=10,filesize=100M \
                  -Xlog:safepoint=debug:file=/root/logs/${APP_NAME}/safepoint%t.log:utctime,level,tags:filecount=10,filesize=10M \
                  -verbose:gc \
                  -Dnetworkaddress.cache.ttl=10 \
                  -Djava.security.egd=file:/dev/./urandom \
                  -Dfile.encoding=utf-8 \
                  -Dspring.profiles.active=${APP_PROFILES} \
                  -Djava.net.preferIPv4Stack=true \
                  --enable-preview \
                  --add-opens java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED \
                  --add-opens java.base/java.io=ALL-UNNAMED \
                  --add-opens java.base/java.lang=ALL-UNNAMED \
                  --add-opens java.base/java.math=ALL-UNNAMED \
                  --add-opens java.base/java.net=ALL-UNNAMED \
                  --add-opens java.base/java.net.spi=ALL-UNNAMED \
                  --add-opens java.base/java.nio=ALL-UNNAMED \
                  --add-opens java.base/java.security=ALL-UNNAMED \
                  --add-opens java.base/java.text=ALL-UNNAMED \
                  --add-opens java.base/java.text.spi=ALL-UNNAMED \
                  --add-opens java.base/java.time=ALL-UNNAMED \
                  --add-opens java.base/java.util=ALL-UNNAMED \
                  --add-opens java.base/javax.crypto=ALL-UNNAMED \
                  --add-opens java.base/javax.net=ALL-UNNAMED \
                  --add-opens java.base/javax.net.ssl=ALL-UNNAMED \
                  --add-opens java.base/sun.net.util=ALL-UNNAMED \
                  --add-opens java.base/sun.reflect.annotation=ALL-UNNAMED \
                  --add-opens java.base/jdk.internal.vm=ALL-UNNAMED \
                  --add-opens java.compiler/javax.annotation.processing=ALL-UNNAMED \
                  --add-opens java.desktop/java.applet=ALL-UNNAMED \
                  --add-opens java.desktop/java.awt=ALL-UNNAMED \
                  --add-opens java.datatransfer/java.awt.datatransfer=ALL-UNNAMED \
                  --add-opens java.desktop/java.beans=ALL-UNNAMED \
                  --add-opens java.desktop/java.beans.beancontext=ALL-UNNAMED \
                  --add-opens java.desktop/javax.accessibility=ALL-UNNAMED \
                  --add-opens java.desktop/javax.imageio=ALL-UNNAMED \
                  --add-opens java.desktop/javax.print=ALL-UNNAMED \
                  --add-opens java.desktop/javax.sound.midi=ALL-UNNAMED \
                  --add-opens java.desktop/javax.swing=ALL-UNNAMED \
                  --add-opens java.sql/java.sql=ALL-UNNAMED \
                  --add-opens java.net.http/java.net.http=ALL-UNNAMED \
                  --add-opens java.compiler/javax.lang.model=ALL-UNNAMED \
                  --add-opens java.management/javax.management=ALL-UNNAMED \
                  --add-opens java.management/java.lang.management=ALL-UNNAMED \
                  --add-opens java.management/sun.management=ALL-UNNAMED \
                  --add-opens java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED \
                  --add-opens java.management.rmi/javax.management.remote.rmi=ALL-UNNAMED \
                  --add-opens java.naming/javax.naming=ALL-UNNAMED \
                  --add-opens java.rmi/sun.rmi.transport=ALL-UNNAMED \
                  --add-opens java.rmi/java.rmi=ALL-UNNAMED \
                  --add-opens java.scripting/javax.script=ALL-UNNAMED \
                  --add-opens java.security.jgss/org.ietf.jgss=ALL-UNNAMED \
                  --add-opens java.security.jgss/javax.security.auth.kerberos=ALL-UNNAMED \
                  --add-opens java.security.sasl/javax.security.sasl=ALL-UNNAMED \
                  --add-opens java.smartcardio/javax.smartcardio=ALL-UNNAMED \
                  --add-opens java.sql/javax.sql=ALL-UNNAMED \
                  --add-opens java.sql.rowset/javax.sql.rowset=ALL-UNNAMED \
                  --add-opens java.compiler/javax.tools=ALL-UNNAMED \
                  --add-opens java.transaction.xa/javax.transaction.xa=ALL-UNNAMED \
                  --add-opens java.instrument/java.lang.instrument=ALL-UNNAMED \
                  --add-opens java.xml/javax.xml=ALL-UNNAMED \
                  --add-opens java.xml/org.xml.sax=ALL-UNNAMED \
                  --add-opens java.xml/jdk.xml.internal=ALL-UNNAMED \
                  --add-opens jdk.accessibility/com.sun.java.accessibility.util=ALL-UNNAMED \
                  --add-opens jdk.jdi/com.sun.jdi=ALL-UNNAMED \
                  --add-opens jdk.httpserver/com.sun.net.httpserver=ALL-UNNAMED \
                  --add-opens jdk.httpserver/com.sun.net.httpserver.spi=ALL-UNNAMED \
                  --add-opens jdk.sctp/com.sun.nio.sctp=ALL-UNNAMED \
                  --add-opens jdk.security.auth/com.sun.security.auth=ALL-UNNAMED \
                  --add-opens jdk.security.auth/com.sun.security.auth.callback=ALL-UNNAMED \
                  --add-opens jdk.compiler/com.sun.source.doctree=ALL-UNNAMED \
                  --add-opens jdk.compiler/com.sun.source.tree=ALL-UNNAMED \
                  --add-opens jdk.compiler/com.sun.source.util=ALL-UNNAMED \
                  --add-opens jdk.attach/com.sun.tools.attach=ALL-UNNAMED \
                  --add-opens jdk.attach/com.sun.tools.attach.spi=ALL-UNNAMED \
                  --add-opens jdk.compiler/com.sun.tools.javac=ALL-UNNAMED \
                  --add-opens jdk.jconsole/com.sun.tools.jconsole=ALL-UNNAMED \
                  --add-opens jdk.management/com.sun.management=ALL-UNNAMED \
                  --add-opens jdk.management.jfr/jdk.management.jfr=ALL-UNNAMED \
                  --add-opens jdk.dynalink/jdk.dynalink=ALL-UNNAMED \
                  --add-opens jdk.javadoc/jdk.javadoc.doclet=ALL-UNNAMED \
                  --add-opens jdk.jfr/jdk.jfr=ALL-UNNAMED \
                  --add-opens jdk.jfr/jdk.jfr.consumer=ALL-UNNAMED \
                  --add-opens jdk.jshell/jdk.jshell=ALL-UNNAMED \
                  --add-opens jdk.net/jdk.net=ALL-UNNAMED \
                  --add-opens jdk.net/jdk.nio=ALL-UNNAMED \
                  --add-opens jdk.nio.mapmode/jdk.nio.mapmode=ALL-UNNAMED \
                  --add-opens jdk.jartool/jdk.security.jarsigner=ALL-UNNAMED \
                  --add-opens jdk.jsobject/netscape.javascript=ALL-UNNAMED \
                  --add-opens java.base/java.lang=ALL-UNNAMED \
                  --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
                  --add-opens java.base/java.lang.invoke=ALL-UNNAMED \
                  --add-opens java.base/java.util=ALL-UNNAMED \
                  --add-opens java.base/sun.net=ALL-UNNAMED \
                  --add-opens java.base/java.math=ALL-UNNAMED \
                  --add-opens java.base/sun.reflect.annotation=ALL-UNNAMED \
                  --add-opens java.base/sun.net=ALL-UNNAMED \
                  --add-opens java.desktop/sun.awt=ALL-UNNAMED \
                  --add-opens java.desktop/sun.font=ALL-UNNAMED \
                  --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
                  --add-opens java.base/java.lang=ALL-UNNAMED \
                  --add-opens java.base/java.math=ALL-UNNAMED \
                  --add-opens java.base/java.util=ALL-UNNAMED \
                  --add-opens java.base/sun.util.calendar=ALL-UNNAMED \
                  --add-opens java.base/java.util.concurrent=ALL-UNNAMED \
                  --add-opens java.base/java.util.concurrent.locks=ALL-UNNAMED \
                  --add-opens java.base/java.security=ALL-UNNAMED \
                  --add-opens java.base/jdk.internal.loader=ALL-UNNAMED \
                  --add-opens java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED \
                  --add-opens java.base/java.net=ALL-UNNAMED \
                  --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
                  --add-opens java.management/java.lang.management=ALL-UNNAMED \
                  --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
                  --add-opens java.management/sun.management=ALL-UNNAMED \
                  --add-opens java.management/java.lang.management=ALL-UNNAMED \
                  --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
                  --add-opens java.management/sun.management=ALL-UNNAMED \
                  --add-opens java.base/java.time=ALL-UNNAMED \
                  --add-opens java.base/java.util.concurrent=ALL-UNNAMED \
                  --add-opens java.base/java.util.concurrent.locks=ALL-UNNAMED \
                  --add-opens java.base/java.security=ALL-UNNAMED \
                  --add-opens java.base/jdk.internal.loader=ALL-UNNAMED \
                  --add-opens java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED \
                  --add-opens java.base/java.net=ALL-UNNAMED \
                  --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
                  --add-opens java.base/java.lang=ALL-UNNAMED \
                  --add-opens java.base/java.math=ALL-UNNAMED \
                  --add-opens java.base/java.util=ALL-UNNAMED \
                  --add-opens java.base/sun.util.calendar=ALL-UNNAMED \
                  --add-opens java.base/sun.net.util=ALL-UNNAMED \
                  --add-opens java.base/sun.net.util=ALL-UNNAMED \
                  --add-opens java.base/java.lang=ALL-UNNAMED \
                  --add-opens java.base/java.math=ALL-UNNAMED \
                  --add-opens java.base/java.util=ALL-UNNAMED \
                  --add-opens java.base/sun.util.calendar=ALL-UNNAMED \
                  --add-opens java.base/java.util.concurrent=ALL-UNNAMED \
                  --add-opens java.base/java.util.concurrent.locks=ALL-UNNAMED \
                  --add-opens java.base/java.security=ALL-UNNAMED \
                  --add-opens java.base/jdk.internal.loader=ALL-UNNAMED \
                  --add-opens java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED \
                  --add-opens java.base/java.net=ALL-UNNAMED \
                  --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
                  --add-opens java.management/java.lang.management=ALL-UNNAMED \
                  --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
                  --add-opens java.management/sun.management=ALL-UNNAMED \
                  --add-opens java.base/sun.net.util=ALL-UNNAMED \
                  --add-opens java.base/java.time=ALL-UNNAMED \
                  --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
                  --add-opens java.base/java.io=ALL-UNNAMED \
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
                  --add-opens java.desktop/sun.awt=ALL-UNNAMED \
                  --add-exports java.desktop/sun.awt=ALL-UNNAMED \
                  --add-exports=java.desktop/sun.font=ALL-UNNAMED \
                  --add-exports=java.base/sun.reflect.generics.tree=ALL-UNNAMED \
                  --add-exports=java.base/sun.net.www.protocol.ftp=ALL-UNNAMED \
                  --add-exports=java.desktop/sun.awt=ALL-UNNAMED \
                  --add-exports=java.desktop/sun.font=ALL-UNNAMED"
                  #-javaagent:/skywalking/agent/skywalking-agent.jar"

USER root

WORKDIR /root

#RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
#    && echo $TZ > /etc/timezone \
#    && mkdir -p /root/logs/${APP_NAME} \
#    && touch /root/logs/${APP_NAME}/${APP_NAME}.jit.compile.log /root/logs/${APP_NAME}/${APP_NAME}.gc.log \
#    && sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
#    && apk add curl

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
	&& echo $TZ > /etc/timezone \
    && mkdir -p /root/logs/${APP_NAME} \
	&& touch /root/logs/${APP_NAME}/jit.compile.log /root/logs/${APP_NAME}/gc.log

VOLUME /root/logs

EXPOSE ${APP_PORT}

COPY --from=builder /builder/extracted/dependencies/ ./
RUN true
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
RUN true
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
RUN true
COPY --from=builder /builder/extracted/application/ ./
RUN true

#HEALTHCHECK --interval=120s --timeout=60s --retries=5 CMD curl -fs http://127.0.0.1:${APP_PORT}/actuator/health || exit 1

#ENTRYPOINT ["java", "-jar", "taotao-cloud-monitor-2026.10.jar"]
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTIONS} -jar taotao-cloud-monitor-2026.10.jar"]


#ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTIONS} -jar taotao-cloud-monitor-2026.10.jar"]
