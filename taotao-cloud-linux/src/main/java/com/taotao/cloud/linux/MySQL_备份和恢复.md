## MySQL 备份和恢复

    第 41 天  【备份和基础(02)】

**为什么要备份？**

1、灾难恢复：硬件故障、软件故障、自然灾害、黑客攻击、误操作；

2、 测试：从线上数据库导出数据，导入到测试库进行测试；

**备份要注意的要点：**

1、能容忍最多丢失多少数据；

2、恢复数据需要在多长时间内完成；

3、需要恢复哪些数据；

4、做还原测试，用于测试备份的可用性；

5、还原演练；

### 备份类型

根据备份的数据集区分：

- 完全备份：备份整个数据集；
- 部分备份：只备份数据子集；


根据备份时间轴的数据变化量区分：

- 完全备份
- 增量备份：仅备份最近一次完全备份或增量备份（如果存在增量）以来变化的数据；
- 差异备份：仅备份最近一次完全备份以来的变化的数据；


根据备份时业务系统能否使用区分：

- 热备份：线上系统在备份过程中，读写操作均可执行；
- 温备份：线上系统在备份过程中，读操作可执行，但写操作不可执行；
- 冷备份：线上系统在备份过程中，读写操作均不可执行；

MyISAM：温备，不能热备；
InnoDB：热备；

根据备份的数据类型区分：

- 物理备份：直接复制数据文件进行备份；
- 逻辑备份：从数据库中“导出”数据另存而进行的备份；与存储引擎无关；

**备份时要考虑的因素：**

1、持锁多久；

2、备份过程的时长；

3、备份负载；

4、恢复过程的时长

**备份什么？**

1、 数据；

2、二进制日志、InnoDB的事务日志；

3、代码（存储过程、存储函数、触发器、事件调度器）；

4、服务器的配置文件；

**设计备份方案：**

1、数据集：完全 + 增量；

2、备份手段：物理，逻辑；

**备份工具：**

mysqldump：逻辑备份工具，适用所有存储引擎，温备；完全备份、部分备份；对InnoDB存储引擎支持热备；

cp,tar等文件系统复制归档工具：物理备份工具，适用所有存储引擎；冷备；完全备份，部分备份；

lvm2的快照：几乎热备；借助于文件系统管理工具实现物理备份；

mysqlhotcopy：几乎冷备；仅适用于MyISAM存储引擎；

**备份工具的选择：**

mysqldump+复制binlog：
```
    mysqldump：完全备份；
    复制binlog中指定时间范围的event：增量备份；
```

lvm2快照+复制binlog：
```
    lvm2快照：使用cp或tar等做物理备份；完全备份；
    复制binlog中指定时间范围的event：增量备份；
```

xtrabackup：
```
    由Percona提供的支持对InnoDB做热备(物理备份)的工具；
    开源，支持完全备份、增量备份；
```

    第 42 天  【MySQL备份与恢复(01)】

#### 逻辑备份工具

mysqldump, mydumper, phpMyAdmin

Schema和数据存储在一起、巨大的SQL语句、单个巨大的备份文件；

缺点：对于较大的数据库备份较慢；

**mysqldump：**

客户端命令，通过mysql协议连接至mysqld服务器；

```
mysqldump [options] [db_name [tbl_name ...]]

   shell> mysqldump [options] db_name [tbl_name ...]  //不会备份创建数据库，仅备份创建表；
   shell> mysqldump [options] --databases db_name ...
   shell> mysqldump [options] --all-databases

[root@node1 ~]# mysqldump -uroot --databases hellodb > hellodb2.sql -p
Enter password: 
[root@node1 ~]# ll hellodb2.sql 
-rw-r--r--. 1 root root 9460 6月   5 10:13 hellodb2.sql
```

MyISAM表：支持温备；要锁定备份库，而后启动备份操作；

锁定方法：

```
--lock-all-tables, -x：锁定所有库的所有表；
--lock-tables：对于每个单独的数据库，在启动备份之前锁定其所有表；

对InnoDB表一样生效，实现温备；
```

InnoDB：支持热备；

```
    --single-transaction：启动一个大的单一事务实现备份，不会锁表；

[root@node1 ~]# mysqldump -uroot --single-transaction --databases hellodb > hellodb3.sql -p
Enter password: 
[root@node1 ~]# ll hellodb3.sql 
-rw-r--r--. 1 root root 9460 6月   5 10:15 hellodb3.sql
```

其它选项：

```
-E，--events：备份自定数据库相关的所有event scheduler；
-R，--routines：备份指定数据库相关的所有存储过程和存储函数；
--triggers：备份表相关的触发器；

--master-data[=#]：记录备份时，数据库系统使用的binlog文件及position（end_log_pos)；
    1：记录为CHANGE MASTER TO语句，此语句不被注释；
    2：记录为注释的CHANGE MASTER TO语句;
        -- CHANGE MASTER TO MASTER_LOG_FILE='mysql-bin.000004', MASTER_LOG_POS=7841;

--flush-logs:
    锁定表完成后，执行flush logs命令，滚动binlog日志；
```

注意：二进制日志文件不应该与数据文件放置在同一块磁盘；

练习：有一100MB级别的数据库：

（1）备份脚本；

（2）制作备份策略；

**基于lvm2的备份：** 

1、请求锁定所有表：

```
mysql> FLUSH TABLES WITH READ LOCK;
```

2、记录二进制日志文件及事件位置：

```
mysql> FLUSH LOGS;       // 滚动日志
mysql> SHOW MASTER STATUS;

# mysql -e 'SHOW MASTER STATUS' > /PATH/TO/SIMEFILE
    # mysql -e 'SHOW MASTER STATUS;' > /mysql_backup/binlog-`date +%F`
```

3、创建快照：

```
lvcreate -L # -s -p r -n NAME /DEV/VG_NAME/LV_NAME
    
        -L #：大小要根据备份过程中业务数据会增长的大小而定；
        -s：创建快照卷；
        -p：策略，r(只读)；
        -n：指明快照卷名称；

    # lvcreate -L 500M -s -p r -n mydata-snap  /dev/myvg/mydata
```

4、释放锁：

```
mysql> UNLOCK TABLES;
```

5、挂载快照卷，执行数据备份：

```
# mount -r /dev/myvg/mydata-snap /mnt
# cp -a /mnt/mydata/* /mysql_backup/
```

6、备份完成后，删除快照卷：

```
# umount /mnt
# lvremove /dev/myvg/mydata-snap
```

7、制定好策略，通过原卷备份二进制日志：

```
# cat /mysql_backup/binlog-2017-07-03 
File    Position    Binlog_Do_DB    Binlog_Ignore_DB
mysql-bin.000007    245 

# mysqlbinlog --start-position=245 /mydata/data/mysql-bin.000007 > /mysql_backup/binlog-`date +%F`.sql
```

8、恢复数据：

```
# cp -a /mysql_backup/* /mnt/mydata/
```

9、使用备份的binlog重放数据：

```
临时关闭binlog：
mysql> SET sql_log_bin=0;

重放数据：
mysql> source /mysql_backup/binlog-`date +%F`.sql

开启binlog：
mysql> SET sql_log_bin=1;
```

    第 42 天  【MySQL备份与恢复(02)】

**xtrabackup：**

由percona公司研发，www.percona.com，percona发布了percona-server数据库；percona-server是mysql的另一个分支。

percona维护了InnoDB的社区版本：Percona-XtraDB，针对Percona-XtraDB开发出了xtrabackup备份工具。

xtrabackup可以备份XtraDB和InnoDB。

功能比较：<https://www.percona.com/software/mysql-database/percona-xtrabackup/feature-comparison>

一、安装

1、简介
Xtrabackup 是由 percona 提供的 mysql 数据库备份工具，据官方介绍，这也是世界上唯一一款开源的能够对 innodb 和 xtradb 数据库进行热备的工具。

特点：

- 备份过程快速、可靠；
- 备份过程不会打断正在执行的事务；
- 能够基于压缩等功能节约资盘空间和流量；
- 自动实现备份检验；
- 还原速度快；

2、安装

[官网下载](https://www.percona.com/downloads/Percona-XtraBackup-2.4/LATEST/)相应版本的 rpm 包，安装即可。

```
$ wget https://www.percona.com/downloads/Percona-XtraBackup-2.4/Percona-XtraBackup-2.4.9/binary/redhat/7/x86_64/percona-xtrabackup-24-2.4.9-1.el7.x86_64.rpm

$ sudo  yum localinstall percona-xtrabackup-24-2.4.9-1.el7.x86_64.rpm
```

二、备份的实现

1、完全备份

```
连接数据库：
$ xtrabackup --user=DVADER --password=14MY0URF4TH3R --backup \
  --target-dir=/data/bkps/
$ innobackupex --user=DBUSER --password=SECRET /path/to/backup/dir/
$ innobackupex --user=LUKE  --password=US3TH3F0RC3 --stream=tar ./ | bzip2 -
```

如果需要使用一个最小权限的用户进行备份，则可基于如下命令创建此类用户：

```
mysql> CREATE USER 'bkpuser'@'localhost' IDENTIFIED BY 's3cret';
mysql> GRANT RELOAD, LOCK TABLES, PROCESS, REPLICATION CLIENT ON *.* TO
       'bkpuser'@'localhost';
mysql> FLUSH PRIVILEGES;
```

使用 innobackupex 备份时，其会调用 xtrabackup 备份所有的 InnoDB 表，复制所有关于表结构定义的相关文件（.frm)、以及 MyISAM、MERGE、CSV 和 ARCHIVE 表的相关文件，同时还会备份触发器和数据库配置信息相关的文件。这些文件会被保存至一个以时间命名的目录中。


在备份的同时，innobackup 还会在备份目录中创建如下文件：

- xtrabackup_checkpoints：备份类型（如完全或增量）、备份状态（如是已经为 prepared 状态）和 LSN（日志序列号）范围信息。
    + 每个 InnoDB 页（通常为 16k 大小）都会包含一个日志序列号，即 LSN。LSN 是整个数据库系统的系统版本号，每个页面相关的 LSN 能够表明此页面是如何发生改变的。
- xtrabackup_binlog_info：mysql 服务器当前正在使用的二进制日志文件及至备份这一刻为止二进制日志事件的位置。
- xtrabackup_binlog_pos_innodb：二进制日志文件及用于 InnoDB 或 XtraDB 表的二进制日志文件的当前 position。
- xtrabackup_binary：备份中用到的 xtrabackup 的可执行文件。
- backup-my.cnf：备份命令用到的配置选项信息。

在使用 innobackupex 进行备份是，还可以使用 --no-timestamp 选项来阻止命令自动创建一个以时间命名的目录；如此一来，innobackupex 命令将会创建一个 BACKUP-DIR 目录来存储备份数据。

示例：

```
# innobackupex --user=root --password=123456  --socket=/tmp/mysql.sock /mysqlbackups 
```

如果执行正确，其输出信息的最后几行通常如下：

```
200208 12:44:46 Executing FLUSH NO_WRITE_TO_BINLOG ENGINE LOGS...
xtrabackup: The latest check point (for incremental): '2644244'
xtrabackup: Stopping log copying thread.
.200208 12:44:46 >> log scanned up to (2644253)

200208 12:44:46 Executing UNLOCK TABLES
200208 12:44:46 All tables unlocked
200208 12:44:46 [00] Copying ib_buffer_pool to /mysqlbackups/2020-02-08_12-44-44/ib_buffer_pool
200208 12:44:46 [00]        ...done
200208 12:44:46 Backup created in directory '/mysqlbackups/2020-02-08_12-44-44/'
MySQL binlog position: filename 'mysql-bin.000001', position '415'
200208 12:44:46 [00] Writing /mysqlbackups/2020-02-08_12-44-44/backup-my.cnf
200208 12:44:46 [00]        ...done
200208 12:44:46 [00] Writing /mysqlbackups/2020-02-08_12-44-44/xtrabackup_info
200208 12:44:46 [00]        ...done
xtrabackup: Transaction log of lsn (2644244) to (2644253) was copied.
200208 12:44:46 completed OK!
```

2、准备（prepare）一个完全备份

一般情况下，在备份完成后，数据尚且不能用于恢复操作，因为备份的数据中可能会包含尚未提交的事务或已经提交但尚未同步至数据文件中的事务。因此，此时数据文件仍处理不一致状态。“准备”的主要作用正是通过回滚未提交的事务及同步已经提交的事务至数据文件也使得数据文件处于一致状态。

innobakupex 命令的 --apply-log 选项可用于实现上述功能。如下面的命令：

```
# innobackupex --apply-log /path/to/BACKUP-DIR
```

示例：

```
# innobackupex --apply-log /mysqlbackups/2020-02-07_23-58-28/
```

如果执行正确，其最后输出的几行信息通常如下：

```
InnoDB: xtrabackup: Last MySQL binlog file position 415, file name mysql-bin.000001
InnoDB: Removed temporary tablespace data file: "ibtmp1"
InnoDB: Creating shared tablespace for temporary tables
InnoDB: Setting file './ibtmp1' size to 12 MB. Physically writing the file full; Please wait ...
InnoDB: File './ibtmp1' size is now 12 MB.
InnoDB: 96 redo rollback segment(s) found. 1 redo rollback segment(s) are active.
InnoDB: 32 non-redo rollback segment(s) are active.
InnoDB: 5.7.13 started; log sequence number 2644501
xtrabackup: starting shutdown with innodb_fast_shutdown = 1
InnoDB: FTS optimize thread exiting.
InnoDB: Starting shutdown...
InnoDB: Shutdown completed; log sequence number 2644520
200208 12:50:37 completed OK!
```

在实现“准备”的过程中，innobackupex 通常还可以使用 --use-memory 选项来指定其可以使用的内存的大小，默认通常为 100M。如果有足够的内存可用，可以多划分一些内存给 prepare 的过程，以提高其完成速度。

3、从一个完全备份中恢复数据

注意：恢复不用启动 MySQL

innobackupex 命令的 --copy-back 选项用于执行恢复操作，其通过复制所有数据相关的文件至 mysql 服务器 DATADIR 目录中来执行恢复过程。innobackupex 通过 my.cnf 来获取 DATADIR 目录的相关信息。

```
# innobackupex --copy-back /path/to/BACKUP-DIR
```

如果执行正确，其输出信息的最后几行通常如下：

```
innobackupex: Finished copying back files.
130201 11:08:13  innobackupex: completed OK!
```

请确保如信息的最后一行出现“innobackupex: completed OK!”

当数据恢复至 DATADIR 目录以后，还需要确保所有数据文件的属主和属组均为正确的用户，如 mysql，否则在启动 mysqld 之前还需要事先修改数据文件的属主和属组。 如：

```
# chown -R mysql:mysql /mydata/data/
```

4、使用 innobackupex 进行增量备份

每个 InnoDB 的页面都会包含一个 LSN 信息，每当相关的数据发生改变，相关的页面的 LSN 就会自动增长。这正是 InnoDB 表可以进行增量备份的基础，即 innobackupex 通过备份上次完全备份之后发生改变的页面来实现。

要实现第一次增量备份，可以使用下面的命令进行：

```
# innobackupex --incremental /backup --incremental-basedir=BASEDIR
```

其中，BASEDIR 指的是完全备份所在的目录，此命令执行结束后，innobackupex 命令会在 /backup 目录中创建一个新的以时间命名的目录以存放所有的增量备份数据。另外，在执行过增量备份之后再一次进行增量备份时，其 --incremental-basedir 应该指向上一次的增量备份所在的目录。

需要注意的是，增量备份仅能应用于 InnoDB 或 XtraDB 表，对于 MyISAM 表而言，执行增量备份时其实进行的是完全备份。

“准备”（prepare）增量备份于整理完全备份有着一些不同，尤其要注意的是：

- 需要在每个备份（包括完全和各个增量备份）上，将已经提交的事务进行“重放”。“重放”之后，所有的备份数据将合并到完全备份上。
- 基于所有的备份将未提交的事务进行“回滚”。

于是，操作就变成了：

```
# innobackupex --apply-log --redo-only BASE-DIR
```

接着执行：

```
# innobackupex --apply-log --redo-only BASE-DIR --incremental-dir=INCREMENTAL-DIR-1
```

而后是第二个增量：

```
# innobackupex --apply-log --redo-only BASE-DIR --incremental-dir=INCREMENTAL-DIR-2
```

其中 BASE-DIR 指的是完全备份所在的目录，而 INCREMENTAL-DIR-1 指的是第一次增量备份的目录，INCREMENTAL-DIR-2 指的是第二次增量备份的目录，其它以此类推，即如果有多次增量备份，每一次都要执行如上操作。

5、Xtrabackup 的“流”及“备份压缩”功能

Xtrabackup 对备份的数据文件支持“流”功能，即可以将备份的数据通过 STDOUT 传输给 tar 程序进行归档，而不是默认的直接保存至某备份目录中。要使用此功能，仅需要使用 --stream 选项即可，如：

```
# innobackupex --stream=tar /backup | gzip > /backups/`date +%F_%H-%M-%S`.tar.gz
```

甚至也可以使用类似如下命令将数据备份至其它服务器：

```
# innobackupex --stream=tar /backup | ssh user@host "cat - | gzip > /backups/`date +%F_%H-%M-%S`.tar.gz"
```

此外，在执行本地备份是，还可以使用 --parallel 选项对多个文件进行并行复制。此选项用于指定在复制时启动的线程数目。当然，在实际进行备份时要利用此功能的便利性，也需要启用 innodb_file_per_table 选项或共享的表空间通过 innodb_data_file_path 选项存储多个 ibdata 文件中。对某一数据库的多个文件的复制无法利用到此功能。其简单使用方法如下：

```
# innobackupex --parallel /path/to/backup
```

同时，innobackupex 备份的数据文件也可以存储至远程主机，这可以使用 --remote-host 选项来实现：

```
# innobackupex --remote-host=user@host /path/IN/REMOTE/HOST/to/backup
```

6、导入或导出单张表

默认情况下，InnoDB 表不能通过直接复制表文件的方式在 mysql 服务器之间进行移植，即使使用了 innodb_file_per_table 选项，而使用 Xtrabackup 工具可以实现此种功能，不过，此时需要“导出”表的 mysql 服务器启用了 innodb_file_per_table 选项（严格来说，是要“导出”的表在其创建之前，mysql 服务器就启用了 innodb_file_per_table选项），并且“导入”表的服务器同时启用了 innodb_file_per_table 和 innodb_expand_import 选项。

（1）“导出”表
导出表时在备份的 prepare 阶段进行的，因此，一旦完全备份完成，就可以在 prepare 过程中通过 --export 选项将某表导出了：

```
# innobackupex --apply-log --export /path/to/backup
```

此命令会为每个 innodb 表的表空间创建一个以 .exp 结尾的文件，这些以 .exp 结尾的文件则可以用于导入至其它服务器。

（2）“导入”表
要在 mysql 服务器上导入来自于其它服务器的某 innodb 表，需要先在当前服务器上创建一个跟原表表结构一致的表，而后才能实现将表导入：

```
mysql> CREATE TABLE mytable (...) ENGINE=InnoDB;
```

然后将此表的表空间删除：

```
mysql> ALTER TABLE mydatabase.mytable DISCARD TABLESPACE;
```

接下来，将来自于“导出”表的服务器的 mytable 表的 mytable.ibd 和 mytable.exp 文件复制到当前服务器的数据目录，然后使用如下命令将其“导入”：

```
mysql> ALTER TABLE mydatabase.mytable IMPORT TABLESPACE;
```

7、使用Xtrabackup对数据库进行部分备份

Xtrabackup也可以实现部分备份，即只备份某个或某些指定的数据库或某数据库中的某个或某些表。但要使用此功能，必须启用innodb_file_per_table选项，即每张表保存为一个独立的文件。同时，其也不支持--stream选项，即不支持将数据通过管道传输给其它程序进行处理。

此外，还原部分备份跟还原全部数据的备份也有所不同，即你不能通过简单地将prepared的部分备份使用--copy-back选项直接复制回数据目录，而是要通过导入表的方向来实现还原。当然，有些情况下，部分备份也可以直接通过--copy-back进行还原，但这种方式还原而来的数据多数会产生数据不一致的问题，因此，无论如何不推荐使用这种方式。

（1）创建部分备份

创建部分备份的方式有三种：正则表达式(--include), 枚举表文件(--tables-file)和列出要备份的数据库(--databases)。

（a）使用--include

使用--include时，要求为其指定要备份的表的完整名称，即形如databasename.tablename，如：

```
# innobackupex --include='^mageedu[.]tb1'  /path/to/backup
```

（b）使用--tables-file

此选项的参数需要是一个文件名，此文件中每行包含一个要备份的表的完整名称；如：

```
# echo -e 'mageedu.tb1\nmageedu.tb2' > /tmp/tables.txt
# innobackupex --tables-file=/tmp/tables.txt  /path/to/backup
```

（c）使用--databases

此选项接受的参数为数据名，如果要指定多个数据库，彼此间需要以空格隔开；同时，在指定某数据库时，也可以只指定其中的某张表。此外，此选项也可以接受一个文件为参数，文件中每一行为一个要备份的对象。如：

```
# innobackupex --databases="mageedu testdb"  /path/to/backup
```

（2）整理(preparing)部分备份

prepare部分备份的过程类似于导出表的过程，要使用--export选项进行：

```
# innobackupex --apply-log --export  /pat/to/partial/backup
```

此命令执行过程中，innobackupex会调用xtrabackup命令从数据字典中移除缺失的表，因此，会显示出许多关于“表不存在”类的警告信息。同时，也会显示出为备份文件中存在的表创建.exp文件的相关信息。

（3）还原部分备份

还原部分备份的过程跟导入表的过程相同。当然，也可以通过直接复制prepared状态的备份直接至数据目录中实现还原，不要此时要求数据目录处于一致状态。

（完）























