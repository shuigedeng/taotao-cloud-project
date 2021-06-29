###########################################
# http://skywalking.apache.org/downloads/

cd /opt/taotao-cloud/skywalking8.6.0

# 1、官网下载skywalking服务端
wget https://mirrors.bfsu.edu.cn/apache/skywalking/8.6.0/apache-skywalking-apm-es7-8.6.0.tar.gz

# 2、上传解压 3、重命名文件夹
tar -zxvf apache-skywalking-apm-es7-8.6.0.tar.gz

# 4、修改配置文件
vim skywalking8.6.0/config/application.yml
#修改h2 为mysql
storage:
  selector: ${SW_STORAGE:mysql}
  mysql:
    properties:
      jdbcUrl: ${SW_JDBC_URL:"jdbc:mysql://127.0.0.1:3306/taotao-cloud-skywalking"}
      dataSource.user: ${SW_DATA_SOURCE_USER:root}
      dataSource.password: ${SW_DATA_SOURCE_PASSWORD:123456}
      dataSource.cachePrepStmts: ${SW_DATA_SOURCE_CACHE_PREP_STMTS:true}
      dataSource.prepStmtCacheSize: ${SW_DATA_SOURCE_PREP_STMT_CACHE_SQL_SIZE:250}
      dataSource.prepStmtCacheSqlLimit: ${SW_DATA_SOURCE_PREP_STMT_CACHE_SQL_LIMIT:2048}
      dataSource.useServerPrepStmts: ${SW_DATA_SOURCE_USE_SERVER_PREP_STMTS:true}
    metadataQueryMaxSize: ${SW_STORAGE_MYSQL_QUERY_MAX_SIZE:5000}
    maxSizeOfArrayColumn: ${SW_STORAGE_MAX_SIZE_OF_ARRAY_COLUMN:20}
    numOfSearchableValuesPerTag: ${SW_STORAGE_NUM_OF_SEARCHABLE_VALUES_PER_TAG:2}

# 5、下载mysql驱动包到 /opt/skywalking/oap-libs 目录下
cp mysql-connector-java-8.0.17.jar skywalking8.6.0/oap-libs

# 6、进入mysql 创建 taotao-cloud-skywalking 数据库

# 7、启动collector服务
#初始化
cd skywalking8.6.0/bin/
./oapServiceInit.sh

#启动collector服务
./oapService.sh

# 8、配置 Skywalking Web服务
vim skywalking8.6.0/webapp/webapp.yml
# 修改webapp.yml 文件配置如下
#默认的8080容易与其他软件冲突，建议改一下比如18080

# 9、启动web服务
cd skywalking8.6.0/bin
./webappService.sh

启动bin目录下的startup.sh可以将collector和Web模块一起启动起来。
访问http://ip:18080进入SkyWalking UI

# 9、探针配置（Agent）
vim /opt/skywalking/agent/config/agent.config
修改项目名字、日志打印级别、skywalking的服务地址


# 10、客户端启动Agent
1、基于Tomcat的服务(SpringMvc)
在tomcat的bin目录下的catalina.sh中增加如下命令行
CATALINA_OPTS="$CATALINA_OPTS -javaagent:/opt/skywalking/agent/skywalking-agent.jar"
export CATALINA_OPTS


2、基于JAR file的服务(SpringBoot)
在启动应用程序的命令行中添加 -javaagent 参数，并确保在-jar参数之前添加它，例如:
java -javaagent:/opt/skywalking/agent/skywalking-agent.jar
-Dskywalking.agent.service_name=taotao-cloud-gateway
-Dskywalking.collector.backend_service=127.0.0.1:11800
-jar yourApp.jar

##################### skywalking.sh #############################
#!/bin/bash

function start_skywalking() {
     nohup sh /opt/taotao-cloud/skywalking8.6.0/bin/startup.sh  >/opt/taotao-cloud/skywalking8.6.0/start.log 2>&1 &
     sleep 10
     echo " skywalking started"
}

function stop_skywalking() {
     ps -ef | grep skywalking|grep -v grep|awk '{print $2}' |xargs kill -9
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

