
    第 41 天  【MariaDB日志文件（01）】


## MariaDB 日志

### 查询日志：query log

记录查询操作。默认 MariaDB 没有启用此功能。

可以配置记录在文件（file）中，也可记录在 MySQL 表（table）中；

```
    general_log=ON|OFF：是否启用查询日志
    general_log_file=HOSTNAME.log：当log_output有FILE类型时，日志信息的记录位置；
    log_output=TABLE|FILE|NONE：日志输出的类型，NONE为不启用日志；

    MariaDB [(none)]> SHOW GLOBAL VARIABLES WHERE Variable_name IN ('general_log','general_log_file','log_output');
    +------------------+-----------+
    | Variable_name    | Value     |
    +------------------+-----------+
    | general_log      | OFF       |
    | general_log_file | node1.log |
    | log_output       | FILE      |
    +------------------+-----------+
    3 rows in set (0.00 sec)
```

### 慢查询日志：slow query log

记录执行时长超出指定时长的查询操作。默认 MariaDB 没有启用此功能。

查看当前系统设置的时长：

```
MariaDB [(none)]> SHOW GLOBAL VARIABLES LIKE 'long_query_time';
+-----------------+-----------+
| Variable_name   | Value     |
+-----------------+-----------+
| long_query_time | 10.000000 |
+-----------------+-----------+
1 row in set (0.00 sec)

MariaDB [(none)]> SELECT @@GLOBAL.long_query_time;
+--------------------------+
| @@GLOBAL.long_query_time |
+--------------------------+
|                10.000000 |
+--------------------------+
1 row in set (0.00 sec)
```

修改当前系统设置的慢查询时长：

```
SET GLOBAL long_query_time = 
```

相关变量参数设置：

```
slow_query_log=ON|OFF：是否启用慢查询日志；
slow_query_log_file=HOSTNAME-slow.log：日志文件；
log_slow_filter = admin,filesort,filesort_on_disk,full_join,full_scan,query_cache,query_cache_miss,tmp_table,tmp_table_on_disk ：过滤，需要记录的查询事件；
log_slow_queries = OFF：是否启用慢查询日志；
log_slow_rate_limit = 1：记录日志的速率；
log_slow_verbosity：记录慢查询日志详细级别；

MariaDB [(none)]> SHOW GLOBAL VARIABLES WHERE Variable_name IN ('slow_query_log','slow_query_log_file','log_slow_filter','log_slow_queries','log_slow_rate_limit','log_slow_verbosity');
+---------------------+--------------------------------------------------------------------------------------------------------------+
| Variable_name       | Value                                                                                                        |
+---------------------+--------------------------------------------------------------------------------------------------------------+
| log_slow_filter     | admin,filesort,filesort_on_disk,full_join,full_scan,query_cache,query_cache_miss,tmp_table,tmp_table_on_disk |
| log_slow_queries    | OFF                                                                                                          |
| log_slow_rate_limit | 1                                                                                                            |
| log_slow_verbosity  |                                                                                                              |
| slow_query_log      | OFF                                                                                                          |
| slow_query_log_file | node1-slow.log                                                                                               |
+---------------------+--------------------------------------------------------------------------------------------------------------+
6 rows in set (0.00 sec)
```

### 错误日志：error log

默认已经启动此日志文件。

1、记录 mysqld 启动和关闭过程中输出的事件信息；

2、记录 mysqld 运行中产生的错误信息；

3、记录 event scheduler 运行一个 event 时产生的日志信息；

4、在主从复制架构中的从服务器上启动从服务器线程时产生的日志信息；

相关变量参数设置：

```
log_error=/PATH/TO/LOG_ERROR_FILE：错误日志文件；
log_warnings=0|1：是否记录警告信息至错误日志文件中，1表示记录，0表示不记录

MariaDB [(none)]> SHOW GLOBAL VARIABLES WHERE Variable_name IN ('log_error','log_warnings');
+---------------+---------------------+
| Variable_name | Value               |
+---------------+---------------------+
| log_error     | /var/log/mysqld.log |
| log_warnings  | 1                   |
+---------------+---------------------+
2 rows in set (0.00 sec)
```

### 二进制日志：binary log

记录导致数据改变或潜在导致数据改变的SQL语句；

功能：用于通过“重放”日志文件中的事件来生成数据副本；

`SHOW {BINARY | MASTER} LOGS`：查看mariadb自行管理使用中的二进制日志文件列表；

```
MariaDB [(none)]> SHOW BINARY LOGS;
+------------------+-----------+
| Log_name         | File_size |
+------------------+-----------+
| mysql-bin.000001 |     13746 |
| mysql-bin.000002 |       264 |
| mysql-bin.000003 |      1907 |
| mysql-bin.000004 |      7655 |
+------------------+-----------+
4 rows in set (0.00 sec)

MariaDB [(none)]> SHOW MASTER LOGS;
+------------------+-----------+
| Log_name         | File_size |
+------------------+-----------+
| mysql-bin.000001 |     13746 |
| mysql-bin.000002 |       264 |
| mysql-bin.000003 |      1907 |
| mysql-bin.000004 |      7655 |
+------------------+-----------+
4 rows in set (0.00 sec)
```

`SHOW MASTER STATUS`：查看正在使用中的二进制日志文件；

```
MariaDB [(none)]> SHOW MASTER STATUS;
+------------------+----------+--------------+------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB |
+------------------+----------+--------------+------------------+
| mysql-bin.000004 |     7655 |              |                  |
+------------------+----------+--------------+------------------+
1 row in set (0.00 sec)
```

`SHOW BINLOG EVENTS [IN 'log_name'] [FROM pos] [LIMIT [offset,] row_count]`：显示指定的二进制日志文件中的事件；
```
MariaDB [(none)]> SHOW BINLOG EVENTS IN 'mysql-bin.000004' FROM 4 LIMIT 1;
+------------------+-----+-------------+-----------+-------------+-------------------------------------------+
| Log_name         | Pos | Event_type  | Server_id | End_log_pos | Info                                      |
+------------------+-----+-------------+-----------+-------------+-------------------------------------------+
| mysql-bin.000004 |   4 | Format_desc |         1 |         245 | Server ver: 5.5.59-MariaDB, Binlog ver: 4 |
+------------------+-----+-------------+-----------+-------------+-------------------------------------------+
1 row in set (0.00 sec)
```

**二进制日志记录格式：**

1、基于“语句”记录：statement

2、基于“行”记录：row

3、混合模式：mixed，让系统自行判断应该基于上面哪种方式进行；

**二进制日志文件的构成：**

- 两类文件：
    + 日志文件：mysql-bin.文件名后缀，二进制格式；
    + 索引文件：mysql-bin.index，文本格式；记录当前使用中的binlog文件名；

**服务器变量：**

```
sql_log_bin=ON|OFF：是否记录二进制日志；
log_bin=/PATH/TO/LOGFILENAME：记录二进制日志的路径及文件名，是否记录二进制日志，通常为ON；
binlog_format=STATEMENT|ROW|MIXED：二进制日志记录的格式；
max_binlog_size=1073741824：单个二进制日志文件的最大体积，默认为1G；
    注意：（1）到达最大值会自动滚动；（2）文件达到上限时的大小未必为指定的精确值；
sync_binlog=1|0：设定是否启用二进制日志同步至磁盘文件功能，commit触发同步；

MariaDB [(none)]> SHOW VARIABLES WHERE Variable_name IN ('sql_log_bin','log_bin','binlog_format','max_binlog_size','sync_binlog');
+-----------------+------------+
| Variable_name   | Value      |
+-----------------+------------+
| binlog_format   | MIXED      |
| log_bin         | ON         |
| max_binlog_size | 1073741824 |
| sql_log_bin     | ON         |
| sync_binlog     | 0          |
+-----------------+------------+
5 rows in set (0.00 sec)
```

**`mysqlbinlog`：客户端命令工具**

基于 mysql 协议链接至服务器端读取 mysql binlog 日志，也可查询本地 binglog 日志。

```
mysqlbinlog [OPTIONS] log file
    --start-position
    --stop-position

    --start-datetime=
    --stop-datetime=
        YYYY-MM-DD hh:mm:ss

Example:
[root@node1 mysql]# ./bin/mysqlbinlog --start-position 7655 --stop-position 7738 /mydata/data/mysql-bin.000004
/*!50530 SET @@SESSION.PSEUDO_SLAVE_MODE=1*/;
/*!40019 SET @@session.max_insert_delayed_threads=0*/;
/*!50003 SET @OLD_COMPLETION_TYPE=@@COMPLETION_TYPE,COMPLETION_TYPE=0*/;
DELIMITER /*!*/;
# at 4
#180527 16:54:40 server id 1  end_log_pos 245   Start: binlog v 4, server v 5.5.59-MariaDB created 180527 16:54:40 at startup
# Warning: this binlog is either in use or was not closed properly.
ROLLBACK/*!*/;
BINLOG '
UHIKWw8BAAAA8QAAAPUAAAABAAQANS41LjU5LU1hcmlhREIAbG9nAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAABQcgpbEzgNAAgAEgAEBAQEEgAA2QAEGggAAAAICAgCAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAPbqXGw==
'/*!*/;
# at 7655
#180603 18:20:18 server id 1  end_log_pos 7738  Query   thread_id=24    exec_time=0     error_code=0
SET TIMESTAMP=1528021218/*!*/;
SET @@session.pseudo_thread_id=24/*!*/;
SET @@session.foreign_key_checks=1, @@session.sql_auto_is_null=0, @@session.unique_checks=1, @@session.autocommit=1/*!*/;
SET @@session.sql_mode=0/*!*/;
SET @@session.auto_increment_increment=1, @@session.auto_increment_offset=1/*!*/;
/*!\C utf8 *//*!*/;
SET @@session.character_set_client=33,@@session.collation_connection=33,@@session.collation_server=8/*!*/;
SET @@session.lc_time_names=0/*!*/;
SET @@session.collation_database=DEFAULT/*!*/;
CREATE DATABASE mydb
/*!*/;
DELIMITER ;
# End of log file
ROLLBACK /* added by mysqlbinlog */;
/*!50003 SET COMPLETION_TYPE=@OLD_COMPLETION_TYPE*/;
/*!50530 SET @@SESSION.PSEUDO_SLAVE_MODE=0*/;
```

**二进制日志事件的格式：**

```
# at 7738
#180603 18:20:54 server id 1  end_log_pos 7841  Query   thread_id=24    exec_time=0     error_code=0
use `mydb`/*!*/;
SET TIMESTAMP=1528021254/*!*/;
CREATE TABLE tb1 (id int, name char(30))
/*!*/;

180603 18:20:54：事件发生的日期和时间；
server id 1：事件发生的服务器标示；
end_log_pos 7841：事件的结束位置；
Query：事件类型；
thread_id=24：事件发生时所在服务器执行此事件的线程的ID；
exec_time=0：语句的时间戳与将其写入二进制文件中的事件差；
error_code=0：错误代码；
余下的为事件内容；
```

GTID：Global Transaction ID：事件发生时所属的事件 ID 号；MySQL5.6 之后的版本，binglog 可能包含此项；

### 中继日志：relay log

复制架构中，从服务器用于保存从主服务器的二进制日志中读取到的事件；后面讲主从复制会详细讲；

```
MariaDB [mydb]> SHOW VARIABLES LIKE 'relay_log';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| relay_log     |       |
+---------------+-------+
1 row in set (0.00 sec)
```

### 事务日志：transaction

帮助事务型存储引擎能够满足 ACID 测试，事务型存储引擎自行管理和使用；

> “事务日志可以帮助提高事务的效率。使用事务日志，存储引擎在修改表的数据时只需要修改其内存拷贝，再把该修改行为记录到持久在硬盘上的事务日志中，而不用每次都将修改的数据本身持久到磁盘。事务日志采用的是追加的方式，因此写日志的操作是磁盘上一小块“区域内的顺序I/O，而不像随机I/O需要在磁盘的多个地方移动磁头，所以采用事务日志的方式相对来说要快得多。事务日志持久以后，内存中被修改的数据在后台可以慢慢地刷回到磁盘。目前大多数存储引擎都是这样实现的，我们通常称之为预写式日志（Write-Ahead Logging），修改数据需要写两次磁盘。

> 如果数据的修改已经记录到事务日志并持久化，但数据本身还没有写回磁盘，此时系统崩溃，存储引擎在重启时能够自动恢复这部分修改的数据。具体的恢复方式则视存储引擎而定。”

> 摘录来自: 施瓦茨 (Baron Schwartz). “高性能MySQL(第3版)”。 

事务日志分组进行使用，每个组内要有两个或以上的文件；同时事务日志不应太大，太大会导致如果服务器崩溃恢复的时间太长；也不应太小，太小会导致日志文件频繁写入磁盘；具体大小应根据数据库业务数据增长量做评估；

```
innodb_log_file_size：InnoDB事务日志文件大小，默认为5M；
innodb_log_files_in_group：InnoDB事务日志组中文件个数，默认为2个；
innodb_log_group_home_dir：InnoDB事务日志组文件存放目录；

MariaDB [mydb]> SHOW VARIABLES WHERE Variable_name IN ('innodb_log_file_size','innodb_log_files_in_group','innodb_log_group_home_dir');
+---------------------------+---------+
| Variable_name             | Value   |
+---------------------------+---------+
| innodb_log_file_size      | 5242880 |
| innodb_log_files_in_group | 2       |
| innodb_log_group_home_dir | ./      |
+---------------------------+---------+
3 rows in set (0.00 sec)
```

**redo log：**

用于数据库崩溃时，重做已经提交事务，将没有写入数据库的数据写入；

**undo log：**

用于数据库崩溃时，撤销未提交提交事务，已经写入数据库的数据撤销；


（完）