###########################################
一、主从复制以及原理
概念
主从复制，是指将一台Redis服务器的数据，复制到其他redis服务器，
前者称为主节点（master/leader）,后者称为从节点（slave/follower）；
数据复制是单向的，只能由主节点到从节点，。Mater以写为主，Slave以读为主。
默认情况下，每台redis服务器都是主节点；且一个主节点可以有多个从节点（或没有从节点），
但一个从节点只能有一个主节点。

主从复制的作用包括：
1，数据冗余：主从复制实现了数据的热备份，是持久化之外的一种数据冗余方式
2，故障恢复：当主节点出现问题时，可以由从节点提供服务，实现快速的故障恢复；实际上是一种服务的冗余。
3，负载均衡：在主从复制的基础上，配合读写分离，可以由主节点提供写服务，由从节点提供读服务（即写redis数据时，应用连接主节点，读redis数据时连接从节点），分担服务负载；尤其是在写少读多的场景下，通过多个从节点分担读负载，可以大大提高Redis服务器的并发量。
4，高可用基石：除了上述作用外，主从复制还是哨兵和集群能够实施的基础，因此说主从复制是redis高可用的基础。

一般来说，要将redis运用到项目工程中，只使用一台Redis是万万不能的，原因如下：
1，从结构上，单个redis服务器会发生单点故障，并且一台redis服务器需要处理所有的请求负载，压力较大。
2，从容量上，单个redis服务器内存容量有限，就算一台服务器内存容量为256G，也不能将所有内存用作redis存储内存。
电商网站上的商品，一般都是上传一次，无数次浏览，也就是”读多写少”，对于这种场景可以使用如下架构

主从复制，读写分离！80%的情况下，都是在进行读操作！减缓服务器压力，架构中间经常使用！一主二从或者多从

1，环境配置
只配置从库，不用配置主库  查看当前redsi信息 info replication

复制三个配置文件，然后修改对应的信息，修改端口、pid名字、log文件名字、dump.rdb。
cp /opt/taotao-cloud/redis-6.2.4/redis.conf redis_6380.conf
cp /opt/taotao-cloud/redis-6.2.4/redis.conf redis_6381.conf
cp /opt/taotao-cloud/redis-6.2.4/redis.conf redis_6382.conf

修改端口、pid名字、log文件名字、dump.rdb
port 6380
pidfile /opt/taotao-cloud/redis-6.2.4/pid/redis_6380.pid
logfile "/opt/taotao-cloud/redis-6.2.4/logs/redis_6380.log"
dbfilename dump_6380.rdb

port 6381
pidfile /opt/taotao-cloud/redis-6.2.4/pid/redis_6381.pid
logfile "/opt/taotao-cloud/redis-6.2.4/logs/redis_6381.log"
dbfilename dump_6381.rdb

port 6382
pidfile /opt/taotao-cloud/redis-6.2.4/pid/redis_6382.pid
logfile "/opt/taotao-cloud/redis-6.2.4/logs/redis_6382.log"
dbfilename dump_6382.rdb

启动6380、6381、6382 redis服务，查看进程中三个redis服务是否运行
/opt/taotao-cloud/redis-6.2.4/bin/redis-server /opt/taotao-cloud/redis-6.2.4/redis_6380.conf
/opt/taotao-cloud/redis-6.2.4/bin/redis-server /opt/taotao-cloud/redis-6.2.4/redis_6381.conf
/opt/taotao-cloud/redis-6.2.4/bin/redis-server /opt/taotao-cloud/redis-6.2.4/redis_6382.conf

2，主从复制配置
默认情况下，每台redis服务器都是主节点，，一般情况下，只需要配置从机就可以了。配置一主二从。主机为6380，从机为6381和6382

######命令行配置
1，先查看6380和6381服务的信息
info replication

2，将6381作为从机，归属于6380机器
slaveof 192.168.10.220 6380

3，将6382作为从机，归属于6380机器
slaveof 192.168.10.220 6380

#######配置文件配置
修改从节点配置文件
replicaof 192.168.10.220 6380
masterauth taotao-cloud
slave-read-only yes

一主一从
最基础的主从复制模型，主节点负责处理写请求，从节点负责处理读请求，主节点使用RDB持久化模式，从节点使用AOF持久化模式：

一主多从
一个主节点可以有多个从节点，但每个从节点只能有一个主节点。一主多从适用于写少读多的场景，多个从节点可以分担读请求负载，提升并发：

树状主从
上面的一主多从可以实现读请求的负载均衡，但当从节点数量多的时候，主节点的同步压力也是线性提升的，因此可以使用树状主从来分担主节点的同步压力

3，主从复制原理
主从复制过程大体可以分为3个阶段：连接建立阶段（即准备阶段）、数据同步阶段、命令传播阶段。
在从节点执行 slaveof 命令后，复制过程便开始按下面的流程运作：
  保存主节点信息：配置slaveof之后会在从节点保存主节点的信息。
  主从建立socket连接：定时发现主节点以及尝试建立连接。
  发送ping命令：从节点定时发送ping给主节点，主节点返回PONG。若主节点没有返回PONG或因阻塞无法响应导致超时，则主从断开，在下次定时任务时会从新ping主节点。
  权限验证：若主节点开启了ACL或配置了requirepass参数，则从节点需要配置masteruser和masterauth参数才能保证主从正常连接。
  同步数据集：首次连接，全量同步。
  命令持续复制：全量同步完成后，保持增量同步。

slave启动成功连接到master后会发送一个sync同步命令。
master接到命令，启动后台的存盘进程，同时收集所有接收到的用于修改数据集的命令，在后台程序执行完毕之后，matser将传送整个数据文件到slave,并完成一次完全同步。

全量复制： 而slave服务在接收到数据库文件数据后，将其存盘，并加载到内存汇总
增量复制：master继续将新的所有收集到的修改命令依次传给slave，完成同步
但是只要是重新连接master，一次完全同步（全量复制）将会被自动执行，我们的数据移一定可以在从机中看到
