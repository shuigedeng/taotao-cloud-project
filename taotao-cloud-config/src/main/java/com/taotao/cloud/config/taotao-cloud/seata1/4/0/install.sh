###########################################
wget https://github.com/seata/seata/releases/download/v1.4.0/seata-server-1.4.0.zip

unzip seata-server-1.4.0.zip
cp -r seata /root/taotao-cloud/

mv /root/taotao-cloud/seata /root/taotao-cloud/seata1.4.0

cd /root/taotao-cloud/seata1.4.0/conf

# 获取seata源码 后面要用
cd /root/github
git clone https://github.com/seata/seata.git

#
# cd seata
# mvn -Prelease-all -DskipTests clean install -U

# 导入配置

vim registry.conf
# registry {
#   type = "nacos"
#   nacos {
#     application = "seata-server"
#     serverAddr = "127.0.0.1:8848"
#     group = "SEATA_GROUP"
#     namespace = "public"
#     cluster = "default"
#     username = "nacos"
#     password = "nacos"
#   }
# }
# config {
#   type = "nacos"
#   nacos {
#     serverAddr = "127.0.0.1:8848"
#     namespace = "public"
#     group = "SEATA_GROUP"
#     username = "nacos"
#     password = "nacos"
#   }
# }

cd conf
cp /root/github/seata/script/config-center/config.txt .
cp -r /root/github/seata/script/config-center/nacos .

vim config.txt
# transport.type=TCP
# transport.server=NIO
# transport.heartbeat=true
# transport.enableClientBatchSendRequest=false
# transport.threadFactory.bossThreadPrefix=NettyBoss
# transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
# transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
# transport.threadFactory.shareBossWorker=false
# transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
# transport.threadFactory.clientSelectorThreadSize=1
# transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
# transport.threadFactory.bossThreadSize=1
# transport.threadFactory.workerThreadSize=default
# transport.shutdown.wait=3
# 修改为 my_test_tx_group
# service.vgroupMapping.my_test_tx_group=default
# 修改为 192.168.1.5
# service.default.grouplist=192.168.1.5:8091
# service.enableDegrade=false
# service.disableGlobalTransaction=false
# client.rm.asyncCommitBufferLimit=10000
# client.rm.lock.retryInterval=10
# client.rm.lock.retryTimes=30
# client.rm.lock.retryPolicyBranchRollbackOnConflict=true
# client.rm.reportRetryCount=5
# client.rm.tableMetaCheckEnable=false
# client.rm.sqlParserType=druid
# client.rm.reportSuccessEnable=false
# client.rm.sagaBranchRegisterEnable=false
# client.tm.commitRetryCount=5
# client.tm.rollbackRetryCount=5
# client.tm.defaultGlobalTransactionTimeout=60000
# client.tm.degradeCheck=false
# client.tm.degradeCheckAllowTimes=10
# client.tm.degradeCheckPeriod=2000
# 修改为db
# store.mode=db
# store.publicKey=
# store.file.dir=file_store/data
# store.file.maxBranchSessionSize=16384
# store.file.maxGlobalSessionSize=512
# store.file.fileWriteBufferCacheSize=16384
# store.file.flushDiskMode=async
# store.file.sessionReloadReadSize=100
# store.db.datasource=druid
# store.db.dbType=mysql
# 修改为com.mysql.cj.jdbc.Driver
# store.db.driverClassName=com.mysql.cj.jdbc.Driver
# 修改为192.168.1.5
# store.db.url=jdbc:mysql://192.168.1.5:3306/seata?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
# 修改为root
# store.db.user=root
# 修改为password
# store.db.password=123456
# store.db.minConn=5
# store.db.maxConn=30
# store.db.globalTable=global_table
# store.db.branchTable=branch_table
# store.db.queryLimit=100
# store.db.lockTable=lock_table
# store.db.maxWait=5000
# store.redis.mode=single
# store.redis.single.host=127.0.0.1
# store.redis.single.port=6379
# store.redis.maxConn=10
# store.redis.minConn=1
# store.redis.maxTotal=100
# store.redis.database=0
# store.redis.password=
# store.redis.queryLimit=100
# server.recovery.committingRetryPeriod=1000
# server.recovery.asynCommittingRetryPeriod=1000
# server.recovery.rollbackingRetryPeriod=1000
# server.recovery.timeoutRetryPeriod=1000
# server.maxCommitRetryTimeout=-1
# server.maxRollbackRetryTimeout=-1
# server.rollbackRetryTimeoutUnlockEnable=false
# client.undo.dataValidation=true
# client.undo.logSerialization=jackson
# client.undo.onlyCareUpdateColumns=true
# server.undo.logSaveDays=7
# server.undo.logDeletePeriod=86400000
# client.undo.logTable=undo_log
# client.undo.compress.enable=true
# client.undo.compress.type=zip
# client.undo.compress.threshold=64k
# log.exceptionRate=100
# transport.serialization=seata
# transport.compressor=none
# metrics.enabled=false
# metrics.registryType=compact
# metrics.exporterList=prometheus
# metrics.exporterPrometheusPort=9898

# 同步配置文件到nacos
cd nacos
sh nacos-config.sh -h localhost -p 8848 -g SEATA_GROUP -u nacos -w nacos

# 在数据库中执行 sql文件
cp /root/github/seata/script/server/db/mysql.sql /root/taotao-cloud/seata1.4.0/conf/
mysql –uroot –p123456 -Dseata</taotao-cloud/seata/conf/mysql.sql

# 在业务系统中执行 sql文件
cp /root/github/seata/script/client/at/db/mysql.sql /root/taotao-cloud/seata1.4.0/conf/client-mysql.sql


##################### seata.sh #############################
#!/bin/bash

function start_seata() {
     nohup sh /root/taotao-cloud/seata1.4.0/bin/seata-server.sh -p 8091 -h 192.168.1.5 >/root/taotao-cloud/seata1.4.0/logs/seata.out 2>&1 &
     sleep 10
     echo " nacos started"
}

function stop_seata() {
     ps -ef | grep seata|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "nacos stoped"
}

case $1 in
"start")
    start_seata
    ;;
"stop")
    stop_seata
    ;;
"restart")
    stop_seata
    sleep 15
    start_seata
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
