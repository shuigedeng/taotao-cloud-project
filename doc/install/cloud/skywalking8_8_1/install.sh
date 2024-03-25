###########################################
# http://skywalking.apache.org/downloads/
# http://archive.apache.org/dist/skywalking/

cd /opt/soft/


# 1、官网下载skywalking服务端
wget https://mirrors.bfsu.edu.cn/apache/skywalking/9.1.0/apache-skywalking-apm-9.1.0.tar.gz

# 2、上传解压 3、重命名文件夹
tar -zxvf apache-skywalking-apm-9.1.0.tar.gz -C /opt/cloud

cd /opt/cloud/apache-skywalking-apm-bin

# 4、修改配置文件
vim config/application.yml

storage:
  #修改h2 为mysql
  selector: ${SW_STORAGE:mysql}
  mysql:
    properties:
      #修改jdbcUrl
      jdbcUrl: ${SW_JDBC_URL:"jdbc:mysql://127.0.0.1:3306/taotao-cloud-skywalking?rewriteBatchedStatements=true"}
      #修改user
      dataSource.user: ${SW_DATA_SOURCE_USER:root}
      #修改password
      dataSource.password: ${SW_DATA_SOURCE_PASSWORD:123456}
      dataSource.cachePrepStmts: ${SW_DATA_SOURCE_CACHE_PREP_STMTS:true}
      dataSource.prepStmtCacheSize: ${SW_DATA_SOURCE_PREP_STMT_CACHE_SQL_SIZE:250}
      dataSource.prepStmtCacheSqlLimit: ${SW_DATA_SOURCE_PREP_STMT_CACHE_SQL_LIMIT:2048}
      dataSource.useServerPrepStmts: ${SW_DATA_SOURCE_USE_SERVER_PREP_STMTS:true}
    metadataQueryMaxSize: ${SW_STORAGE_MYSQL_QUERY_MAX_SIZE:5000}
    maxSizeOfArrayColumn: ${SW_STORAGE_MAX_SIZE_OF_ARRAY_COLUMN:20}
    numOfSearchableValuesPerTag: ${SW_STORAGE_NUM_OF_SEARCHABLE_VALUES_PER_TAG:2}

receiver-sharing-server:
  selector: ${SW_RECEIVER_SHARING_SERVER:default}
  default:
    # For Jetty server
    restHost: ${SW_RECEIVER_SHARING_REST_HOST:0.0.0.0}
    restPort: ${SW_RECEIVER_SHARING_REST_PORT:0}
    restContextPath: ${SW_RECEIVER_SHARING_REST_CONTEXT_PATH:/}
    restMinThreads: ${SW_RECEIVER_SHARING_JETTY_MIN_THREADS:1}
    restMaxThreads: ${SW_RECEIVER_SHARING_JETTY_MAX_THREADS:200}
    restIdleTimeOut: ${SW_RECEIVER_SHARING_JETTY_IDLE_TIMEOUT:30000}
    restAcceptorPriorityDelta: ${SW_RECEIVER_SHARING_JETTY_DELTA:0}
    restAcceptQueueSize: ${SW_RECEIVER_SHARING_JETTY_QUEUE_SIZE:0}
    # For gRPC server
    gRPCHost: ${SW_RECEIVER_GRPC_HOST:0.0.0.0}
    gRPCPort: ${SW_RECEIVER_GRPC_PORT:0}
    maxConcurrentCallsPerConnection: ${SW_RECEIVER_GRPC_MAX_CONCURRENT_CALL:0}
    maxMessageSize: ${SW_RECEIVER_GRPC_MAX_MESSAGE_SIZE:0}
    gRPCThreadPoolQueueSize: ${SW_RECEIVER_GRPC_POOL_QUEUE_SIZE:0}
    gRPCThreadPoolSize: ${SW_RECEIVER_GRPC_THREAD_POOL_SIZE:0}
    gRPCSslEnabled: ${SW_RECEIVER_GRPC_SSL_ENABLED:false}
    gRPCSslKeyPath: ${SW_RECEIVER_GRPC_SSL_KEY_PATH:""}
    gRPCSslCertChainPath: ${SW_RECEIVER_GRPC_SSL_CERT_CHAIN_PATH:""}
    #修改authentication
    authentication: ${SW_AUTHENTICATION:"taotao-cloud"}

# 5、下载mysql驱动包到 /opt/skywalking/oap-libs 目录下
cp mysql-connector-java-8.0.20.jar /opt/cloud/apache-skywalking-apm-bin/oap-libs

# 6、进入mysql 创建 cloud-skywalking 数据库
mysql –uroot –p
create database `taotao-cloud-skywalking`;

# 7、启动collector服务
#初始化
/opt/cloud/apache-skywalking-apm-bin/bin/oapServiceInit.sh

#启动collector服务
/opt/cloud/apache-skywalking-apm-bin/bin/oapService.sh

# 8、配置 Skywalking Web服务
vim /opt/cloud/apache-skywalking-apm-bin/webapp/webapp.yml
# 修改webapp.yml 文件配置如下
#默认的8080容易与其他软件冲突，建议改一下比如 8085

# 9、启动web服务
/opt/cloud/apache-skywalking-apm-bin/bin/webappService.sh

启动bin目录下的startup.sh可以将collector和Web模块一起启动起来。
http://192.168.10.220:8085进入SkyWalking UI


# 9、探针配置（Agent）
vim skywalking9.1.0/agent/config/agent.config
修改项目名字、日志打印级别、skywalking的服务地址

# 10、客户端启动Agent
1、基于Tomcat的服务(SpringMvc)
在tomcat的bin目录下的catalina.sh中增加如下命令行
CATALINA_OPTS="$CATALINA_OPTS -javaagent:/opt/skywalking/agent/skywalking-agent.jar"
export CATALINA_OPTS

2、基于JAR file的服务(SpringBoot)
在启动应用程序的命令行中添加 -javaagent 参数，并确保在-jar参数之前添加它，例如:
java -javaagent:/Users/shuigedeng/software/skywalking/agent/skywalking-agent.jar \
-Dskywalking.agent.service_name=cloud-gateway \
-Dskywalking.agent.authentication=cloud \
-Dskywalking.logging.file_name=cloud-gateway.skywalking.log \
-Dskywalking.logging.level=INFO \
-Dskywalking.logging.dir=../logs/application/cloud-gateway \
-Dskywalking.collector.backend_service=xxx:11800 \
-jar cloud-gateway-1.3.0.jar

##################### skywalking.sh #############################
#!/bin/bash

function start_skywalking() {
     nohup sh /opt/cloud/apache-skywalking-apm-bin-es7/bin/startup.sh \
      >/opt/cloud/apache-skywalking-apm-bin-es7/start.log 2>&1 &
     sleep 10
     echo "skywalking started"
}

function stop_skywalking() {
     ps -ef | grep apache-skywalking-apm-bin-es7|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "skywalking stoped"
}

case $1 in
"start")
    start_skywalking
    ;;
"stop")
    stop_skywalking
    ;;
"restart")
    stop_skywalking
    sleep 15
    start_skywalking
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac

