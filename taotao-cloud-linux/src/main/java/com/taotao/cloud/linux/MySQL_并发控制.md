    第 40 天 【MariaDB存储引擎及编译安装(03)】

## MySQL 并发控制

### 锁

- 读锁：共享锁
- 写锁：独占锁

### 锁粒度

- 表级锁
- 行级锁
- 锁策略：在锁粒度及数据安全性寻求的平衡机制；

每种存储引擎都可以自行实现其锁策略和锁粒度；MySQL 在服务器级也实现了锁--表级锁；

用户可以显示请求锁：
```

（1）、
    Syntax:
    LOCK TABLES
        tbl_name [[AS] alias] lock_type
        [, tbl_name [[AS] alias] lock_type] ...

    lock_type:
        READ [LOCAL]
      | [LOW_PRIORITY] WRITE

    UNLOCK TABLES

    Example:
        MariaDB [hellodb]> LOCK TABLES students WRITE;
        Query OK, 0 rows affected (0.00 sec)

（2）、
    Syntax:
    FLUSH TABLES tb_name[,...] [WITH READ LOCK]   //只能请求读锁。

    Example:
        FLUSH TABLES students WITH READ LOCK;

（3）、
    SELECT clause FROM tbl_name FOR UPDATE.  

    Example:
        MariaDB [testdb]> SELECT * FROM students FOR UPDATE;
```

第三种方式马哥没有讲清楚，其需要事务的支持，MyISAM 是不支持事务的，需要进一步说明。

`SELECT caluse FROM tbl_name FOR UPDATE`的具体使用方法及使用场景建议参考：<https://my.oschina.net/u/347414/blog/736256>

（完）