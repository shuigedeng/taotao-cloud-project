FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:latest as builder

WORKDIR /root

ADD ./build/libs/taotao-cloud-sys-2026.03.jar ./

RUN java -Djarmode=layertools -jar taotao-cloud-sys-2026.03.jar extract

# FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-skywalking:openjdk-17-8.6.0
# FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:latest
#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:latest
#FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:skywalking9.1.0-pyroscope0.13.0-jre-font
FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk25:latest

ARG SKYWALKING_BACKEND_SERVICE=127.0.0.1:11800
ARG SKYWALKING_AGENT_AUTHENTICATION=taotao-cloud
ARG DINGDING_TOKEN_ID=xxx
ARG DINGDING_SECRET=xxx
ARG TAOTAO_CLOUD_MAIL_PASSWORD=xxxx
ARG APP_NAME=taotao-cloud-sys
ARG APP_VERSION=2026.03
ARG APP_JAR_NAME=${APP_NAME}-${APP_VERSION}.jar
ARG APP_PORT=33421
ARG APP_PRROFILES=dev
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
ENV APP_PRROFILES=${APP_PRROFILES}
ENV REQUEST_API_DOCS=${REQUEST_API_DOCS}
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
ENV PYROSCOPE_LABELS="APP=taotao-cloud-sys"
#ENV PYROSCOPE_AUTH_TOKEN=info

ENV JAVA_OPTIONS="-Xms1g \
                  -Xmx2g \
                  -Xmn1280m \
                  -Xss512k \
                  -Xlog:async \
                  -verbose:gc \
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
                  -Xlog:gc*=debug:file=/root/logs/${APP_NAME}/gc.log:utctime,level,tags:filecount=50,filesize=100M \
                  -Xlog:jit+compilation=debug:file=/root/logs/${APP_NAME}/jit.compile.log:utctime,level,tags:filecount=10,filesize=100M \
                  -Xlog:safepoint=debug:file=/root/logs/${APP_NAME}/safepoint%t.log:utctime,level,tags:filecount=10,filesize=10M \
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

#######################################################################################################
#远程debug选项 只能在测试环境使用
#1.使用容器部署时 ENTRYPOINT后面加上DEBUG_OPTIONS
#2.使用jar部署时 java -XDebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8889 -jar demo
EXPOSE 8889
ENV DEBUG_OPTIONS = "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8889 -XDebug "

USER root

WORKDIR /root

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && mkdir -p /root/logs/${APP_NAME} \
    && touch /root/logs/${APP_NAME}/jit.compile.log /root/logs/${APP_NAME}/gc.log

#https://mirrors.aliyun.com/alpine/v3.14/main
#https://mirrors.ustc.edu.cn/alpine/v3.14/main/
#清华TUNA镜像源 https://mirror.tuna.tsinghua.edu.cn/alpine/v3.14/main
RUN echo 'http://mirrors.aliyun.com/alpine/v3.14/main' > /etc/apk/repositories \
    && echo 'http://mirrors.aliyun.com/alpine/v3.14/community' >>/etc/apk/repositories \
    && set -eux \
    && apk update --allow-untrusted \
    && apk add curl --allow-untrusted \
    && apk add libstdc++ --allow-untrusted \
    && apk add --no-cache --update fontconfig ttf-dejavu \
    && apk add --no-cache --update tzdata

# 添加 Arthas Toolkit
#COPY --from=registry.cn-huhehaote.aliyuncs.com/jokerbai/arthas:latest /opt/arthas /opt/arthas
# 添加 Skywalking Agent
#COPY --from=registry.cn-huhehaote.aliyuncs.com/jokerbai/skywalking-agent-sidecar:8.1.0-es7 /usr/skywalking/agent /opt/skywalking/agent

# RUN apk update && apk agdd libreoffice
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

#docker inspect --format='{{json .State.Health}}' file
#interval: 30s #运行间隔时间(ms|s|m|h)(缺省为0s)
#timeout: 20s #一次检查运行的最大时间(ms|s|m|h)(默认为0s)
#retries: 3   #连续失败次数后报告不健康
#start_period: 10s #容器初始化的起始周期(ms|s|m|h)(默认0)
#HEALTHCHECK --interval=60s --timeout=30s --retries=5 CMD curl -fs http://127.0.0.1:${APP_PORT}/actuator/health || exit 1
#使用healthcheck.sh
HEALTHCHECK --interval=60s --timeout=30s --retries=5 CMD curl -fs http://127.0.0.1:${APP_PORT}/actuator/health || /bin/sh -c 'kill -s 15 -1 && (sleep 10; kill -s 9 -1)'

#HEALTHCHECK --start-period=60s --interval=10s --timeout=3s --retries=50 CMD /bin/sh /healthcheck.sh

ENTRYPOINT sleep 30; java ${JAVA_OPTIONS} org.springframework.boot.loader.launch.JarLauncher


#######################################################################################################
#JVM 包含多种不同类型的参数选项、
#-- 用来设置应用参数，
#-D 用来设置系统属性，属于标准选项
#-X 非标准参数，支持的选项范围跟具体的 JVM 实现有关
#-XX 非标准参数，设置高级选项，允许开发者调整 JVM 的行为、性能、输出调试信息，支持的选项范围也跟具体的 JVM 实现有关
#布尔类型的高级选项是起到功能的开关作用，不带参数。使用 + 启用功能，使用 - 禁用功能；对于带参数的高级选项，需要指定参数值
#使用 java -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+PrintFlagsFinal -version 命令可以查看 JVM 所有的选项

#1.标准参数
#-client和-server：设置JVM使用client模式或server模式。client模式启动快，但性能和内存管理效率不高，适用于桌面应用；
#server模式启动慢，但性能和内存管理效率高，适用于服务器。64位的JVM只有server模式
#-verbose：查询GC（垃圾回收）问题最常用的命令之一，常用于排查GC问题。
#例如，-verbose:gc输出每次GC的相关情况，-verbose:class输出JVM载入类的相关信息，-verbose:jni输出native方法调用的相关情况
#     启动 Java 应用时，JVM 会在每次发生垃圾回收时输出一行或多行日志
#
#2.非标准参数（-X）
#-Xms：设置初始堆内存大小，即程序启动时占用内存大小，默认是物理内存的1/64。
#-Xmx：设置最大堆内存大小，即程序运行期间最大可占用的内存大小。如果程序运行需要占用更多的内存，超出了这个设置值，就会抛出OutOfMemory异常，默认是物理内存的1/4。
#-Xmn：设置年轻代大小，增大年轻代会减少老年代大小，官网推荐整堆的3/8。
#-Xss：设置单个线程栈内存大小，默认是1MB。
#
#3.非标准参数（-XX） 高级选项
#	性能参数：
#		-XX:+UseSerialGC：使用串行垃圾收集器。
#		-XX:+UseParallelGC：使用并行垃圾收集器（年轻代）。
#		-XX:+UseParallelOldGC：使用并行垃圾收集器（老年代），JDK6.0以上支持。
#		-XX:+UseConcMarkSweepGC：使用CMS（Concurrent Mark Sweep）垃圾收集器，适用于多CPU，并要求缩短因GC造成程序停滞的时间。
#		-XX:+UseG1GC：使用G1垃圾收集器。
#		-XX:+HeapDumpOnOutOfMemoryError：这个参数告诉JVM在内存溢出错误发生时生成堆转储文件。
#		-XX:HeapDumpPath={HEAP-DUMP-FILE-PATH} ：这个参数指定了堆转储文件的存储路径。需要将{HEAP-DUMP-FILE-PATH}替换为实际的文件路径。
#	行为参数：
#		-XX:NewRatio：设置年轻代（包括Eden和两个Survivor区）与老年代的比值（除去持久代）。例如，设置为4，则年轻代与老年代所占比值为1:4。
#		-XX:SurvivorRatio：设置年轻代中Eden区与Survivor区的大小比值。例如，设置为4，则两个Survivor区与一个Eden区的比值为2:4。
#		-XX:MaxTenuringThreshold：设置垃圾最大年龄。如果设置为0，则年轻代对象不经过Survivor区，直接进入老年代。
#		-XX:PermSize 和 -XX:MaxPermSize：设置持久代（PermGen space）的初始大小和最大大小。
#			不过，在Java 8中，PermGen space被元空间（Metaspace）所替代，相关参数变为-XX:MetaspaceSize和-XX:MaxMetaspaceSize。
#	调试参数：
#		-XX:+PrintGC：每次触发GC时打印相关日志。
#		-XX:+PrintGCDetails：打印更详细的GC日志。
#		-XX:+PrintGCTimeStamps：打印GC发生的时间戳。
#		-Xloggc:filename：将GC日志输出到指定文件。例如，-Xloggc:/data/gclog/gc.log表示将GC日志输出到指定路径。
#		-XX:+DisableExplicitGC：禁用System.gc()调用，防止显式触发Full GC。
#		
#	辅助指令jinfo
#		jinfo：查看或修改正在运行的JVM配置。例如，jinfo -flag
#		HeapDumpOnOutOfMemoryError 870778用于查看进程号为870778的JVM是否开启了HeapDumpOnOutOfMemoryError配置。	
#		
#		
#


#ENV JAVA_OPTIONS="
#########################################基本参数##########################
#                  -Dfile.encoding=utf-8 #指定编码为 UTF-8，其实 Java 18 之后默认编码就是 UTF-8 了，这样避免不同操作系统编译带来的差异（Windows 默认是 GB2312，Linux 默认是 UTF-8\
#                  -Xms2048m #JVM初始堆大小，单位有m（mb），g(gb)等
#                  -Xmx2048m #最大堆，单位有m（mb），g(gb)等
#                  -Xmn1280m  #新生代大小，通常为xmx的1/3，如果生成过快，基本上不进入老年代，建议调大新生代（特别是并发量比较高的系统）
#                  -Xss512k #栈大小（栈空间属于线程私有，栈设置的越大，最后容纳的线程就越少，不同平台默认栈大小不一样，linux/x64:1m ）如果没有特别深的调用,一般256就够了，\
#                  -XX:+PrintGCDetails #输出详细的垃圾回收日志
#                  -XX:MetaspaceSize=256m #元空间大小，初始和最大不要一样，如果一样，最后回收成本较大，只要没有动态生成类功能，建议小一些 达到会触发fullgc，虽然fullgc回收元空间的成本比较大 \
#                  -XX:MaxMetaspaceSize=512m #最大元空间大小
#                  -XX:MinMetaspaceFreeRatio=50 #设置Metaspace GC后空闲空间的最小比例，控制Metaspace的增长速度
#                  -XX:MaxMetaspaceFreeRatio=50 #设置Metaspace GC后空闲空间的最大比例，控制Metaspace的释放
#                  -XX:MaxMetaspaceExpansion=50 #设置Metaspace增长时的最大幅度
#                  -XX:PretenureSizeThresold #进入老年代的阈值
#                  -XX:+DisableExplicitGC 关#闭手动gc（系统里的System.gc()）
#                  -XX:NewRatio=4  #新生代和老年代的占比，表示新生代:老年代 = 1:4 即老年代占整个堆的4/5；默认值=2
#                  -XX:SurvivorRatio=8  #survivor区和Eden区大小比率,比如：S区和Eden区占新生代比率为1:6,两个S区2:6
#                  -XX:MaxHeapFreeRatio=70 #缩小堆内存的时机,堆内存使用率大于70时扩张堆内存，xms=xmx时该参数无效，默认值70
#                  -XX:MinHeapFreeRatio=40 #扩张堆内存的时机,堆内存使用率小于40时缩减堆内存，xms=xmx时该参数无效，默认值40
#                  -XX:MaxDirectMemorySize=1024m  #设置直接内存，不设置与Java堆内存最大值一致\
#                  -XX:MaxGCPauseMillis=50 #目标最大 STW（Stop-the-world） 时间，这个越小，GC 占用 CPU 资源，占用内存资源就越多，微服务吞吐量就越小，但是延迟低。这个需要做成可配置的\
#                  -XX:-UseBiasedLocking #禁用偏向锁，偏向锁其实未来会被完全移除（参考：），目前咱们都是高并发的环境，偏向锁基本没啥用并且还有负面影\
#                  -XX:+UseCountedLoopSafepoints #防止大有界循环带来的迟迟不进入安全点导致 GC STW 时间过长\
#                  -XX:StartFlightRecording=disk=true,maxsize=4096m,maxage=3d #这个参数是指Java 应用程序启动 JFR 记录 \
#                  -XX:FlightRecorderOptions=defaultrecording=true,disk=true,maxchunksize=128m  #这个参数是指定每个线程的本地缓冲区大小（以字节为单位\
#                  -XX:GuaranteedSafepointInterval=0 #禁用定时安全点任务，没必要，咱们不是那种热点代码经常改变，资源珍贵的场景，并且如果是 ZGC 本身就会定时进入安全点进行 GC 检查，更没必要了\
#                  -XX:-OmitStackTraceInFastThrow #关闭堆栈省略：这个只会省略 JDK 内部的异常，比如 NullPointerException 应用已经对于大量报错的时候输出大量堆栈导致性能压力的优化\
#                  -XX:+UnlockDiagnosticVMOptions \
#                  -XX:+UseCompressedOops \
#                  -XX:+UseCompressedClassPointers \
#                  -XX:+SegmentedCodeCache \
#                  -XX:+PrintCommandLineFlags \
#                  -XX:+ExplicitGCInvokesConcurrent \
#                  -XX:+ShowCodeDetailsInExceptionMessages \
#                  -Dnetworkaddress.cache.ttl=10 #将 DNS 缓存降低为 10s 过期，k8s 内部有很多通过域名解析的资源（通过 k8s 的 coreDNS），解析的 ip 可能会过期，漂移成新的 ip，默认的 30s 有点久，改成 10s，但是这会增加 coreDNS 的压力 \\
#                  -Djava.security.egd=file:/dev/./urandom 更换 random #为 urandom 避免高并发加密证书通信的时候的生成随机数带来的阻塞（例如高并发 https 请求，高并发 mysql 连接通信\
#########################################系统级参数##########################
#                  -verbose:class 显示类加载的信息（查看加载的先后顺序） \
#                  -Dproperty=value 设置属性4值例如：-Dkey="value" \
#                  -XX:+UnlockExperimentalVMOptions 允许使用实验性参数比如在jdk14中使用zgc\
#                  -XX:ErrorFile=${LOGDIR}/hs_err_%p.log 配置jvm crash 输出路径，一般分析jvm hang住的时候比较有用\
##                 -XX:+HeapDumpOnOutOfMemoryError 开启oom生成堆栈信息\
#                  -XX:+HeapDumpBeforeFullGC 在fullgc之前进行堆输出
#                  -XX:+HeapDumpAfterFullGC 在fullgc之后进行堆输出
#                  -XX:HeapDumpPath=/opt/apps/errorDump.hprof 输出文件 \
#                  -XX:+UseTLAB 使用并行线程创建对象
#                  -XX:TLABSize=size 设置TLAB的空间大小
#                  -XX:+DisableExplicitGC 禁止System.gc()如果后台跑批可以开启,关闭显示GC(System.gc())触发的FullGC，防止netty这种误检测内存泄漏显示调用 \
#########################################日志输出参数##########################
#                  -Xlog:async \
                  -XX:+UseContainerSupport \
                  -XX:MaxRAMPercentage=75 \
#                  -XX:AsyncLogBufferSize=409600 \
#                  -verbose:gc 显示每次垃圾事件的信息\
#                  -XX:+PrintGCTimeStamps  打印gc时间戳
#                  -XX:+PrintGCDateStamps
#                  -XX:+PrintGCApplicationStoppedTime 打印gc停顿时间
#                  -XX:+PrintHeapAtGC 打印出jvm堆内存每个区域的使用情况
#                  -XX:-UseGCLogFileRotation 打印引用暂停时间
#                  -XX:+PrintTenuringDistribution 打印存活实例年龄信息
#                  -Xlog:gc*=debug:file=/root/logs/${APP_NAME}/gc.log:utctime,level,tags:filecount=50,filesize=100M  gc基本信息日志\
#                  -Xlog:jit+compilation=debug:file=/root/logs/${APP_NAME}/jit.compile.log:utctime,level,tags:filecount=10,filesize=100M JIT 编译日志  \
#                  -Xlog:safepoint=debug:file=/root/logs/${APP_NAME}/safepoint%t.log:utctime,level,tags:filecount=10,filesize=10M Safepoint日志 \
#########################################编译参数配置##########################
#                  -XX:+TieredCompilation 开启分层编译，JDK8之后默认开启
#                  -XX:+CICompilerCount=N 编译线程数，设置数量后，JVM会自动分配线程数，C1:C2 = 1:2
#                  -XX:TierXBackEdgeThreshold OSR编译的阈值
#                  -XX:TierXMinInvocationThreshold 开启分层编译后各层调用的阈值
#                  -XX:TierXCompileThreshold 开启分层编译后的编译阈值
#                  -XX:ReservedCodeCacheSize=128m
#                  -XX:InitialCodeCacheSize=128m 即时编译缓存大小，一般情况下不用修改，除非系统内即时编译的内容比较多
#                  -XX:+UnlockDiagnosticVMOptions
#                  -XX:+PrintCompilation
#                  -XX:+PrintInlining
#                  -XX:+PrintCodeCache
#                  -XX:+PrintCodeCacheOnCompilation
#                  -XX:+TraceClassLoading
#                  -XX:+LogCompilation
#                  -XX:LogFile= filename 输出编译、内联、codeCache信息到文件。可以使用JITwatch的工具来分析编译日志。JITwatch首页的Open Log选中日志文件，点击Start就可以开始分析日志
#                  -XX:ReservedCodeCacheSize=256m  JIT 即时编译后（C1 C2 编译器优化）的代码占用内存\
#########################################gc策略配置##########################
#                  -XX:ParallelGCThreads=4 设置用于垃圾回收的线程数。通常情况下可以和 CPU 数量相等。但在 CPU 数量比较多的情况下，设置相对较小的数值也是合理的\
#                  -XX:PretenureSizeThreshold=1000000  新生代可容纳的最大对象，大于此值的对象直接会分配到老年代，设置为0则没有限制 避免在Eden区和Survivor区发生大量的内存复制，该参数只对Serial和ParNew收集器有效，Parallel Scavenge并不认识该参数
#                  -XX:ConcGCThreads=4 并发gc的线程数，默认根据cpu数量订
#                  -XX:InitialTenuringThreshol=7 进入老年代最小的GC年龄，年轻代对象转换为老年代对象最小年龄值，默认值7，对象在坚持过一次Minor GC之后，年龄就加1，每个对象在坚持过一次Minor GC之后，年龄就增加1
#########################################cms垃圾收集器##########################
#                   -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode 开启cms
#                   -XX:+UseConcMarkSweepGC 新生代使用并行收集器，老年代使用 CMS+串行收集器。
#                   -XX:+CMSIncrementalMode 使用增量模式，比较适合单 CPU。
#                   -XX:+ParallelCMSThreads=2 设定 CMS 的线程数量
#                   -XX:+CMSInitiatingOccupancyFraction 设置 CMS 收集器在老年代空间被使用多少后触发，默认为 68%
#                   -XX:+UseFullGCsBeforeCompaction 设定进行多少次 CMS 垃圾回收后，进行一次内存压缩（慎用，如果频繁fullgc会严重影响性能）
#                   -XX:+CMSClassUnloadingEnabled 允许对类元数据进行回收
#                   -XX:+CMSParallelRemarkEndable 启用并行重标记
#                   -XX:CMSInitatingPermOccupancyFraction 当永久区占用率达到这一百分比后，启动 CMS 回收 (前提是-XX:+CMSClassUnloadingEnabled 激活了)
#                   -XX:UseCMSInitatingOccupancyOnly 表示只在到达阈值的时候，才进行 CMS 回收
#                   -XX:+UseCMSCompactAtFullCollection 以下两个参数会减少垃圾碎片,开启CMS的压缩
#                   -XX:CMSFullGCsBeforeCompaction=2 默认为0，指经过多少次CMS FullGC才进行压缩
#                   -XX:CMSInitiatingOccupancyFraction 让老年代占用率达到该值就进行CMS GC，以降低浮动垃圾占满老年代的问题 默认92% 可以降低这个值，
#########################################G1垃圾收集器##########################
#                   -XX:+UseG1GC 使用 G1 回收器
#                   -XX:G1HeapRegionSize  手动指定region大小 region的大小必须是2的倍数   region 最多2048个，堆/2048≈ region大小
#                   -XX:G1NewSizePercent 手动指定新生代的占比,默认占堆的5%
#                   -XX:G1MaxNewSizePercent 手动指定新生代的最大占比，默认不超过60%，达到阈值就youngGc
#                   -XX:InitiatingHeapOccupancyPercent 设置老年代占整个堆的比例，默认是45%，老年代达到阈值就触发Mixed GC （年轻代和老年代都回收）
#                   -XX:+MaxGCPauseMills=200 设置最大垃圾收集停顿时间  g1的一大好处
#                   -XX:+GCPauseIntervalMills 设置停顿间隔时间
#########################################ZGC垃圾收集器##########################
#                   看情况，jdk15以后zgc就正式发布了
#                   -XX:+UnlockExperimentalVMOptions #来动态感知容器的Cgroups内存限制，最大堆内存为Cgroups内存限制的1/4
#                   -XX:+UseCGroupMemoryLimitForHeap #来动态感知容器的Cgroups内存限制，最大堆内存为Cgroups内存限制的1/4
#                   -XX:MaxRAMFraction=2 #参数值必须为整数 1->90% 2->50% 3->33% 4->25%
#                   -XX:MinRAMFraction=2 #参数值必须为整数 1->90% 2->50% 3->33% 4->25%
#                   -XX:+UseZGC #开启ZGC
#                   -XX:ConcGCThreads=2 #基准测试中的32和服务器，128G堆的情况下，配置的ConcGCThread是4，配置的ParallelGCThreads是20 并行回收垃圾的线程数（默认是12.5%，8核以下默认是1），调大后垃圾回收速度加快，但是抢占cpu资源，可能导致用户线程抢不到cpu资源
#                   -XX:ParallelGCThreads=6 #STW阶段使用线程数，默认是总核数的60%
#                   -XX:ZCollectionInterval=120 #ZGC发生的最小时间间隔，单位秒,并发量大的时候，可以把这个参数调小，主动回收垃圾
#                   -XX:ZAllocationSpikeTolerance=5 #ZGC触发自适应算法的修正系数，默认2，数值越大，越早的触发ZGC
#                   -XX:-ZProactive #是否启用主动回收，默认开启，这里的配置表示关闭
#                   -Xlog:safepoint,classhisto*=trace,age*,gc*=info:file=/app/userApi/logs/gc/gc-%t.log:time,tid,tags:filecount=20,filesize=100m 设置GC日志中的内容、格式、位置以及每个日志的大小
#                   -XX:+UseNUMAZGC 默认是开启支持NUMA的，不过，如果JVM探测到系统绑定的是CPU子集，就会自动禁用NUMA
#                   -XX:+UseNUMA 通过这个参数显示是启用还是禁用了NUMA（前提是操作系统支持NUMA，linux内核2.5以后才支持）
#                   -XX:ZUncommit 默认情况下ZGC会把不再使用的内存归还给操作系统（如果Xms和Xmx配置一致，该参数无意义）
#                   -XX:ZUncommitDelay 表示延迟多少秒jvm归还占用的内存资源给操作系统
#                   -XX:MaxTenuringThreshold=15 对象多少次晋升到老年代的参数，默认15次，有些垃圾回收器会自优化,并发量较大适当减小（只针对分代垃圾回收器有用）
#########################################其他配置##########################
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
#########################################agent配置##########################
#                  -Dspring.profiles.active=${PRROFILES} \
#                  -javaagent:/skywalking/agent/skywalking-agent.jar \
#                  -Dskywalking.agent.service_name=${APP_NAME} \
#                  -Dskywalking.agent.authentication=${AGENT_AUTHENTICATION} \
#                  -Dskywalking.logging.file_name=skywalking.log \
#                  -Dskywalking.logging.level=INFO \
#                  -Dskywalking.logging.dir=/root/logs/${APP_NAME} \
#                  -Dskywalking.collector.backend_service=${BACKEND_SERVICE} "










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
                  -XX:+UseContainerSupport \
                  -XX:MaxRAMPercentage=75 \
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
#                  -Dskywalking.logging.file_name=skywalking.log \
#                  -Dskywalking.logging.level=INFO \
#                  -Dskywalking.logging.dir=/root/logs/${APP_NAME} \
#                  -Dskywalking.collector.backend_service=${BACKEND_SERVICE} "
