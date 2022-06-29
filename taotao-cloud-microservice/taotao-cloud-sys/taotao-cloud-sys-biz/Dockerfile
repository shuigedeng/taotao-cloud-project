FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk17:amd64 as builder

WORKDIR /root

ADD ./build/libs/taotao-cloud-sys-biz-2022.07.jar ./

RUN java -Djarmode=layertools -jar taotao-cloud-sys-biz-2022.07.jar extract

# FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-skywalking:openjdk-17-8.6.0
# FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk17:amd64
FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-skywalking:8.8.1

ARG BACKEND_SERVICE=192.168.10.200:11800
ARG AGENT_AUTHENTICATION=taotao-cloud
ARG DINGDING_TOKEN_ID=xxx
ARG DINGDING_SECRET=xxx
ARG TAOTAO_CLOUD_MAIL_PASSWORD=xxxx
ARG APP_NAME=taotao-cloud-sys
ARG APP_VERSION=2022.07
ARG JAR=${APP_NAME}-${VERSION}
ARG JAR_NAME=${JAR}.jar
ARG APP_PORT=33337
ARG PRROFILES=dev

LABEL application_name=${APP_NAME}
LABEL application_version=${APP_VERSION}
LABEL org.opencontainers.image.authors=981376577@qq.com

ENV TZ=Asia/Shanghai
ENV DINGDING_TOKEN_ID=${DINGDING_TOKEN_ID}
ENV DINGDING_SECRET=${DINGDING_SECRET}
ENV APP_NAME=${APP_NAME}
ENV VERSION=${APP_VERSION}
ENV JAR_NAME=${APP_NAME}-${VERSION}.jar
ENV BACKEND_SERVICE=${BACKEND_SERVICE}
ENV AGENT_AUTHENTICATION=${AGENT_AUTHENTICATION}
ENV TAOTAO_CLOUD_MAIL_PASSWORD=${TAOTAO_CLOUD_MAIL_PASSWORD}
ENV PRROFILES=${PRROFILES}

ENV JAVA_OPTIONS="-Xms2048m -Xmx2048m -Xmn1280m \
                  -Xss512k \
                  -XX:MaxDirectMemorySize=1024m \
                  -XX:MetaspaceSize=256m \
                  -XX:MaxMetaspaceSize=512m \
                  -XX:ReservedCodeCacheSize=256m \
                  -XX:+DisableExplicitGC \
                  -XX:MaxGCPauseMillis=50 \
                  -XX:-UseBiasedLocking \
                  -XX:+UseCountedLoopSafepoints \
                  -XX:StartFlightRecording=disk=true,maxsize=4096m,maxage=3d \
                  -XX:-OmitStackTraceInFastThrow \
                  -XX:+UnlockDiagnosticVMOptions \
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
                  --add-opens java.base/java.math=ALL-UNNAMED \
                  --add-exports java.desktop/sun.awt=ALL-UNNAMED \
                  --add-opens java.desktop/sun.awt=ALL-UNNAMED \
                  -Dspring.profiles.active=${PRROFILES} \
                  -javaagent:/skywalking/agent/skywalking-agent.jar \
                  -Dskywalking.agent.service_name=${APP_NAME} \
                  -Dskywalking.agent.authentication=${AGENT_AUTHENTICATION} \
                  -Dskywalking.logging.file_name=${APP_NAME}.skywalking.log \
                  -Dskywalking.logging.level=INFO \
                  -Dskywalking.logging.dir=/root/logs/${APP_NAME} \
                  -Dskywalking.collector.backend_service=${BACKEND_SERVICE} "

USER root

WORKDIR /root

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
	  && echo $TZ > /etc/timezone \
    && mkdir -p /root/logs/${APP_NAME} \
	  && touch /root/logs/${APP_NAME}/jit.compile.log /root/logs/${APP_NAME}/gc.log \
    && sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
    && apk add curl

# RUN apk update && apk add libreoffice
# RUN apk add --no-cache msttcorefonts-installer fontconfig
# RUN update-ms-fonts
#
# # Google fonts
# RUN wget https://github.com/google/fonts/archive/master.tar.gz -O gf.tar.gz --no-check-certificate
# RUN tar -xf gf.tar.gz
# RUN mkdir -p /usr/share/fonts/truetype/google-fonts
# RUN find $PWD/fonts-master/ -name "*.ttf" -exec install -m644 {} /usr/share/fonts/truetype/google-fonts/ \; || return 1
# RUN rm -f gf.tar.gz
# RUN fc-cache -f && rm -rf /var/cache/*

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

HEALTHCHECK --interval=120s --timeout=60s --retries=5 CMD curl -fs http://127.0.0.1:${APP_PORT}/actuator/health || exit 1

ENTRYPOINT sleep 10; java ${JAVA_OPTIONS} org.springframework.boot.loader.JarLauncher

                  #堆内存控制
#ENV JAVA_OPTIONS="-Xms2048m -Xmx2048m -Xmn1280m \
#                  # 线程栈大小控制 \
#                  -Xss512k \
#                  # 直接内存（各种 Direct Buffer）大小控制 \
#                  -XX:MaxDirectMemorySize=1024m \
#                  # 元空间控制 \
#                  -XX:MetaspaceSize=256m \
#                  # 元空间最大控制 \
#                  -XX:MaxMetaspaceSize=512m \
#                  # JIT 即时编译后（C1 C2 编译器优化）的代码占用内存 \
#                  -XX:ReservedCodeCacheSize=256m \
#                  # 关闭显示 GC（System.gc()触发的 FullGC），防止 netty 这种误检测内存泄漏显示调用 \
#                  -XX:+DisableExplicitGC \
#                  # 目标最大 STW（Stop-the-world） 时间，这个越小，GC 占用 CPU 资源，占用内存资源就越多，微服务吞吐量就越小，但是延迟低。这个需要做成可配置的 \
#                  -XX:MaxGCPauseMillis=50 \
#                  #禁用偏向锁，偏向锁其实未来会被完全移除（参考：），目前咱们都是高并发的环境，偏向锁基本没啥用并且还有负面影响 \
#                  -XX:-UseBiasedLocking \
#                  #防止大有界循环带来的迟迟不进入安全点导致 GC STW 时间过长 \
#                  -XX:+UseCountedLoopSafepoints \
#                  # 这个参数是指Java 应用程序启动 JFR 记录 \
#                  -XX:StartFlightRecording=disk=true,maxsize=4096m,maxage=3d \
#                  # 这个参数是指定每个线程的本地缓冲区大小（以字节为单位 \
#                  -XX:FlightRecorderOptions=defaultrecording=true,disk=true,maxchunksize=128m \
#                  # 禁用定时安全点任务，没必要，咱们不是那种热点代码经常改变，资源珍贵的场景，并且如果是 ZGC 本身就会定时进入安全点进行 GC 检查，更没必要了 \
#                  -XX:GuaranteedSafepointInterval=0 \
#                  # 关闭堆栈省略：这个只会省略 JDK 内部的异常，比如 NullPointerException 应用已经对于大量报错的时候输出大量堆栈导致性能压力的优化 \
#                  -XX:-OmitStackTraceInFastThrow \
#                  -XX:+UnlockDiagnosticVMOptions \
#                  -XX:+UnlockExperimentalVMOptions \
#                  -XX:SurvivorRatio=8 \
#                  -XX:+UseCompressedOops \
#                  -XX:+UseCompressedClassPointers \
#                  -XX:+SegmentedCodeCache \
#                  -XX:+PrintCommandLineFlags \
#                  -XX:+ExplicitGCInvokesConcurrent \
#                  -XX:+HeapDumpOnOutOfMemoryError \
#                  -XX:+ShowCodeDetailsInExceptionMessages \
#                  -XX:ParallelGCThreads=4 \
#                  -Xlog:async \
#                  -XX:AsyncLogBufferSize=409600 \
#                  # gc日志 \
#                  -Xlog:gc*=debug:file=/root/logs/${APP_NAME}/gc.log:utctime,level,tags:filecount=50,filesize=100M \
#                  # JIT 编译日志 \
#                  -Xlog:jit+compilation=debug:file=/root/logs/${APP_NAME}/jit.compile.log:utctime,level,tags:filecount=10,filesize=100M \
#                  # Safepoint日志 \
#                  -Xlog:safepoint=debug:file=/root/logs/${APP_NAME}/safepoint%t.log:utctime,level,tags:filecount=10,filesize=10M \
#                  -verbose:gc \
#                  # 将 DNS 缓存降低为 10s 过期，k8s 内部有很多通过域名解析的资源（通过 k8s 的 coreDNS），解析的 ip 可能会过期，漂移成新的 ip，默认的 30s 有点久，改成 10s，但是这会增加 coreDNS 的压力 \
#                  -Dnetworkaddress.cache.ttl=10 \
#                  # 更换 random 为 urandom 避免高并发加密证书通信的时候的生成随机数带来的阻塞（例如高并发 https 请求，高并发 mysql 连接通信 \
#                  -Djava.security.egd=file:/dev/./urandom \
#                  # 指定编码为 UTF-8，其实 Java 18 之后默认编码就是 UTF-8 了，这样避免不同操作系统编译带来的差异（Windows 默认是 GB2312，Linux 默认是 UTF-8 \
#                  -Dfile.encoding=utf-8 \
#                  --add-opens java.base/java.security=ALL-UNNAMED \
#                  --add-opens java.base/jdk.internal.misc=ALL-UNNAMED \
#                  --add-opens java.base/java.text=ALL-UNNAMED \
#                  --add-opens java.base/java.nio=ALL-UNNAMED \
#                  --add-opens java.base/jdk.internal.access=ALL-UNNAMED \
#                  --add-opens java.base/java.time=ALL-UNNAMED \
#                  --add-opens java.base/java.io=ALL-UNNAMED \
#                  --add-opens java.base/java.net=ALL-UNNAMED \
#                  --add-opens java.base/java.lang=ALL-UNNAMED \
#                  --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
#                  --add-opens java.base/java.util=ALL-UNNAMED \
#                  --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
#                  --add-opens java.base/sun.reflect.annotation=ALL-UNNAMED \
#                  --add-opens java.base/java.math=ALL-UNNAMED \
#                  --add-exports java.desktop/sun.awt=ALL-UNNAMED \
#                  --add-opens java.desktop/sun.awt=ALL-UNNAMED \
#                  -Dspring.profiles.active=${PRROFILES} \
#                  -javaagent:/skywalking/agent/skywalking-agent.jar \
#                  -Dskywalking.agent.service_name=${APP_NAME} \
#                  -Dskywalking.agent.authentication=${AGENT_AUTHENTICATION} \
#                  -Dskywalking.logging.file_name=${APP_NAME}.skywalking.log \
#                  -Dskywalking.logging.level=INFO \
#                  -Dskywalking.logging.dir=/root/logs/${APP_NAME} \
#                  -Dskywalking.collector.backend_service=${BACKEND_SERVICE} "
