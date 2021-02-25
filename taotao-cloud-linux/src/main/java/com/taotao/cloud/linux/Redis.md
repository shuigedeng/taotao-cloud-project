## Redis

- redis: <rdis.io>
    + KV cache and store
        * in-memory
        * 持久化
        * 主从（借助于sentinel实现一定意义上的HA）
        * Clustering（分布式）

    + 数据结构服务器：
        * String, List, Hash, Set, Sorted Set, Bitmap, Hyperloglogs

    + Redis is an in-memory but persistent on disk database.
    + 1 Million small Key -> String value pairs use ~ 100MB of memory
    + Single threaded - but CPU should not be the bottleneck
        * Average Linux system can deliver even 500K requests per second.
    + Limit is likely the available memory in your system.
        * max. 232 keys

- 存储系统有三类：
    + RDBMS
    + NoSQL:
        * KV NoSQL：redis
        * Column Family NoSQL：HBase
        * Documentation NoSQL：MongoDB
        * Graph NoSQL：Neo4j
    + NewSQL

- Redis的组件：
    + redis-server
    + redis-cli
        * Command line interface
    + redis-benchmark
        * Benchmarking utility
    + redis-check-dump & redis-check-aof
        * Corrupted RDB/AOF file utilities

- 安装：epel源
```
    # yum install redis
```

- 配置文件：
```
    /etc/redis.conf
```

- Redis守护进程：
    + 监听 6379/TCP

- Strings:
    + SET key value [EX #] [NX|XX]
    + GET
    + INCR
    + DECR
    + EXIST
- Lists:
    + LPUSH
    + RPUSH
    + LPOP
    + ROP
    + LINDEX
    + LSET
- Sets:
    + SADD
    + SINTER
    + SUNION
    + SPOP
    + SINMEMBER
- Sorted Sets:
    + ZADD
    + ZRANGE
    + ZCARD
    + ZRANC
- Hashes:
    + HSET
    + HSETNX
    + HGET
    + HKEYS
    + HVALS
    + HDEL

> 第 60 天 【redis 应用进阶(02)】

- 认证实现方法：
    + redis.conf
        * `requirepass PASSWORD`
    + redis-client
        * `AUTH PASSWORD`
    ```
        # redis-cli 
        127.0.0.1:6379> select 0
        (error) NOAUTH Authentication required.
        127.0.0.1:6379> AUTH foobared
        OK
        127.0.0.1:6379> select 0
        OK
    ```

- 清空数据库：
    + FLUSHDB：清空当前库
    + FLUSHALL：清空所有库

- 事务：
    + 通过MULTI, EXEC, WATCH等命令实现事务功能；将一个或多个命令归并为一个操作提请服务器按顺序执行的机制。
    + redis 不支持回滚操作；
    ```
        127.0.0.1:6379> MULTI
        OK
        127.0.0.1:6379> SET IP 192.168.1.1
        QUEUED
        127.0.0.1:6379> GET IP
        QUEUED
        127.0.0.1:6379> SET PORT 8080
        QUEUED
        127.0.0.1:6379> GET PORT
        QUEUED
        127.0.0.1:6379> EXEC
        1) OK
        2) "192.168.1.1"
        3) OK
        4) "8080"
    ```
    + MULTI：启动一个事务；
    + EXEC：执行事务；
        * 一次性将事务中的所有操作执行完成后返回给客户端；
    + WATCH：乐观锁，在EXEC命令执行之前，用于监视指定数量的键，如果监视中的某任意键数据被修改，则服务器拒绝执行事务;
    ```
    client 1:
        127.0.0.1:6379> GET IP
        "192.168.1.1"
        127.0.0.1:6379> WATCH IP
        OK
        127.0.0.1:6379> MULTI
        OK
        127.0.0.1:6379> SET IP 10.0.0.1
        QUEUED
        127.0.0.1:6379> GET IP
        QUEUED

    client 2:
        127.0.0.1:6379> GET IP
        "192.168.1.1"
        127.0.0.1:6379> SET IP 172.16.100.99
        OK
        127.0.0.1:6379> GET IP
        "172.16.100.99"

    clietn 1:
        127.0.0.1:6379> EXEC
        (nil)
    ```

- Connection 相关的命令：
    + AUTH
    + PING
    + ECHO
    + SELECT

- Server相关的命令：
    + HELP @server
    + CLIENT SETNAME  connection-name
    + CLIENT GETNAME 
    ```
        127.0.0.1:6379> CLIENT GETNAME
        (nil)
        127.0.0.1:6379> CLIENT SETNAME localconn
        OK
        127.0.0.1:6379> CLIENT GETNAME
        "localconn"
    ```
    + CLIENT KILL ip:port

    + CONFIG RESETSTAT 
    + CONFIG SET parameter value
    + CONFIG REWRITE: Rewrite the configuration file with the in memory configuration
    + DBSIZE: Return the number of keys in the selected database
    + LASTSAVE: Get the UNIX time stamp of the last successful save to disk
    + MONITOR: Listen for all requests received by the server in real time
    + SHUTDOWN [NOSAVE|SAVE]: Synchronously save the dataset to disk and then shut down the server

- 发布与订阅(PUBLISH/SUBSCRIBE)
    + 频道：消息队列
    + SUBSCRIBE：订阅一个或多个队列；
    + PUBLISH：向频道发布消息；
    ```
        127.0.0.1:6379> HELP SUBSCRIBE
          SUBSCRIBE channel [channel ...]
          summary: Listen for messages published to the given channels
          since: 2.0.0
          group: pubsub

        127.0.0.1:6379> SUBSCRIBE news
        Reading messages... (press Ctrl-C to quit)
        1) "subscribe"
        2) "news"
        3) (integer) 1

        127.0.0.1:6379> PUBLISH news hello
        (integer) 1

        127.0.0.1:6379> SUBSCRIBE news
        Reading messages... (press Ctrl-C to quit)
        1) "subscribe"
        2) "news"
        3) (integer) 1
        1) "message"
        2) "news"
        3) "hello"
    ```
    + PSUBSCRIBE：模式订阅
    ```
        127.0.0.1:6379> PSUBSCRIBE "news.i[to]"
        Reading messages... (press Ctrl-C to quit)
        1) "psubscribe"
        2) "news.i[to]"
        3) (integer) 1
    ```

- Redis的持久化：
    + RDB：snapshot，二进制格式；按事先定制的策略，周期性地将数据保存至磁盘，数据文件默认为 dump.rdb;
        * 客户端也可以显示使用SAVE或BGSAVE命令启动快照保存机制；
            - SAVE：同步，在主线程中保存快照，此时会阻塞所有客户端请求；
            - BGSAVE：异步，不会阻塞客户端请求；
        * 配置文件：
        ```
            # /etc/redis.conf
            SAVE 900 1
            SAVE 300 10
            SAVE 60  10000

            stop-writes-on-bgsave-error yes
            rdbcompression yes
            rdbchecksum yes
        ```
    + AOF：Append Only File，
        * 记录每一次写操作至指定的文件尾部实现持久化，当redis重启时，可通过重新执行文件中的命令在内存中重建数据库；
        * BGREWRITEAOF：AOF文件重写；
            - 不会读取正在使用AOF文件，而通过将内存中的数据以命令的方式保存到临时文件中，完成之后替换原来的AOF文件；
            - 重写过程：
                + （1）redis主进程通过fork创建子进程；
                + （2）子进程根据redis内存中的数据创建数据库重建命令序列与临时文件；
                + （3）父进程继续接收client的请求，并会把这些请求中的写操作继续追加至原来AOF文件中；额外的，这些新的写请求还会被放置于一个缓冲队列中；
                + （4）子进程重写完成，会通知父进程，父进程把缓冲中的命令写到临时文件中；
                + （5）父进程用临时文件替换老的AOF文件；
        * 配置文件：
        ```
            # /etc/redis.conf
            appendonly no //默认没有开启
            appendfilename "appendonly.aof"
            appendfsync {always|everysec|no}
            no-appendfsync-on-rewrite no
            auto-aof-rewrite-percentage 100
            auto-aof-rewrite-min-size 64mb
        ```

        * 注意：持久本身不能取代备份；还应该指定备份策略，对redis数据库定期进行备份；
        * RDB与AOF同时启用：
            - (1) BGSAVE和BGREWRITEAOF不会同时执行；
            - (2) 在Redis服务器启动用于恢复数据时，会优先使用AOF；

- Rides 复制：
    + 特点：
        * 一个Master可以有多个Slave；
        * 支持链式复制；
        * Master以非阻塞方式同步数据至Slave；
    + slave 配置：
    ```
        # /etc/redis.conf
        slaveof <masterip> <masterport>

        或者使用命令：
        > SLAVEOF <masterip> <masterport>
    ```
    + 可以使用 info 命令查看配置信息；
    + 注意： 如果master使用requirepass开启了认证功能，从服务器要使用masterauth <PASSWORD>来连入服务请求使用此密码进行认证；

- sentinel:
    + 用于管理多个redis服务器实现HA；
        * 监控
        * 通知
        * 自动故障转移
        * 流言协议，投票协议
    + 程序：
        * `redis-sentinel /path/to/file.conf` 
        * `redis-server /path/to/file.conf --sentinel`
        * (1) 服务器自身初始化，运行redis-server中专用于sentinel功能的代码；
        * (2) 初始化sentinel状态，根据给定的配置文件，初始化监控的master服务器列表；
        * (3) 创建连向master的连接；
    + 专用配置文件：
    ```
        /etc/redis-sentinel.conf
        指定监控的master节点：
        (1) # sentinel monitor <master-name> <ip> <redis-port> <quorum>
                sentinel monitor mymaster 127.0.0.1 6379 2

        多长时间master为接收响应，将master标记为故障：
        (2) # sentinel down-after-milliseconds <master-name> <milliseconds>
                sentinel down-after-milliseconds mymaster 30000

        (3) # sentinel parallel-syncs <master-name> <numslaves>
                sentinel parallel-syncs mymaster 1

        故障转移超时时间：
        (4) # sentinel failover-timeout <master-name> <milliseconds>
                sentinel failover-timeout mymaster 180000
    ```
    + 主观下线，客观下线：
        * 主观下线：一个sentinel节点判断出某节点下线；
        * 客观下线：多个sentinel节点协商后判断出某节点下线；
    + 专用命令:
        * SENTINEL masters：列出所有监控的主服务器；
        * SENTINEL slaves <master name>：获取指定主服务器的所有从节点；
        * SENTINEL get-master-addr-by-name <master name>：获取指定主节点的ip地址；
        * SENTINEL reset：重置所有操作；
        * SENTINEL failover <master name>：手动故障转移；

- Clustring:
    + 分布式数据库，通过分片机制进行数据分布，clustering内的每个节点仅持有数据库的一部分数据；
        * 每个节点持有全局元数据，但仅持有一部分数据；
    + 解决方案：
        * Twemproxy(Twitter): 
            - 代理分片机制；
            - 非常稳定，企业级方案；
            - 单点故障
            - 已弃用，不建议使用。
        * Codis（豌豆荚）：豆瓣开发，国内用的比较多，推荐。
        * Redis Cluster（官方）：去中心化，需要智能客户端，需要redis3.0以上版本支持；
        * Cerberus（芒果TV）：无需智能客户端，需要redis3.0以上版本支持