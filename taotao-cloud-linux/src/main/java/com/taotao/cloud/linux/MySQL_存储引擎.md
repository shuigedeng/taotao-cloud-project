    第 40 天 【MariaDB存储引擎及编译安装(03)】

## MySQL存储引擎

对MySQL来讲，存储引擎是表级别的概念，每创建一张表都可以单独指明存储引擎类型；因为各存储引擎的特性各不相同，所以并不建议交叉使用存储引擎；
```
表类型：
    CREATE TABLE ... ENGINE=
```

#### InnoDB

InnoDB 用于处理大量的短期事务；数据存储于“表空间”（Table Space）中；基于 MVCC（多版本并发控制）来提高并发，支持所有的四个隔离级别，默认级别为 REPEATABLE READ（可重复读）；使用间隙锁防止幻读；使用聚集索引；支持“自适应 hash 索引”；

MariaDB 使用的 InnoDB 引擎是由 Percona 提供的开源优化版本：XtraDB 

- 1、所有InonoDB表的数据和索引放置于同一个表空间中；
    + 表空间文件：datadir 定义的目录下：
        * 数据文件（存储数据和索引）：`ibdata1, ibddata2, ...`
- 2、每个表单独使用一个表空间存储表的数据和索引；
    + `innodb_file_per_table = on`
    + 数据文件（存储数据和索引）：`tbl_name.ibd`
    + 表格式定义存储文件：`tbl_name.frm`

###### 特性总结：
- 数据存储：表空间
- 并发：MVCC，间隙锁
- 索引：聚集索引、辅助索引
- 性能：预读操作、自适应 hash、插入缓存区
- 备份：支持热备（xtrabackup）
- 锁粒度：行级锁

#### MyISAM

MyISAM 支持全文索引（FULLTEXT index）、压缩、空间函数（GIS）；但不支持事务，且为表级锁；

诟病：崩溃后无法安全恢复；

适用场景：只读（或者写较少）、表较小（可以接受长时间进行修复操作）；

MariaDB 支持使用 Aria 存储引擎，Aria 除了支持 MyISAM 所有特性外，另外还支持 Crash-safe（崩溃后安全恢复），是 MyISAM 的一种扩展实现； 

- 文件：
    + 表格式定义文件：`tbl_name.frm`
    + 数据文件：`tbl_name.MYD`
    + 索引文件：`tbl_name.MYI`

###### 特性总结:
- 加锁和并发：表级锁
- 修复：手工活自动修复、但可能丢失数据
- 索引：非聚集索引
- 支持延迟更新索引键
- 支持压缩存储表

行格式：dynamic, fixed, compressed, compact, redundent

#### 其它的存储引擎：

##### 1、CSV

将普通 CSV（字段通过逗号分隔）做为 MySQL 表使用。但不支持索引。可以在数据库运行时导入或导出文件。可以将 Excel 等电子表格软件中的数据存储为 CSV文件，然后复制到 MySQL 数据目录下，就能在 MySQL 中打开使用；

##### 2、MRG_MYISAM 

MRG_MYISAM 引擎是 MyISAM 引擎的一个变种。MRG_MYISAM 表是将多个 MyISAM 表合并成为一个虚拟表；

##### 3、BLACKHOLE

类似于 Linux `/dev/null` 文件设备，不真正存储任何数据。

> “Blackhole引擎没有实现任何的存储机制，它会丢弃所有插入的数据，不做任何保存。但是服务器会记录Blackhole表的日志，所以可以用于复制数据到备库，或者只是简单地记录到日志。这种特殊的存储引擎可以在一些特殊的复制架构和日志审核时发挥作用。但这种应用方式我们碰到过很多问题，因此并不推荐。”

> 摘录来自: 施瓦茨 (Baron Schwartz). “高性能MySQL(第3版)”。

##### 4、MEMORY

所有数据都保存于内存中，内存表；支持 hash 索引；表级锁；

> “如果需要快速地访问数据，并且这些数据不会被修改，重启以后丢失也没有关系，那么使用Memory表（以前也叫做HEAP表）是非常有用的。Memory表至少比MyISAM表要快一个数量级，因为所有的数据都保存在内存中，不需要进行磁盘I/O。Memory表的结构在重启以后还会保留，但数据会丢失。”

>摘录来自: 施瓦茨 (Baron Schwartz). “高性能MySQL(第3版)”。 

##### 5、PERFORMANCE_SCHEMA

伪存储引擎，其在内存中存储 MySQL 核心内部性能统计，数据会随着 MySQL 的运行不断变化；

##### 6、ARCHIVE

只支持 SELECT 和 INSERT 操作；支持行级锁和专用缓冲区；不支持事务；

> “Archive引擎会缓存所有的写并利用zlib对插入的行进行压缩，所以比MyISAM表的磁盘I/O更少。但是每次SELECT查询都需要执行全表扫描。所以Archive表适合日志和数据采集类应用，这类应用做数据分析时往往需要全表扫描。或者在一些需要更快速的INSERT操作的场合下也可以使用。”

> 摘录来自: 施瓦茨 (Baron Schwartz). “高性能MySQL(第3版)”。

##### 7、FEDERATED 

用于访问其它远程 MySQL 服务器一个代理，它通过创建一个到远程 MySQL 服务器的客户端连接，并将查询传输到远程服务器执行，而后完成数据存取；

> “Federated引擎是访问其他MySQL服务器的一个代理，它会创建一个到远程MySQL服务器的客户端连接，并将查询传输到远程服务器执行，然后提取或者发送需要的数据。最初设计该存储引擎是为了和企业级数据库如Microsoft SQL Server和Oracle的类似特性竞争的，可以说更多的是一种市场行为。尽管该引擎看起来提供了一种很好的跨服务器的灵活性，但也经常带来问题，因此默认是禁用的。MariaDB使用了它的一个后续改进版本，叫做FederatedX。”

> 摘录来自: 施瓦茨 (Baron Schwartz). “高性能MySQL(第3版)”。


#### MariaDB 支持的其它存储引擎：

- OQgraph：支持图操作（比如查找两点之间的最短路径）；
- SphinxSE：常用语构建企业自己的搜索引擎；
- TokuDB：是一种大数据存储引擎，可以在很大哦的数据量上创建大量的索引；
- Cassandra：NoSQL存储引擎；
- SQUENCE

（完）
