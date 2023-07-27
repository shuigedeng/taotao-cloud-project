###########################################
# 哨兵模式
哨兵（sentinel），用于对主从结构中的每一台服务器进行监控，当主节点出现故障后通过投票机制来挑选新的主节点，
并且将所有的从节点连接到新的主节点上。

前面的主从是最基础的提升Redis服务器稳定性的一种实现方式，
但我们可以看到master节点仍然是一台，若主节点宕机，所有从服务器都不会有新的数据进来，
如何让主节点也实现高可用，当主节点宕机的时候自动从从节点中选举一台节点提升为主节点就是哨兵实现的功能。

当主服务器宕机后，需要把一台从服务器切换为主服务器，保证redis服务的高可用。

作用
  监控：监控主从节点运行情况。
  通知：当监控节点出现故障，哨兵之间进行通讯。
  自动故障转移：当监控到主节点宕机后，断开与宕机主节点连接的所有从节点，然后在从节点中选取一个作为主节点，将其他的从节点连接到这个最新的主节点。最后通知客户端最新的服务器地址。

哨兵也是一台redis服务器，只是不对外提供任何服务，
redis的bin目录下的redis-sentinel其实就是redis-server的软连接。

哨兵节点最少三台且必须为单数。这个与其他分布式框架如zookeeper类似，
如果是双数，在选举的时候就会出现平票的情况，所以必须是三台及以上的单数。

原理

哨兵之间会有通讯，哨兵和主从节点之间也有监控，基于这些信息同步和状态监控实现Redis的故障转移：

哨兵和哨兵之间以及哨兵和Redis主从节点之间每隔一秒发送ping监控它们的健康状态；

哨兵向Redis主从节点每隔10秒发送一次info保存节点信息；

哨兵向Redis主节点每隔2秒发送一次hello，直到哨兵报出sdown，代表主节点失联，然后通知其余哨兵尝试连接该主节点；

Redis主节点下线的情况分为主观下线和客观下线：

主观下线(sdown)：单独一个哨兵发现master故障了。
客观下线(odown)：半数哨兵都认为master节点故障就会触发故障转移。

哨兵Leader选举：

一般情况下当哨兵发现主节点sdown之后 该哨兵节点会成为领导者负责处理主从节点的切换工作：

哨兵A发现Redis主节点失联；

哨兵A报出sdown，并通知其他哨兵，发送指令sentinel is-master-down-by-address-port给其余哨兵节点；

其余哨兵接收到哨兵A的指令后尝试连接Redis主节点，发现主节点确实失联；

哨兵返回信息给哨兵A，当超过半数的哨兵认为主节点下线后，状态会变成odown；

最先发现主节点下线的哨兵A会成为哨兵领导者负责这次的主从节点的切换工作；
哨兵的选举机制是以各哨兵节点接收到发送sentinel is-master-down-by-address-port指令的哨兵id 投票，票数最高的哨兵id会成为本次故障转移工作的哨兵Leader；

故障转移：

当哨兵发现主节点下线之后经过上面的哨兵选举机制，选举出本次故障转移工作的哨兵节点完成本次主从节点切换的工作：

哨兵Leader 根据一定规则从各个从节点中选择出一个节点升级为主节点；

其余从节点修改对应的主节点为新的主节点；

当原主节点恢复启动的时候，变为新的主节点的从节点

哨兵Leader选择新的主节点遵循下面几个规则：

健康度：从节点响应时间快；

完整性：从节点消费主节点的offset偏移量尽可能的高 ()；

稳定性：若仍有多个从节点，则根据从节点的创建时间选择最有资历的节点升级为主节点；

 在哨兵模式下主从节点总是会变更，因此在Java或Python中访问哨兵模式下的Redis时可以使用对应的哨兵接口连接：

#JavaJedisSentinelPool
#Pythonfrom redis.sentinel import SentinelConnectionPool

# 单哨兵模式
cp /opt/taotao-cloud/redis-6.2.4/sentinel.conf sentinel.conf_bak

# sentinel.conf
bind 127.0.0.1 192.168.10.220
protected-mode yes
port 26379
daemonize yes
pidfile /opt/taotao-cloud/redis-6.2.4/pid/sentinel.pid
logfile "/opt/taotao-cloud/redis-6.2.4/logs/sentinel.log"
dir /tmp
dir /opt/taotao-cloud/redis-6.2.4/data
sentinel monitor mymaster 192.168.10.220 6380 2
sentinel auth-pass mymaster taotao-cloud
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000

# 启动sentinel
/opt/taotao-cloud/redis-6.2.4/bin/redis-sentinel /opt/taotao-cloud/redis-6.2.4/sentinel.conf



# 多哨兵模式（集群版）
多哨兵模式集群很简单，复制多个sentinel，改下端口即可
cp /opt/taotao-cloud/redis-6.2.4/sentinel.conf sentinel_26380.conf
cp /opt/taotao-cloud/redis-6.2.4/sentinel.conf sentinel_26381.conf
cp /opt/taotao-cloud/redis-6.2.4/sentinel.conf sentinel_26382.conf

port 26380
daemonize yes
pidfile /opt/taotao-cloud/redis-6.2.4/pid/sentinel_26380.pid
logfile "/opt/taotao-cloud/redis-6.2.4/logs/sentinel_26380.log"

port 26381
daemonize yes
pidfile /opt/taotao-cloud/redis-6.2.4/pid/sentinel_26381.pid
logfile "/opt/taotao-cloud/redis-6.2.4/logs/sentinel_26381.log"

port 26382
daemonize yes
pidfile /opt/taotao-cloud/redis-6.2.4/pid/sentinel_26382.pid
logfile "/opt/taotao-cloud/redis-6.2.4/logs/sentinel_26382.log"

/opt/taotao-cloud/redis-6.2.4/bin/redis-sentinel /opt/taotao-cloud/redis-6.2.4/sentinel_26380.conf
/opt/taotao-cloud/redis-6.2.4/bin/redis-sentinel /opt/taotao-cloud/redis-6.2.4/sentinel_26381.conf
/opt/taotao-cloud/redis-6.2.4/bin/redis-sentinel /opt/taotao-cloud/redis-6.2.4/sentinel_26382.conf
